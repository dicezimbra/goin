package br.com.legacy.viewControllers.activitites

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_subcategories.view.*
import androidx.core.content.ContextCompat
import br.com.R
import br.com.goin.component.model.category.SubcategoriesModel

class SubCategoriesAdapter(var subCategories: List<SubcategoriesModel>,
                           internal val onClickListener: OnClickListener,
                           var subcategoryId: String = "")
    : RecyclerView.Adapter<SubCategoriesAdapter.ViewHolder>() {

    companion object {

        var selectedPos = RecyclerView.NO_POSITION
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_subcategories, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subCategory = subCategories[position]

        Glide.with(holder.itemView.context)
                .load(subCategory.urlImage)
                .apply(RequestOptions().placeholder(R.drawable.user_icon))
                .into(holder.imageViewSubcategories)

        holder.subCategoryName.text = subCategory.name

        holder.itemView.isSelected = (selectedPos == position)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (holder.itemView.isSelected || subCategory.subcategoryId.equals(subcategoryId)) {

                holder.viewSelectedSubcategory.visibility = View.VISIBLE
                holder.subCategoryName.setTextColor(holder.itemView.context.getColor(R.color.melon))
            } else {

                holder.viewSelectedSubcategory.visibility = View.INVISIBLE
                holder.subCategoryName.setTextColor(holder.itemView.context.getColor(R.color.battleship_gray))
            }
        } else {

            if (holder.itemView.isSelected || subCategory.subcategoryId.equals(subcategoryId)) {

                holder.viewSelectedSubcategory.visibility = View.VISIBLE
                holder.subCategoryName.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.melon))
            } else {

                holder.viewSelectedSubcategory.visibility = View.INVISIBLE
                holder.subCategoryName.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.battleship_gray))
            }
        }

        holder.itemView.setOnClickListener {

            onClickListener.onClickSubcategory(subCategory)

            notifyItemChanged(selectedPos)
            selectedPos = position
            notifyItemChanged(selectedPos)
        }
    }

    override fun getItemCount(): Int {
        return subCategories.size
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val subCategoryName = view.textViewSubCategory
        val imageViewSubcategories = view.imageViewSubcategories
        val viewSelectedSubcategory = view.viewSelectedSubcategory
    }

    interface OnClickListener {

        fun onClickSubcategory(subCategories: SubcategoriesModel)
    }
}