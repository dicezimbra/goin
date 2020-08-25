package br.com.goin.feature.place

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import br.com.goin.base.utils.DateUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import br.com.goin.component.model.event.Event
import br.com.goin.component.ui.kit.GlideBackgroundFromBitmapListener
import br.com.goin.feature.R
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.item_event_card_place.view.*

class PlaceEventAdapter(var events: List<Event>) : androidx.recyclerview.widget.RecyclerView.Adapter<PlaceEventAdapter.ViewHolder>() {

    var onClickEventListener: ((event: Event) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ViewHolder(inflater.inflate(R.layout.item_event_card_place, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eventModel = events[position]

        Glide.with(holder.itemView.context)
                .load(eventModel.photoUrl)
                .into(holder.cardEventImage)

        Glide.with(holder.itemView.context)
                .load(eventModel.photoUrl)
                .apply(bitmapTransform(BlurTransformation(25, 3)))
                .into(holder.background)

        holder.cardName.text = eventModel.name
        holder.txtMonth.text = DateUtils.getMonth(eventModel.startDate!!)
        holder.txtDay.text = DateUtils.getDay(eventModel.startDate!!)

        if (DateUtils.getDay(eventModel.startDate!!).length == 1) {
            holder.txtDay.text = "0${DateUtils.getDay(eventModel.startDate!!)}"
        }

        holder.itemView.setOnClickListener {
            onClickEventListener?.invoke(eventModel)
        }
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

   inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val cardEventImage = itemView.card_event_image
        val cardName = itemView.card_event_name
        val background: ImageView = itemView.iv_background
        val txtDay = itemView.card_date_day
        val txtMonth = itemView.card_date_month
    }
}
