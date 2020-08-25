package br.com.goin.feature.rating

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.goin.component_model_club.model.ClubRatingCardModel
import br.com.goin.feature.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_rating.*
import kotlinx.android.synthetic.main.view_rating_item.view.*

class RatingAdapter(var ratings: List<ClubRatingCardModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<RatingAdapter.ViewHolder>() {

    var onClickListener: ((ClubRatingCardModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.view_rating_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categorie = ratings[position]

        holder.itemView.setOnClickListener {
            onClickListener?.invoke(categorie)
        }

        holder.onBindViewHolder(categorie)
    }

    override fun getItemCount(): Int {
        return ratings.size
    }

    class ViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        fun onBindViewHolder(item: ClubRatingCardModel) {

            txtRatingUser.text = item.userName
            item.rating?.let {   ratingBar.rating = it }

            if(!item.comment?.trim().isNullOrEmpty()) {
                ratingComment.text = item.comment
                ratingComment.visibility = View.VISIBLE
            }
            else {
                ratingComment.visibility = View.GONE
            }

            Glide.with(view.context)
                    .load(item?.avatar)
                    .apply(RequestOptions.circleCropTransform().placeholder(R.drawable.user_icon_rating))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(userImage)
        }

        val userImage = view.userImgRating
        val txtRatingUser = view.txtRatingUser
        val ratingBar = view.rating_bar
        val ratingComment = view.rating_card_comment
    }
}