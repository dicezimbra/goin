package br.com.goin.feature.search.category.filter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.goin.feature.search.category.R
import br.com.goin.feature.search.category.model.SubcategoriesModel
import kotlinx.android.synthetic.main.item_subcategory_filter_party.view.*

class SubCategoriesFilterPartyAdapter(var subCategories: List<SubcategoriesModel>) : RecyclerView.Adapter<SubCategoriesFilterPartyAdapter.ViewHolder>() {

    var onClickListener: (SubcategoriesModel?) -> Unit = {}
    var count = Math.min(8 ,subCategories.size)


    var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_subcategory_filter_party, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return count
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun showAllItens(){
        if(count < subCategories.size){
            count = subCategories.size
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subcategory = subCategories[position]
        val context = holder.itemView.context!!
        
        if(count < subCategories.size && position == count - 1){
            holder.itemView.checkbox.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            holder.itemView.checkbox.text = context.getString(R.string.more_options)
            holder.itemView.checkbox.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))

        }else {
            holder.itemView.checkbox.isSelected = (selectedPosition == position)
            holder.itemView.checkbox.background = ContextCompat.getDrawable(context, R.drawable.background_subcategories_filter)
            holder.itemView.checkbox.setTextColor(ContextCompat.getColorStateList(context, R.color.color_subcategories_filter))
            holder.itemView.checkbox.text = subcategory.name
        }

        holder.itemView.checkbox.setOnClickListener {
            if(count < subCategories.size && position == count - 1){
                showAllItens()
            }else {
                if(selectedPosition == position){
                    selectedPosition = -1
                    onClickListener(null)
                }else{
                    selectedPosition = position
                    onClickListener(subcategory)
                }

                notifyDataSetChanged()
            }
        }
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item)
}