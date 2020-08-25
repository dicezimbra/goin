package br.com.goin.feature.feed.commentary

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.visible
import br.com.goin.base.utils.Constants.POST_ID
import br.com.goin.base.utils.Constants.POST_POS
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.base.utils.Constants.*
import br.com.goin.component.session.user.UserModel
import br.com.goin.component.ui.kit.dialog.DialogUtils
import br.com.goin.feature.feed.R
import br.com.goin.feature.feed.commentary.adapter.CommentAdapter
import br.com.goin.feature.feed.commentary.model.Comments
import br.com.goin.feature.feed.commentary.model.CreateComment
import br.com.goin.feature.feed.model.Post
import br.com.goin.feature.feed.post.PostUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.transitionseverywhere.Fade
import com.transitionseverywhere.TransitionManager
import com.transitionseverywhere.TransitionSet
import com.transitionseverywhere.extra.Scale
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.item_commentary.*
import kotlinx.android.synthetic.main.item_user_post.*
import java.text.SimpleDateFormat
import java.util.*

class CommentActivity : AppCompatActivity(), CommentView {

    private val presenter = CommentPresenterImpl(this)
    private var adapter: CommentAdapter? = null

    companion object {
        fun starter(activity: Activity, postId: String?, post: Post?) {
            val intent = Intent(activity, CommentActivity::class.java)
            intent.putExtra(POST_ID, postId)
            intent.putExtra(POST_POS, post)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        presenter.onCreate(intent?.extras?.getString(POST_ID), intent?.extras?.getSerializable(POST_POS))
    }

    override fun onResume() {
        super.onResume()
        Analytics.instance.screenView(this, getString(R.string.comment_screen_view))
    }

    override fun onClickListeners(postId: String?, userId: String?) {
        post_comment_button.setOnClickListener {

            val comment = CreateComment()
            comment.description = PostUtils.getPostDescriptionWithTags(commentary)
            comment.postId = postId!!
            commentary.setText("")
            presenter.sendComment(comment)
        }
    }

    override fun intentPost(intent: Post) {
        if (intent.sharedBy == null) {
            shared_profile_name_txt.gone()
            shared_profile_image.gone()
            feed_card_shared_overflow_button.gone()
            shared_divider.gone()
            bottom_line.gone()
        } else {
            bottom_line.gone()
            shared_divider.visible()
            shared_profile_name_txt.text = intent.sharedBy?.name
            Glide.with(this@CommentActivity)
                    .load(intent.sharedBy?.avatar)
                    .apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions().placeholder(R.drawable.ic_user))
                    .into(shared_profile_image)
            shared_profile_name_txt.visible()
            shared_profile_image.visible()
            feed_card_shared_overflow_button.visible()
        }


        Glide.with(this@CommentActivity)
                .load(intent.avatar)
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions().placeholder(R.drawable.ic_user))
                .into(profile_image)
        profile_name_txt.text = intent.name
        profile_name_txt.typeface = Typeface.createFromAsset(this.assets, "fonts/Quicksand-Bold.ttf")
        commentary.typeface = Typeface.createFromAsset(this.assets, "fonts/Quicksand-Bold.ttf")
        intent.timeStamp?.let {

            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy 'às' HH:mm'h'", Locale.getDefault())
            val date = simpleDateFormat.format(Date(it))
            txt_hour_feed.text = date
        }

        txt_feed_subject.text = intent.description
        txt_feed_subject.typeface = Typeface.createFromAsset(this.assets, "fonts/Quicksand-Medium.ttf")

        if (!intent.image.isNullOrEmpty()) {
            Glide.with(this@CommentActivity)
                    .load(intent.image)
                    .into(photo_feed)
            photo_feed.visible()
        } else {
            photo_feed.gone()
        }

        if (!intent.video.isNullOrEmpty()) {
            Glide.with(this@CommentActivity)
                    .load(intent.image)
                    .into(photo_feed)
            icon_play.visible()
        } else {
            icon_play.gone()
        }

        txt_icon_feed.text = intent.likesCount.toString()
        txt_icon_feed.typeface = Typeface.createFromAsset(this.assets, "fonts/Quicksand-Medium.ttf")
        feed_comment_label.text = intent.commentsCount.toString()
        feed_comment_label.typeface = Typeface.createFromAsset(this.assets, "fonts/Quicksand-Medium.ttf")

        feed_comment_label.gone()
        txt_icon_feed.gone()
        icon_comment_feed.gone()
        icon_like_feed.gone()
    }

    override fun configureToolbar() {
        toolbar_comment.setOnBackButtonClicked { onBackPressed() }
        toolbar_comment.title = getString(R.string.comments)
    }

    override fun setUserInfo(it: UserModel) {
        Glide.with(this)
                .load(it.profilePicture)
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions().placeholder(R.drawable.ic_user))
                .into(add_comment_user)
    }

    override fun showMessageNoComments() {
        no_comments_message.visible()
    }

    override fun onLoadComments(comments: MutableList<Comments>) {
        if (no_comments_message != null) {
            no_comments_message.gone()
        }

        adapter = CommentAdapter(comments)
        adapter?.notifyDataSetChanged()

        recycler_view_commentaries.adapter = adapter
        recycler_view_commentaries.layoutManager = LinearLayoutManager(this)

        val set = TransitionSet()
                .addTransition(Scale(0.7f))
                .addTransition(Fade())
                .setInterpolator(LinearOutSlowInInterpolator())
        TransitionManager.beginDelayedTransition(recycler_view_commentaries, set)
        recycler_view_commentaries.visibility = View.VISIBLE



        adapter?.onOptionsClickListener = { postId, commentId ->
            DialogUtils.showDialogOptions(this@CommentActivity,
                    getString(R.string.msg_delete),
                    getString(R.string.yes_dialog),
                    getString(R.string.no_dialog)) {
                presenter.onDeleteComment(postId, commentId)
            }
        }
    }

    override fun emptyComments(message: String?) {
        recycler_view_commentaries.gone()
        no_comments_message.visible()
    }

    override fun hideLoading() {
        loading_comments.gone()
    }

    override fun showLoading() {
        loading_comments.isIndeterminate = true
        loading_comments.visible()
    }
}