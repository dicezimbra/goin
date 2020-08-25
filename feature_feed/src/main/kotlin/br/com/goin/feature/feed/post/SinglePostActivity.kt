package br.com.goin.feature.feed.post

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.view.View
import android.widget.ImageView

import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.toTitleCase
import br.com.goin.base.extensions.visible
import br.com.goin.component.navigation.NavigationController
import br.com.goin.component.session.user.UserHelper
import br.com.goin.feature.feed.FeedHelper
import br.com.goin.feature.feed.R
import br.com.goin.feature.feed.model.Post
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_single_post_feed.*
import java.text.SimpleDateFormat
import java.util.*

private const val COMMENT_REQUEST_CODE = 1001

class SinglePostActivity : AppCompatActivity(), SinglePostView {

    companion object {
        private const val POST = "POST"

        fun starter(activity: Activity, post: Post, cardView: CardView) {
            val intent = Intent(activity, SinglePostActivity::class.java)
            intent.putExtra(POST, post)

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, cardView, "cardView")
            activity.startActivity(intent, options.toBundle())
        }

        fun starter(context: Context, post: Post) {
            val intent = Intent(context, SinglePostActivity::class.java)
            intent.putExtra(POST, post)
            context.startActivity(intent)
        }
    }

    private val presenter: SinglePostPresenter = SinglePostPresenterImpl(this)
    private val navigationController = NavigationController.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_post_feed)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val fade = Fade()
            fade.excludeTarget(R.id.txt_feed_subject, true)
            fade.excludeTarget(R.id.icon_comment_feed, true)
            fade.excludeTarget(R.id.txt_icon_feed, true)
            fade.excludeTarget(R.id.icon_like_feed, true)
            fade.excludeTarget(R.id.feed_comment_label, true)
            fade.excludeTarget(R.id.icon_play, true)
            fade.excludeTarget(R.id.photo_feed, true)

            window.enterTransition = fade
            window.exitTransition = fade
        }

        presenter.onReceivePost(intent.getSerializableExtra(POST))
        presenter.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun configureOnClick(post: Post, currentUserId: String){
        toolbar_left_icon.setOnClickListener {
            supportFinishAfterTransition()
        }

        icon_comment_feed.setOnClickListener {
            onLikeClicked(post)
        }

        txt_icon_feed.setOnClickListener {
            onLikeClicked(post)
        }

        icon_like_feed.setOnClickListener {
            navigationController?.legacyApp()?.goToComment(this, post.postId, COMMENT_REQUEST_CODE)
        }
        feed_comment_label.setOnClickListener {
            navigationController?.legacyApp()?.goToComment(this, post.postId, COMMENT_REQUEST_CODE)
        }

        profile_image.setOnClickListener {
            navigationController?.profileModule()?.goToProfile(this, post.posterId)
        }

        profile_name_txt.setOnClickListener {
            navigationController?.profileModule()?.goToProfile(this, post.posterId)
        }

        shared_profile_image.setOnClickListener {
            navigationController?.profileModule()?.goToProfile(this, post.sharedBy?.id)
        }

        shared_profile_name_txt.setOnClickListener {
            navigationController?.profileModule()?.goToProfile(this, post.sharedBy?.id)
        }

        feed_card_shared_overflow_button.setOnClickListener {
            if(post.sharedBy?.id == currentUserId) deletePost() else reportPost()
        }

        feed_card_overflow_button.setOnClickListener {
            if(post.postId == currentUserId) deletePost() else reportPost()
        }
    }


    override fun configurePost(post: Post) {
        post.timeStamp?.let { timeStamp ->
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy às hh:mm", Locale.getDefault())
            txt_hour_feed.text = simpleDateFormat.format(Date(timeStamp))
        }

        profile_name_txt.text = post.name?.toTitleCase()
        txt_icon_feed.text = post.likesCount?.toString()
        feed_comment_label.text = post.commentsCount?.toString()

        if (post.description.isNullOrBlank()) {
            txt_feed_subject.gone()
        } else {
            txt_feed_subject.text = post.description?.toTitleCase()
            txt_feed_subject.visible()
        }

        icon_comment_feed.isSelected = post.likedByMe
        txt_icon_feed.text = post.likesCount?.toString()

        val requestOptionsAvatar = RequestOptions()
                .circleCrop()
                .placeholder(R.drawable.background_circle_placeholder)
                .error(R.drawable.background_circle_placeholder)

        Glide.with(this)
                .load(post.avatar)
                .apply(requestOptionsAvatar)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(RequestOptions().signature(UserHelper.getSignature()))
                .into(profile_image)

        post.image?.let {
            Glide.with(this).load(post.image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(photo_feed)
        }
    }

    override fun configureSharedPost(post: Post) {
        if (post.sharedBy == null) {
            shared_divider.gone()
            shared_profile_image.gone()
            shared_profile_name_txt.gone()
            feed_card_shared_overflow_button.gone()

        } else {
            shared_divider.visible()
            shared_profile_image.visible()
            shared_profile_name_txt.visible()
            feed_card_shared_overflow_button.visible()
            shared_profile_name_txt.text = post.sharedBy?.name?.toTitleCase()

            val requestOptionsAvatar = RequestOptions()
                    .circleCrop()
                    .placeholder(R.drawable.background_circle_placeholder)
                    .error(R.drawable.background_circle_placeholder)

            Glide.with(this).load(post.sharedBy?.avatar)
                    .apply(requestOptionsAvatar)
                    .apply(RequestOptions().signature(UserHelper.getSignature()))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(shared_profile_image)
        }
    }

    private fun onLikeClicked(post: Post) {
        post.likedByMe = !post.likedByMe
        var likesCount = post.likesCount ?: 0

        if (post.likedByMe) {
            likesCount = likesCount.plus(1)
        } else {
            likesCount = likesCount.minus(1)
        }

        post.likesCount = likesCount
        icon_comment_feed.isSelected = post.likedByMe
        txt_icon_feed.text = post.likesCount.toString()

        presenter.onLikeClicked()
    }

    private fun reportPost() {
        FeedHelper.showReportDialog(this) { presenter.reportPost() }
    }

    private fun deletePost() {
        FeedHelper.showDeleteDialog(this) { presenter.deletePost() }
    }
}
