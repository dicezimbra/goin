package br.com.goin.friends

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.goin.component.session.user.FriendStatus
import br.com.goin.component.session.user.UserModel
import br.com.goin.friends.model.FriendToInvite
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.view_find_friend_item.view.*

class InviteFriendAdapter(var friends: MutableList<FriendToInvite> = mutableListOf())
    : RecyclerView.Adapter<InviteFriendAdapter.FriendViewHolder>(), Filterable {


    private var filterFriends = friends
    var onClickAvatar: (FriendToInvite) -> Unit = {}
    var onClickInvite: (FriendToInvite) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_find_friend_item, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = filterFriends[position]
        val context = holder.itemView.context
        holder.bind(friend)
        setupAvatar(holder, friend, context)
        inviteFriend(holder, friend)
    }

    private fun setupAvatar(holder: FriendViewHolder, model: FriendToInvite, context: Context) {
        Glide.with(context)
                .load(model.avatar)
                .apply(RequestOptions().circleCrop())
                .apply(RequestOptions.placeholderOf(R.drawable.background_circle_placeholder_invite_friends))
                .into(holder.itemView.user_card_avatar)

        holder.itemView.user_card_avatar.setOnClickListener {
            onClickAvatar(model)
        }
    }

    private fun inviteFriend(holder: FriendViewHolder, model: FriendToInvite) {
        if (model.invitedByMe == false) {
            holder.itemView.invite_button.setOnClickListener {
                onClickInvite(model)
            }
        }
    }

    override fun getItemCount(): Int {
        return this.filterFriends.size
    }

    class FriendViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        fun bind(item: FriendToInvite) {

            itemView.user_card_name.text = item.name
            itemView.invite_button.apply {
                if (item.invitedByMe == true) {
                    setBackgroundResource(R.drawable.button_border_transparent_new)
                    setTextColor(ContextCompat.getColor(context, R.color.gray))
                    text = context.getString(R.string.invited_button)

                } else {
                    setBackgroundResource(R.drawable.button_primary_dark_new)
                    setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                    text = context.getString(R.string.invite_button)
                }
            }
        }
    }

    override fun getFilter(): Filter {
        return object: Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var filter: List<FriendToInvite> = friends
                constraint?.let{ query ->
                    if(query.isNotEmpty()) {
                        filter = filterFriends
                                .filter { !it.name.isNullOrBlank() }
                                .filter { it.name!!.toLowerCase().contains(query.toString().toLowerCase()) }
                    }
                }

                val result = FilterResults()
                result.count = filter.size
                result.values = filter
                return result
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterFriends = ArrayList(results?.values as? List<FriendToInvite>)
                notifyDataSetChanged()
            }
        }
    }
}
