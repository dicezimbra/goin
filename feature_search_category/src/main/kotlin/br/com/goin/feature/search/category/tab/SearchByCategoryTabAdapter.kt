package br.com.goin.feature.search.category.tab

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.goin.base.extensions.dpToPx
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.visible
import br.com.goin.base.utils.DateUtils
import br.com.goin.feature.search.category.R
import br.com.goin.feature.search.category.model.SearchFilterPageItem
import br.com.goin.feature.search.category.model.Type
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.view_search_category_item_grid.view.*
import kotlinx.android.synthetic.main.view_search_category_item_header.view.*

class SearchByCategoryTabAdapter(var itens: MutableList<SearchFilterPageItem>,
                                 var type: VIEW_MODE = VIEW_MODE.LIST,
                                 val location: Pair<Double, Double>? = null) : RecyclerView.Adapter<SearchByCategoryTabAdapter.ViewHolder>() {

    enum class VIEW_MODE { LIST, GRID }
    private enum class LAYOUT_TYPE { HEADER, LIST_EVENT, LIST_PLACE, GRID }

    var onClickListener: (SearchFilterPageItem) -> Unit = {}
    var onClickChangeViewType: () -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchByCategoryTabAdapter.ViewHolder {
         val layout = when (LAYOUT_TYPE.values()[viewType]) {
            LAYOUT_TYPE.LIST_EVENT -> R.layout.view_search_category_item
            LAYOUT_TYPE.LIST_PLACE -> R.layout.view_search_category_place_item
            LAYOUT_TYPE.HEADER -> R.layout.view_search_category_item_header
            LAYOUT_TYPE.GRID -> R.layout.view_search_category_item_grid
        }

        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchByCategoryTabAdapter.ViewHolder, position: Int) {
        if (position == 0) {
            val context = holder.itemView.context
            if (type == VIEW_MODE.GRID) {
                holder.itemView.change_view_format_button?.background = ContextCompat.getDrawable(context, R.drawable.icon_list_view_2x)
            } else {
                holder.itemView.change_view_format_button?.background = ContextCompat.getDrawable(context, R.drawable.icon_grid_view_3x)
            }
            holder.itemView.change_view_format_button?.setOnClickListener {
                onClickChangeViewType()
            }
        } else {
            val item = itens[position - 1]
            holder.onBindViewHolder(item)
        }
    }

    override fun getItemCount(): Int {
        return itens.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        if(position == 0) return LAYOUT_TYPE.HEADER.ordinal
        return when(type){
            VIEW_MODE.LIST -> {
                if(itens.size > position - 1 && itens[position - 1].type == Type.EVENT) {
                    LAYOUT_TYPE.LIST_EVENT.ordinal
                } else {
                    LAYOUT_TYPE.LIST_PLACE.ordinal
                }
            }
            VIEW_MODE.GRID ->  LAYOUT_TYPE.GRID.ordinal
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        val disposable = holder.distance?.tag as? Disposable
        disposable?.dispose()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.image
        val name = itemView.name
        val address = itemView.address
        val distance = itemView.distance
        val upperTitle = itemView.upper_title
        val logo = itemView.logo
        val logoBackground = itemView.background_logo

        fun onBindViewHolder(item: SearchFilterPageItem) {
            name.text = item.name

            val addressS = StringBuilder()
            if (!item.address.isNullOrBlank()) addressS.append(item.address)

            item.city?.let {
                if (!addressS.contains("$it"))
                    addressS.append(" - $it")
            }

            item.state?.let {
                if (!addressS.contains(" $it "))
                    addressS.append(" / $it")
            }

            address.text = addressS.toString()
            itemView.setOnClickListener { onClickListener(item) }

            this.distance.visible()
            distance.text = item.getFormattedDistance(location)

            if (item.type == Type.EVENT) {
                configureEvent(item)
            } else {
                configurePlace(item, itemView.context)
            }
        }

        private fun configureEvent(item: SearchFilterPageItem) {
            Glide.with(itemView.context)
                    .load(item.image)
                    .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(7.dpToPx()))
                            .placeholder(R.drawable.background_category_search))
                    .into(image)

            item.startDate?.let {
                upperTitle.visible()
                upperTitle.text = "${DateUtils.getDay(it)} de ${DateUtils.getFullMonth(it)}"
            } ?: upperTitle.gone()
        }

        private fun configurePlace(item: SearchFilterPageItem, context: Context) {
            val imageBackgroundUrl = if(type == VIEW_MODE.GRID) item.image else item.logo

            Glide.with(itemView.context)
                    .load(imageBackgroundUrl)
                    .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(7.dpToPx()))
                            .placeholder(R.drawable.background_category_search))
                    .into(image)

            if (logo != null && logoBackground != null) {
                Glide.with(itemView.context)
                        .load(item.logo)
                        .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(7.dpToPx()))
                                .placeholder(R.drawable.background_category_search))
                        .into(logo)
                logo.visible()
                logoBackground.visible()
            }

            if (item.rating == 0.0f) {
                upperTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.start_drawable_size_grey, 0, 0, 0)
                upperTitle.text = ""
            } else {
                upperTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.start_drawable_size, 0, 0, 0)
                val ratingClub = String.format("%.1f", item.rating)
                upperTitle.setTextColor(ContextCompat.getColor(context, R.color.gold))
                upperTitle.text = ratingClub
            }
            upperTitle.visible()
        }
    }
}
