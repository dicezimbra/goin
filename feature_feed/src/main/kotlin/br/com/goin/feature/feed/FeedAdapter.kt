package br.com.goin.feature.feed

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.RecyclerView
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.toTitleCase
import br.com.goin.base.extensions.visible
import br.com.goin.component.session.user.UserHelper
import br.com.goin.feature.feed.model.Post
import br.com.goin.feature.feed.post.PostActivityHelper
import br.com.goin.feature.feed.post.PostUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_feed.view.*
import java.text.SimpleDateFormat
import java.util.*


class FeedAdapter(var posts: MutableList<Post> = arrayListOf(),
                  var myId: String?) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    var onLikeClickListener: ((Post) -> Unit)? = null
    var onCommentClickListener: ((String?, Post?) -> Unit)? = null
    var onPhotoClickListener: ((ImageView, String) -> Unit)? = null
    var onVideoClickListener: ((String?) -> Unit)? = null
    var onProfileClickListener: ((String?) -> Unit)? = null
    var onPostOptionsClickListener: ((String?, String?) -> Unit)? = null
    var onReadMoreClickListener: ((Post, CardView) -> Unit)? = null
    var onBottomReachedListener: (() -> Unit)? = null
    var onUsedMentionedClickListener: ((userId: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: FeedViewHolder, position: Int) {
        val post = posts[position]
        val context = viewHolder.itemView.context

        val typefaced = Typeface.createFromAsset(context.assets, "fonts/Quicksand-Medium.ttf")

        post.timeStamp?.let { timeStamp ->
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy 'às' HH:mm'h'", Locale.getDefault())
            viewHolder.timeStamp.text = simpleDateFormat.format(Date(timeStamp))
            viewHolder.timeStamp.typeface = typefaced
        }

        viewHolder.profileName.text = post.name?.toTitleCase()
        viewHolder.profileName.typeface = typefaced
        viewHolder.likeCount.text = post.likesCount?.toString()
        viewHolder.commentCount.text = post.commentsCount?.toString()
        viewHolder.commentCount.typeface = typefaced
        viewHolder.readMore.typeface = typefaced
        viewHolder.readMore.gone()

        val builder = SpannableStringBuilder()

        val spanUser = SpannableString(viewHolder.profileName.text.toString())
        spanUser.setSpan(StyleSpan(Typeface.BOLD), 0, viewHolder.profileName.text.length, 0)
        builder.append(spanUser)

        post.video?.let {
            if (it.contains("playlist.m3u8")) {
                viewHolder.iconPlay.visible()
                viewHolder.iconPlay.setOnClickListener { onVideoClickListener?.invoke(post.video!!) }
            } else {
                viewHolder.iconPlay.gone()
            }
        }

        if (!post.feeling.isNullOrEmpty()) {

            val spanFeelings = SpannableString(" " + context?.resources?.getString(R.string.is_feeling) + " ")
            builder.append(spanFeelings)

            val spanFeeling = SpannableString(post.feeling)
            val spanCapitalized = spanFeeling.substring(0, 1).toLowerCase() + spanFeeling.substring(1)
            spanFeeling.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorPrimaryDark)), 0, spanCapitalized.length, 0)
            builder.append(spanCapitalized)

            val feelingText = when (post.feeling) {
                context.getString(R.string.emoji_glad) -> "\uD83D\uDE42"
                context.getString(R.string.emoji_excited) -> "\uD83E\uDD17"
                context.getString(R.string.emoji_happy) -> "\uD83D\uDE01"
                context.getString(R.string.emoji_cool) -> "\uD83D\uDE0F"
                context.getString(R.string.emoji_anxious) -> "\uD83D\uDE2B"
                context.getString(R.string.emoji_joyful) -> "\uD83D\uDE1C"
                context.getString(R.string.emoji_seductive) -> "\uD83D\uDE09"
                context.getString(R.string.emoji_angry) -> "\uD83D\uDE21"
                context.getString(R.string.emoji_sick) -> "\uD83E\uDD22"
                else -> ""
            }
            builder.append(" $feelingText")

        } else if (!post.eventName.isNullOrEmpty()) {
            val spanDidCheckIn = SpannableString(" " + context.resources.getString(R.string.did_checkin) + " ")
            builder.append(spanDidCheckIn)

            val spanEvent = SpannableString(post.eventName)
            spanEvent.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorPrimaryDark)), 0, post.eventName!!.length, 0)
            builder.append(spanEvent)
        } else if (!post.clubName.isNullOrEmpty()) {
            val spanDidCheckIn = SpannableString(" " + context.resources.getString(R.string.did_checkin) + " ")
            builder.append(spanDidCheckIn)

            val spanEvent = SpannableString(post.clubName)
            spanEvent.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorPrimaryDark)), 0, post.clubName!!.length, 0)
            builder.append(spanEvent)
        }
        viewHolder.profileName.setText(builder, TextView.BufferType.SPANNABLE)


        if (post.description.isNullOrBlank()) {
            viewHolder.subject.gone()
            viewHolder.subject.typeface = typefaced
            viewHolder.readMore.gone()
        } else {
            viewHolder.subject.text = post.description?.toTitleCase()
            viewHolder.subject.typeface = typefaced
            viewHolder.subject.visible()

            val spanDescription =
                    FeedHelper.getPostWithTags(
                            viewHolder.subject.text.toString()) {
                        onUsedMentionedClickListener?.invoke(it)
                    }
            viewHolder.subject.movementMethod = LinkMovementMethod.getInstance()
            viewHolder.subject.setText(spanDescription, TextView.BufferType.SPANNABLE)

            configureReadMore(viewHolder)
        }

        viewHolder.iconLike.isSelected = post.likedByMe
        viewHolder.likeCount.text = post.likesCount?.toString()
        viewHolder.likeCount.typeface = typefaced

        val requestOptionsAvatar = RequestOptions()
                .circleCrop()
                .placeholder(R.drawable.background_circle_placeholder)
                .error(R.drawable.background_circle_placeholder)

        Glide.with(context)
                .load(post.avatar)
                .apply(requestOptionsAvatar)
                .transition(withCrossFade())
                .apply(RequestOptions().signature(UserHelper.getSignature()))
                .into(viewHolder.avatar)

        post.image?.let {
            Glide.with(context).load(post.image)
                    .transition(withCrossFade())
                    .into(viewHolder.photoFeed)
            viewHolder.photoFeed.visibility = View.VISIBLE
        }

        configureComment(viewHolder, post)
        configureShared(post, viewHolder, context)
        configureOnClickListeners(viewHolder, post)

        if (position == posts.size - 1) {
            onBottomReachedListener?.invoke()
        }
    }

    override fun onViewRecycled(holder: FeedViewHolder) {
        super.onViewRecycled(holder)
        Glide.with(holder.itemView.context).clear(holder.photoFeed)
        Glide.with(holder.itemView.context).clear(holder.avatar)
        Glide.with(holder.itemView.context).clear(holder.sharedAvatar)
    }

    fun addPosts(posts: List<Post>) {
        this.posts.clear()
        this.posts.addAll(posts)
        notifyDataSetChanged()
    }

    private fun configureShared(post: Post, viewHolder: FeedViewHolder, context: Context) {
        if (post.sharedBy == null) {
            viewHolder.sharedDivier.gone()
            viewHolder.sharedAvatar.gone()
            viewHolder.sharedProfileName.gone()
            viewHolder.sharedOptions.gone()

        } else {
            viewHolder.sharedDivier.visible()
            viewHolder.sharedAvatar.visible()
            viewHolder.sharedProfileName.visible()
            viewHolder.sharedOptions.visible()
            viewHolder.sharedProfileName.text = context.getString(R.string.shared_a_post, post.sharedBy?.name?.toTitleCase())

            val requestOptionsAvatar = RequestOptions()
                    .circleCrop()
                    .placeholder(R.drawable.background_circle_placeholder)
                    .error(R.drawable.background_circle_placeholder)

            Glide.with(context).load(post.sharedBy?.avatar)
                    .apply(requestOptionsAvatar)
                    .apply(RequestOptions().signature(UserHelper.getSignature()))
                    .transition(withCrossFade())
                    .into(viewHolder.sharedAvatar)
        }
    }

    private fun configureComment(viewHolder: FeedViewHolder, post: Post) {
        if (post.commentAuthor.isNullOrBlank()) {
            viewHolder.commentAuthor.gone()
            viewHolder.comment.gone()
            viewHolder.bottomLine.visibility = View.GONE

        } else {
            viewHolder.bottomLine.visibility = View.VISIBLE
            viewHolder.commentAuthor.visible()
            viewHolder.comment.visible()

            viewHolder.commentAuthor.text = post.commentAuthor?.toTitleCase()
            if (post.comment?.startsWith("@")!!) {
                val commentTemp = post.comment?.substring(post.comment!!.indexOf(":"), post.comment!!.indexOf("/"))!!.replace(":", "")
                viewHolder.comment.text = commentTemp
            } else {
                viewHolder.comment.text = post.comment?.toTitleCase()
            }
        }
    }

    private fun configureOnClickListeners(viewHolder: FeedViewHolder, post: Post) {
        viewHolder.likeCount.setOnClickListener {
            onLikeClicked(post, viewHolder)
        }
        viewHolder.iconLike.setOnClickListener {
            onLikeClicked(post, viewHolder)
        }

        viewHolder.readMore.setOnClickListener { onReadMoreClickListener?.invoke(post, viewHolder.cardView) }
        viewHolder.photoFeed.setOnClickListener { onPhotoClickListener?.invoke(viewHolder.photoFeed, post.image!!) }

        viewHolder.commentCount.setOnClickListener { onCommentClickListener?.invoke(post.postId, posts[viewHolder.adapterPosition]) }
        viewHolder.iconComment.setOnClickListener { onCommentClickListener?.invoke(post.postId, posts[viewHolder.adapterPosition]) }

        viewHolder.profileName.setOnClickListener { onProfileClickListener?.invoke(post.posterId) }
        viewHolder.sharedProfileName.setOnClickListener { onProfileClickListener?.invoke(post.posterId) }
        viewHolder.avatar.setOnClickListener { onProfileClickListener?.invoke(post.posterId) }
        viewHolder.sharedAvatar.setOnClickListener { onProfileClickListener?.invoke(post.posterId) }

        viewHolder.options.setOnClickListener { onPostOptionsClickListener?.invoke(post.postId, post.posterId) }
        viewHolder.sharedOptions.setOnClickListener { onPostOptionsClickListener?.invoke(post.postId, post.sharedBy?.id) }
    }

    private fun onLikeClicked(post: Post, viewHolder: FeedViewHolder) {
        post.likedByMe = !post.likedByMe
        var likesCount = post.likesCount ?: 0

        if (post.likedByMe) {
            likesCount = likesCount.plus(1)
        } else {
            likesCount = likesCount.minus(1)
        }

        post.likesCount = likesCount
        viewHolder.iconLike.isSelected = post.likedByMe
        viewHolder.likeCount.text = post.likesCount.toString()
        onLikeClickListener?.invoke(post)
    }

    private fun configureReadMore(viewHolder: FeedViewHolder) {
        val description = viewHolder.subject
        description.doOnPreDraw {
            val layout = description.layout
            if (layout != null &&
                    layout.getEllipsisCount(description.lineCount - 1) > 0) {
                viewHolder.readMore.visible()
            }
        }
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    class FeedViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val sharedAvatar = itemView.shared_profile_image
        val sharedProfileName = itemView.shared_profile_name_txt
        val sharedDivier = itemView.shared_divider
        val sharedOptions = itemView.feed_card_shared_overflow_button
        val avatar = itemView.profile_image
        val photoFeed = itemView.photo_feed
        val profileName = itemView.profile_name_txt
        val subject = itemView.txt_feed_subject
        val likeCount = itemView.txt_icon_feed
        val commentCount = itemView.feed_comment_label
        val timeStamp = itemView.txt_hour_feed
        val iconLike = itemView.icon_comment_feed
        val iconComment = itemView.icon_like_feed
        val options = itemView.feed_card_overflow_button
        val readMore = itemView.read_more
        val cardView = itemView.card_view
        val comment = itemView.comment_text_layout
        val commentAuthor = itemView.comment_user
        val bottomLine = itemView.bottom_line
        val iconPlay = itemView.icon_play
    }
}
