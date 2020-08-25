package br.com.goin.feature.feed.post.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.goin.feature.feed.R
import br.com.goin.feature.feed.model.friends.PostUser
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_friends_avatar.view.*

class PostActivityAdapter(private var items: MutableList<PostUser> = arrayListOf()) :
        androidx.recyclerview.widget.RecyclerView.Adapter<PostActivityAdapter.PostActivityViewHolder>() {

    var onFriendClick: ((friendModel: PostUser) -> Unit) = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostActivityViewHolder {
        return PostActivityViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_friends_avatar, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PostActivityViewHolder, position: Int) {
        val model = items[position]

        holder.name.text = model.name
        Glide.with(holder.friend.context!!)
                .load(model.avatar)
                .apply(RequestOptions.circleCropTransform())
                .transition(withCrossFade())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_user))
                .into(holder.pic)

        holder.friend.setOnClickListener { onFriendClick(model) }
    }


    class PostActivityViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var pic = itemView.pic_friend_avatar
        var name = itemView.friend_name_avatar
        val friend = itemView.container_friend_avatar
    }


}