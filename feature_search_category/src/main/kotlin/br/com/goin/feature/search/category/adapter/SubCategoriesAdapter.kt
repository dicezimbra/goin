package br.com.goin.feature.search.category.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.goin.feature.search.category.R
import br.com.goin.feature.search.category.model.SubcategoriesModel
import kotlinx.android.synthetic.main.item_search_by_category_subcategory.view.*

class SubCategoriesAdapter(var subCategories: List<SubcategoriesModel>,
                           var subcategoryId: String? = "")
    : RecyclerView.Adapter<SubCategoriesAdapter.ViewHolder>() {

    var onClickSubCategories: (SubcategoriesModel?) -> Unit = {}
    var selectedPos = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_search_by_category_subcategory, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subCategory = subCategories[position]

        holder.subCategoryName.text = subCategory.name
        holder.itemView.isSelected = (selectedPos == position)

        holder.itemView.setOnClickListener {

            if(position == selectedPos){
                selectedPos = -1
                onClickSubCategories(null)
            }else{
                selectedPos = position
                onClickSubCategories(subCategory)
            }

            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return subCategories.size
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val subCategoryName = view.textViewSubCategory
    }
}