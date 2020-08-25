package br.com.goin.findfriends.adapter

import android.app.AlertDialog
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.goin.base.extensions.gone
import br.com.goin.component.session.user.UserModel
import br.com.goin.findfriends.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_user_action_new.view.*
import java.util.*

class FriendsInviteListAdapter(list: MutableList<UserModel>,
                               private val findFriendsListener: FindFriendsClickListener,
                               private val currentUserId: String?) : androidx.recyclerview.widget.RecyclerView.Adapter<FriendsInviteListAdapter.FriendViewHolder>() {
    var users: MutableList<UserModel>? = list
    private var usersAux: MutableList<UserModel> = ArrayList()

    init {
        this.usersAux.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_action_new, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val model = users?.get(position)
        model?.let { userModel ->
            holder.bind(userModel)

            hideInviteForYourself(holder, userModel)

            setupAvatar(holder, userModel)

            followUser(holder, userModel)
        }
    }

    private fun hideInviteForYourself(holder: FriendViewHolder, model: UserModel) {
        if (this.currentUserId != null && this.currentUserId == model.id) {
            holder.itemView.invite_button.gone()
        }
    }

    private fun setupAvatar(holder: FriendViewHolder, model: UserModel) {
        holder.itemView.user_card_avatar.context?.let { context ->
            Glide.with(context)
                    .load(model.profilePicture)
                    .apply(RequestOptions().circleCrop())
                    .apply(RequestOptions.placeholderOf(R.drawable.user_icon))
                    .into(holder.itemView.user_card_avatar)
            holder.itemView.user_card_avatar.setOnClickListener {
                findFriendsListener.onClickUser(model.id, currentUserId)
            }
        }
    }

    private fun followUser(holder: FriendViewHolder, model: UserModel) {
        holder.itemView.invite_button.setOnClickListener {
            if (model.followedByMe == true) {
                showUnfollowAlert(holder, model)
            } else {
                model.followedByMe = false
                findFriendsListener.onClickFollow(model.id)
            }
            holder.bind(model)
        }
    }

    private fun showUnfollowAlert(holder: FriendViewHolder, model: UserModel) {
        val unfollowAlert = String.format(holder.itemView.context?.resources?.getString(R.string.unfollow_alert)
                ?: "", model.name)

        AlertDialog.Builder(holder.itemView.item_user_action_container?.context)
                .setMessage(unfollowAlert)
                .setPositiveButton(R.string.unfollow) { _, _ ->
                    model.followedByMe = false
                    holder.bind(model)
                    findFriendsListener.onClickUnfollow(model.id)
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .create()
                .show()
    }

    private fun hideInviteOptions(holder: FriendViewHolder) {
        holder.itemView.user_card_name.isClickable = false
        holder.itemView.user_card_avatar.isClickable = false
        holder.itemView.invite_button.gone()
    }

    override fun getItemCount(): Int {
        return this.users?.size ?: 0
    }

    fun setUserList(newUserList: MutableList<UserModel>) {
        this.users = newUserList
        notifyDataSetChanged()
    }

    fun filter(text: String) {
        users?.run {
            this.clear()

            if (text.isEmpty()) {
                this.addAll(usersAux)
            } else {
                usersAux.forEach { userModel ->
                    if (userModel.name.toLowerCase().contains(text.toLowerCase())) {
                        this.add(userModel)
                    }
                }
            }
            notifyDataSetChanged()
        }
    }

    fun clear() {
        this.users?.clear()
        this.usersAux.clear()
    }

    class FriendViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun bind(item: UserModel) {
            itemView.user_card_name.text = item.name
            itemView.invite_button.apply {
                setBackgroundResource(if (item.setBackgroundPrimaryColor()) {
                    R.drawable.button_border_transparent_new
                } else {
                    R.drawable.button_primary_dark_new
                })

                setTextColor(if (item.setTextPrimaryColor()) {
                    ContextCompat.getColor(context, R.color.colorPrimaryDark)
                } else {
                    ContextCompat.getColor(context, R.color.lightGray)
                })

                text = item.getTextButton(context)
            }
        }
    }
}
