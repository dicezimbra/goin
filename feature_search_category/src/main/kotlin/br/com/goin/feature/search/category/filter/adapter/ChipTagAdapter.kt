package br.com.goin.feature.search.category.filter.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import br.com.goin.feature.search.category.R
import br.com.goin.feature.search.category.filter.model.ChipTag
import kotlinx.android.synthetic.main.item_tag.view.*

class ChipTagAdapter(val tagList: MutableList<ChipTag>) : RecyclerView.Adapter<ChipTagAdapter.ChipTagViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipTagViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_tag, parent, false)
        return ChipTagViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return tagList.size
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ChipTagViewHolder, position: Int) {
        val tag = tagList.get(position)

        holder.textViewTag.text = tag.name

        if (tag.isSelected) {

            holder.textViewTag.setTextColor(holder.itemView.context.getColor(R.color.white))
            holder.constraintTag.background = holder.itemView.context.getDrawable(R.drawable.background_tag_filter_selected)
        } else {

            holder.textViewTag.setTextColor(holder.itemView.context.getColor(R.color.text_color_unselected_chip))
            holder.constraintTag.background = holder.itemView.context.getDrawable(R.drawable.background_tag_filter_unselected)
        }

        holder.itemView.setOnClickListener {

            tag.isSelected = !tag.isSelected
            notifyItemChanged(position)
        }
    }

    class ChipTagViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        val textViewTag = item.textViewTag
        val constraintTag = item.constraintTag
    }
}