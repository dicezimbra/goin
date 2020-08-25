package br.com.goin.feature_all_categories

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.view_categories_item.view.*
import br.com.goin.feature_all_categories.model.CategoryDetails

class CategoriesAdapter(var categories: List<CategoryDetails>) : androidx.recyclerview.widget.RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    var onClickListener: ((CategoryDetails) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.view_categories_item, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categorie = categories[position]

        Glide.with(holder.itemView.context)
                .load(categorie.imageIcon)
                .apply(RequestOptions().placeholder(R.drawable.user_icon))
                .into(holder.icon)

        holder.categorieName.text = categorie.name

        holder.itemView.setOnClickListener {
            onClickListener?.invoke(categorie)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    class ViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val categorieName = view.categorie_name
        val icon = view.icon
    }
}