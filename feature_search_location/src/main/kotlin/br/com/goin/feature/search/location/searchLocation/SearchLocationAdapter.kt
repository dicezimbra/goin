package br.com.goin.feature.search.location.searchLocation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.goin.feature.search.location.R
import br.com.goin.feature.search.location.searchLocation.model.CityLocation
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.view_search_location_item.view.*

class SearchLocationAdapter: RecyclerView.Adapter<SearchLocationAdapter.ViewHolder>() {

    private val cities: ArrayList<CityLocation> = arrayListOf()
    var onClickListener: ((CityLocation) -> Unit)? = null

    fun configureCities(cities: List<CityLocation>){
        this.cities.clear()
        this.cities.addAll(cities)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.view_search_location_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = cities.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = cities[position]
        val context = holder.itemView.context

        holder.title.text = city.title
        city.description?.let {
            holder.description.text = it
            holder.description.visibility = View.VISIBLE
        }

        Glide.with(context)
                .load(city.iconRes)
                .into(holder.icon)

        holder.itemView.setOnClickListener { onClickListener?.invoke(city) }

        when(city.highlight){
            true -> {
                holder.title.setTextColor(ContextCompat.getColor(context, R.color.style_orange))
                holder.description.setTextColor(ContextCompat.getColor(context, R.color.style_orange))
            }
            false -> {
                holder.title.setTextColor(ContextCompat.getColor(context, R.color.battleship_gray))
                holder.description.setTextColor(ContextCompat.getColor(context, R.color.battleship_gray))
            }
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        val context = holder.itemView.context

        holder.description.text = ""
        holder.description.visibility = View.GONE
        Glide.with(context).clear(holder.icon)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val title = itemView.title
        val description = itemView.description
        val icon = itemView.icon
    }

    class MyDiffCallback(var old: List<CityLocation>,
                         var new: List<CityLocation>): DiffUtil.Callback(){

        override fun getOldListSize(): Int {
            return old.size
        }

        override fun getNewListSize(): Int {
            return new.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return old[oldItemPosition].title == old[newItemPosition].title
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return old[oldItemPosition].title == new[newItemPosition].title
        }
    }
}