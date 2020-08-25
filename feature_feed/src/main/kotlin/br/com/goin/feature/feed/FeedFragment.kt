package br.com.goin.feature.feed

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkManager
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.visible
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.navigation.NavigationController
import br.com.goin.component.session.user.UserHelper
import br.com.goin.component.ui.kit.features.error.ErrorViewHelper
import br.com.goin.feature.feed.model.Post
import br.com.goin.feature.feed.post.PostType
import br.com.goin.feature.feed.post.SinglePostActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.item_new_feed_bar.*
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import br.com.goin.feature.feed.commentary.CommentActivity
import com.transitionseverywhere.Fade
import com.transitionseverywhere.TransitionManager
import com.transitionseverywhere.TransitionSet
import com.transitionseverywhere.extra.Scale
import kotlinx.android.synthetic.main.layout_new_post_itens.view.*


private const val COMMENT_REQUEST_CODE = 1001

class FeedFragment : Fragment(), FeedView {

    companion object {
        private const val USER_ID = "USER_ID"

        fun newInstance(userId: String? = null): Fragment {
            val bundle = Bundle()
            bundle.putString(USER_ID, userId)

            val fragment = FeedFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val presenter: FeedPresenter = FeedPresenterImpl(this)
    private val navigationController = NavigationController.instance
    private var adapter: FeedAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setTitle(R.string.feed)
        toolbar.hideBackButton()

        toolbar.setRightSecondButton(R.drawable.ico_add_amigo) {
            navigationController?.findFriendsModule()?.goToFindFriends(requireContext())
        }

        toolbar.setRightButton(R.drawable.icon_notificacao) {
            navigationController?.notificationModule()?.goToNotifications(activity!!)
        }

        presenter.onReceivedProfileId(arguments?.getString(USER_ID))
        presenter.onCreate()
    }

    override fun onResume() {
        super.onResume()
        presenter.logScreenName()
    }

    override fun logScreenName(){
        Analytics.instance.screenView(activity!!, getString(R.string.feed_screen_name))
    }

    override fun onStart() {
        super.onStart()
        val workManager = WorkManager.getInstance()
        workManager?.getStatusesByTag("createPost")?.observe(this, androidx.lifecycle.Observer {
            presenter.onBackgroundTasks(it)
        })
    }

    override fun onStop() {
        super.onStop()

        val workManager = WorkManager.getInstance()
        workManager?.getStatusesByTag("createPost")?.removeObservers(this)
    }

    override fun onRunningTask() {
        val set = TransitionSet()
                .addTransition(Scale(0.7f))
                .addTransition(Fade())
                .setInterpolator(LinearOutSlowInInterpolator())
        TransitionManager.beginDelayedTransition(publish_container, set)
        more_posts_loading.visibility = View.VISIBLE
        title_feed_new_post.visibility = View.VISIBLE

    }

    override fun onSucceededTask() {
        more_posts_loading.visibility = View.GONE
        title_feed_new_post.visibility = View.GONE

        presenter.loadFeedByTask()
        val workManager = WorkManager.getInstance()
        workManager.cancelAllWorkByTag("createPost")
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun configureUserAvatar(profilePicture: String) {
        Glide.with(context!!)
                .load(profilePicture)
                .transition(withCrossFade())
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.background_circle_placeholder))
                .apply(RequestOptions().signature(UserHelper.getSignature()))
                .into(imageViewUser)
    }


    override fun configureRecyclerview(userId: String, isProfile: Boolean) {
        val linearLayoutManager = LinearLayoutManager(context!!)
        recycler_view.layoutManager = linearLayoutManager
        adapter = FeedAdapter(myId = userId)
        recycler_view_header.attachTo(recycler_view)

        if (isProfile) {
            profile_header.gone()
        }

        recycler_view.adapter = adapter
        val animation = AnimationUtils.loadLayoutAnimation(context!!, R.anim.layout_animation_fall_down)
        recycler_view.layoutAnimation = animation

        adapter?.apply {
            onBottomReachedListener = {
                presenter.loadMorePosts()
            }

            onPhotoClickListener = { view, url ->
                navigationController?.fullImageModule()?.goToFullImage(activity!!, view, url)
            }

            onProfileClickListener = { posterId ->
                posterId?.let { navigationController?.profileModule()?.goToProfile(activity!!, posterId) }
            }

            onPostOptionsClickListener = { postId, posterId ->
                if (posterId == userId) deletePost(postId) else reportPost(postId)
            }

            onLikeClickListener = { post ->
                presenter.onLikeClicked(post)
            }

            onCommentClickListener = { postId, postPos ->
                if (!postId.isNullOrEmpty() && postPos != null) {
                    CommentActivity.starter(activity!!, postId, postPos)
                }
            }

            onReadMoreClickListener = { post, cardView ->
                SinglePostActivity.starter(activity!!, post, cardView)
            }

            onVideoClickListener = { url ->
                navigationController?.legacyApp()?.goToPlayerActivity(activity!!, url)
            }

            onUsedMentionedClickListener = {
                navigationController?.profileModule()?.goToProfile(activity!!, it)
            }
        }
    }

    override fun configurePosts(posts: List<Post>) {
        adapter?.addPosts(posts)
    }

    override fun updatePosts() {
        adapter?.notifyDataSetChanged()
    }

    override fun configureOnClickListener() {
        activity?.let { activity ->
            newPostItens.location_post_button.setOnClickListener {

                navigationController?.legacyApp()?.goToPost(activity, PostType.CheckIn.toString())
            }
            newPostItens.insert_photo_button.setOnClickListener {

                navigationController?.legacyApp()?.goToPost(activity, PostType.Picture.toString())
            }

            newPostItens.insert_video_button_post.setOnClickListener {

                navigationController?.legacyApp()?.goToPost(activity, PostType.Video.toString())
            }

            newPostItens.feeling_post_button.setOnClickListener {

                navigationController?.legacyApp()?.goToPost(activity, PostType.Feeling.toString())
            }

            title_post_something.setOnClickListener {
                navigationController?.legacyApp()?.goToPost(activity)
            }
        }
    }

    private fun reportPost(postId: String?) {
        FeedHelper.showReportDialog(context!!) { presenter.reportPost(postId) }
    }

    private fun deletePost(postId: String?) {
        FeedHelper.showDeleteDialog(context!!) { presenter.deletePost(postId) }
    }

    override fun showSuccessMessage() {
        FeedHelper.showSuccessReportMessage(activity!!, getString(R.string.thank_you),
                getString(R.string.thank_you_for_report), getString(R.string.ok))
    }

    override fun configureEmptyFeed() {
        no_more_posts.visible()
        recycler_view.gone()
    }

    override fun showLoading() {
        more_posts_loading.visible()
        no_more_posts.gone()
        title_feed_new_post.visibility = View.GONE
    }

    override fun hideLoading() {
        more_posts_loading.gone()
        title_feed_new_post.visibility = View.GONE
    }

    override fun handleError(throwable: Throwable) {
        ErrorViewHelper.handleErrorView(coordinator_layout, throwable) {
            presenter.onCreate()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == COMMENT_REQUEST_CODE) {
            data?.let {
                val comment = it.getStringExtra("LAST_COMMENT_CONTENT")
                val commentAuthor = it.getStringExtra("LAST_COMMENT_AUTHOR")
                val postId = it.getStringExtra("POST_ID")
                presenter.onCommentCreated(postId, comment, commentAuthor)
            }
        }
    }
}