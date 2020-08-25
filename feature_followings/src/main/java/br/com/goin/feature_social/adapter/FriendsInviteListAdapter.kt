package br.com.goin.feature_social.adapter

import android.app.AlertDialog
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.visible
import br.com.goin.base.model.PurchaseModel
import br.com.goin.component.navigation.LegacyNavigationController
import br.com.goin.component.navigation.NavigationController
import br.com.goin.component.session.user.ActionType
import br.com.goin.component.session.user.FriendStatus
import br.com.goin.component.session.user.UserModel
import br.com.goin.feature_social.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_user_action_new.view.*
import java.util.*

class FriendsInviteListAdapter(list: MutableList<UserModel>,
                               var actionType: ActionType,
                               val userId: String?) : androidx.recyclerview.widget.RecyclerView.Adapter<FriendsInviteListAdapter.FriendViewHolder>() {
    var users: MutableList<UserModel>? = list
    private var usersAux: MutableList<UserModel> = ArrayList()
    var eventId: String? = null
    var isGroupConversation: Boolean? = null
    var purchaseModel: PurchaseModel? = null
    private val navigationController = NavigationController.instance

    var inviteFriendToEvent: ((eventId: String, invitedId: String) -> Unit)? = null
    var follow: (() -> Unit)? = null
    var followUser: ((userId: String) -> Unit)? = null
    var unfollowUser: ((userId: String) -> Unit)? = null

    init {
        this.usersAux.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsInviteListAdapter.FriendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_action_new, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendsInviteListAdapter.FriendViewHolder, position: Int) {
        val model = users?.get(position)
        model?.let { userModel ->
            holder.bind(userModel)

            hideInviteForYourself(holder, userModel)

            setupAvatar(holder, userModel)

            when (actionType) {
                ActionType.Invite -> {
                    inviteFriend(holder, userModel)
                }

                ActionType.Follow -> {
                    followUser(holder, userModel)
                }

                ActionType.Conversation -> {
                    setupConversation(holder, userModel)
                    configureUnreadMessages(holder, userModel)
                }

                ActionType.StartConversation -> {
                    setupConversation(holder, userModel)
                }

                ActionType.TransferTicket -> {
                    setupTicketTransfer(holder, userModel)
                }

                else -> {
                }
            }
        }
    }

    private fun hideInviteForYourself(holder: FriendsInviteListAdapter.FriendViewHolder, model: UserModel) {
        if (this.userId != null && this.userId == model.id) {
            holder.itemView.invite_button.gone()
        }
    }

    private fun setupAvatar(holder: FriendsInviteListAdapter.FriendViewHolder, model: UserModel) {
        holder.itemView.user_card_avatar.context?.let { context ->
            Glide.with(context)
                    .load(model.profilePicture)
                    .apply(RequestOptions().circleCrop())
                    .apply(RequestOptions.placeholderOf(R.drawable.user_icon))
                    .into(holder.itemView.user_card_avatar)

            holder.itemView.user_card_avatar.setOnClickListener {
                navigationController?.profileModule()?.goToProfile(context, model.id)
            }
        }
    }

    private fun inviteFriend(holder: FriendsInviteListAdapter.FriendViewHolder, model: UserModel) {
        if (model.friendStatus != FriendStatus.Invited) {
            holder.itemView.invite_button.setOnClickListener {
                model.friendStatus = FriendStatus.Invited
                holder.bind(model)
                eventId?.let {
                    inviteFriendToEvent?.invoke(it, model.id)
                }
                holder.itemView.invite_button.setOnClickListener(null)
            }
        }
    }

    private fun followUser(holder: FriendsInviteListAdapter.FriendViewHolder, model: UserModel) {
        holder.itemView.invite_button.setOnClickListener {
            if (model.followedByMe == true) {
                showUnfollowAlert(holder, model)
            } else {
                follow?.invoke()
                model.followedByMe = false
                followUser?.invoke(model.id)
            }
            holder.bind(model)
        }
    }

    private fun showUnfollowAlert(holder: FriendsInviteListAdapter.FriendViewHolder, model: UserModel) {
        val unfollowAlert = String.format(holder.itemView.context?.resources?.getString(R.string.unfollow_alert)
                ?: "", model.name)

        AlertDialog.Builder(holder.itemView.item_user_action_container?.context)
                .setMessage(unfollowAlert)
                .setPositiveButton(R.string.unfollow) { _, _ ->
                    model.followedByMe = false
                    holder.bind(model)
                    unfollowUser?.invoke(model.id)
                }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .create()
                .show()
    }

    private fun hideInviteOptions(holder: FriendsInviteListAdapter.FriendViewHolder) {
        holder.itemView.user_card_name.isClickable = false
        holder.itemView.user_card_avatar.isClickable = false
        holder.itemView.invite_button.gone()
    }

    private fun setupConversation(holder: FriendsInviteListAdapter.FriendViewHolder, model: UserModel) {
        hideInviteOptions(holder)
    }

    private fun configureUnreadMessages(holder: FriendViewHolder, model: UserModel) {
        holder.itemView.chat_unread_messages.apply {
            visible()

            val unreadMessages = (model.unreadMessages ?: 0).toString()
            model.unreadMessages?.let {
                if (it > 0) {
                    text = unreadMessages
                }
            }
        }
    }

    private fun setupTicketTransfer(holder: FriendViewHolder, model: UserModel) {
        hideInviteOptions(holder)
        holder.itemView.setOnClickListener {
            val info = PurchaseModel().TicketsInfo().apply {
                userId = model.id
                name = model.name
            }
            navigationController?.legacyApp()?.goToTransferTicket(holder.itemView.item_user_action_container?.context, purchaseModel, info, model.profilePicture)
        }
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
