package br.com.goin.feature.theaters.plays.movies.theatherPlays.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import br.com.goin.feature.theaters.R
import br.com.goin.base.extensions.toTitleCase
import br.com.goin.feature.theaters.plays.movies.model.PlayModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.view_theather_landscape_item.view.*

class TheaterPlaysAdapter(private var plays: List<PlayModel>? = arrayListOf()) : androidx.recyclerview.widget.RecyclerView.Adapter<TheaterPlaysAdapter.PlaysViewHolder>() {

    var onClickListener: ((model: PlayModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaysViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_theather_landscape_item, parent, false)
        return PlaysViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaysViewHolder, position: Int) {
        val playModel = plays!![position]

        Glide.with(holder.container?.context!!)
                .load(playModel.image)
                .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(16)).placeholder(R.drawable.background_placeholder_landscape_mini))
                .into(holder.itemView.play_layout_image)

        holder.title.text = playModel.name?.toTitleCase()
        holder.subtitle.text = playModel.city?.toTitleCase()
        holder.itemView.setOnClickListener { onClickListener?.invoke(playModel) }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return this.plays!!.size
    }

    fun setList(playsList: List<PlayModel>) {
        plays = playsList
        notifyDataSetChanged()
    }

    class PlaysViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val container = itemView.play_layout_container
        val title = itemView.play_title
        val subtitle = itemView.play_subtitle
    }
}