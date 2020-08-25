package br.com.goin.feature.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.visible
import br.com.goin.component.session.user.UserHelper
import br.com.goin.feature.notifications.model.Notification
import br.com.goin.feature.notifications.model.NotificationType
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_notification_view.view.*

class NotificationsAdapter : RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {

    val notifications: MutableList<Notification> = arrayListOf()

    var onFollowClicked: ((Notification) -> Unit)? = null
    var onAvatarClicked: ((Notification) -> Unit)? = null
    var onNotificationClicked: ((Notification) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification_view, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        val context = holder.itemView.context

        val following = notification.followedByMe ?: false
        if(following){
            holder.followButton.text = context.getString(R.string.following)
        }else{
            holder.followButton.text = context.getString(R.string.follow)
        }

        if(notification.type != NotificationType.follow){
            holder.followButton.gone()
        }else{
            holder.followButton.visible()
        }

        holder.followButton.isSelected = notification.followedByMe ?: false
        holder.followButton.setOnClickListener { onFollowClicked?.invoke(notification) }

        holder.container.setOnClickListener { onNotificationClicked?.invoke(notification) }

        holder.description.text = NotificationHelper.getNotificationText(context, notification)
        holder.avatar.setOnClickListener { onAvatarClicked?.invoke(notification) }

        notification.avatar?.let {
            Glide.with(holder.itemView.context)
                    .load(notification.avatar)
                    .apply(RequestOptions.circleCropTransform().signature(UserHelper.getSignature()))
                    .transition(withCrossFade())
                    .into(holder.avatar)
        }
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    fun setNotifications(notifications: List<Notification>){
        this.notifications.clear()
        this.notifications.addAll(notifications)
        notifyDataSetChanged()
    }

    class NotificationViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val avatar = view.notification_avatar
        val description = view.notification_text
        val followButton = view.notification_follow_button
        val container = view.notification_container
    }
}
