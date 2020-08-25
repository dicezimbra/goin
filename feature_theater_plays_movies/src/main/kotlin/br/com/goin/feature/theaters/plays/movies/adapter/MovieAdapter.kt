package br.com.goin.feature.theaters.plays.movies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.goin.base.extensions.toTitleCase

import br.com.goin.feature.theaters.R
import br.com.goin.feature.theaters.plays.movies.model.PlayModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.play_layout.view.*

class MovieAdapter(private var plays: List<PlayModel>? = arrayListOf()) : androidx.recyclerview.widget.RecyclerView.Adapter<MovieAdapter.PlaysViewHolder>() {

    var onClickListener: ((model: PlayModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaysViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.play_layout, parent, false)
        return PlaysViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaysViewHolder, position: Int) {
        val playModel = plays!![position]

        Glide.with(holder.container?.context!!)
                .load(playModel.image)
                .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(16)).placeholder(R.color.gray))
                .into(holder.itemView.play_layout_image)

        holder.text.text = playModel.name
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
        val text = itemView.play_layout_text
        val subtitle = itemView.play_subtitle
    }
}