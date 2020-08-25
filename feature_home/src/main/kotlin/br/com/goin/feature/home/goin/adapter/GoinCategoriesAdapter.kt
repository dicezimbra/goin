package br.com.goin.feature.home.goin.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.goin.component.model.category.Category
import br.com.goin.feature.home.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.component_selectable_circle.view.*

class GoinCategoriesAdapter(var components: List<Category>, var itemWidth: Int) : androidx.recyclerview.widget.RecyclerView.Adapter<GoinCategoriesAdapter.ViewHolder>() {

    var onClickListener: ((categories: Category) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.component_selectable_circle, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val component = components[position]
        holder.textValue?.text = component.name

        Glide.with(holder.itemView.context)
                .load(component.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.fullIcon)

        holder.itemView.setOnClickListener {
            onClickListener?.invoke(component)
        }

        holder.fullLayout.layoutParams.width = itemWidth
        holder.fullLayout.requestLayout()
    }

    override fun getItemCount(): Int {
        return components.size
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val fullIcon = itemView.full_icon
        val textValue = itemView.textComponentSelectable
        val fullLayout = itemView.selectable_circle_container
    }
}
