package br.com.goin.feature.feed.commentary.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.goin.base.BaseApplication
import br.com.goin.base.BuildConfig
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.visible
import br.com.goin.feature.feed.FeedHelper
import br.com.goin.feature.feed.R
import br.com.goin.feature.feed.commentary.model.Comments
import br.com.goin.feature.feed.model.Post
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_commentary.view.*
import kotlinx.android.synthetic.main.item_user_post.view.*
import java.text.SimpleDateFormat
import java.util.*

class CommentAdapter(var commentList: MutableList<Comments>?) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    var onOptionsClickListener: ((String?, String?) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_commentary, parent, false))
    }

    override fun getItemCount(): Int {
        return commentList?.size ?: 0
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val commentModel = commentList!![position]

        Glide.with(holder.itemView.context)
                .load(BuildConfig.BUCKET_PATH + "Users/" + commentModel.userId)
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions().placeholder(R.drawable.ic_user))
                .into(holder.pic)

        holder.user.text = commentModel.name
        holder.comment.text = commentModel.description
        if (commentModel.timeStamp != null) {
            holder.commentTime.text = FeedHelper.getTimeDifText(commentModel.timeStamp, BaseApplication.instance.context!!)

        } else {
            holder.comment.text = BaseApplication.instance.context?.getString(R.string.long_time_ago)
        }
        holder.options.setOnClickListener { onOptionsClickListener?.invoke(commentModel.postId, commentModel.commentId) }
    }


    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var user = itemView.user
        var pic = itemView.comment_user_pic
        var commentTime = itemView.comment_time
        var comment = itemView.comment_user
        var options = itemView.comment_overflow_button
    }

}