package br.com.goin.feature.home.goin.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.visible
import br.com.goin.base.utils.DateUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import br.com.goin.base.utils.GpsUtils
import br.com.goin.base.utils.Utils
import br.com.goin.component.model.event.EventHome
import br.com.goin.component.model.event.OriginType
import br.com.goin.component.session.user.location.UserLocation
import br.com.goin.component.ui.kit.GlideBackgroundFromBitmapListener
import br.com.goin.feature.home.R
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.item_event_card_home.view.*


class GoinSugestionAdapter(var events: List<EventHome>, var location: UserLocation) : androidx.recyclerview.widget.RecyclerView.Adapter<GoinSugestionAdapter.ViewHolder>() {

    var onClickEventListener: ((event: EventHome) -> Unit)? = null
    var onClickShareListener: ((event: EventHome) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoinSugestionAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return GoinSugestionAdapter.ViewHolder(inflater.inflate(R.layout.item_event_card_home, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: GoinSugestionAdapter.ViewHolder, position: Int) {
        val eventModel = events[position]

        Glide.with(holder.itemView.context)
                .load(eventModel.image)
                .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .listener(GlideBackgroundFromBitmapListener(holder.background))
                .into(holder.cardEventImage)

        holder.cardName.text = eventModel.title
        holder.placeAddress.text = eventModel.subtitle

        holder.txtMonth.text = DateUtils.getMonth(eventModel.date!!)
        holder.txtDay.text = DateUtils.getDay(eventModel.date!!)

        if (eventModel.lat != null && eventModel.long != null) {
            val distance = GpsUtils.calculateDistance(location.lat, location.lng, eventModel.lat!!, eventModel.long!!)
            holder.textViewDistance.text = "${Utils.formatKm(distance)} km"
        }

        if (DateUtils.getDay(eventModel.date!!).length == 1) {
            holder.txtDay.text = "0${DateUtils.getDay(eventModel.date!!)}"
        }

        if (eventModel.partnersInfo?.logo != null && !eventModel.partnersInfo?.bigIcon!!.isEmpty()) {
            Glide.with(holder.itemView.context)
                    .load(eventModel.partnersInfo?.bigIcon)
                    .transition(withCrossFade())
                    .into(holder.partnersButton)
        } else {
            when {
                eventModel.originType == OriginType.ALOINGRESSOS.type -> {
                    holder.partnersButton.setImageResource(R.drawable.logo_alo_ingresso_preto)
                }
                eventModel.originType == OriginType.GOIN.type -> {
                    holder.partnersButton.setImageResource(R.drawable.logo_go_in)
                }
                eventModel.originType == OriginType.INGRESSE.type -> {
                    holder.partnersButton.setImageResource(R.drawable.logo_horizontal_escuro_ingresse)
                }
                eventModel.originType == OriginType.INGRESSORAPIDO.type -> {
                    holder.partnersButton.setImageResource(R.drawable.ingressorapido_icon)
                }
                eventModel.originType == OriginType.EVENTBRITE.type -> {
                    holder.partnersButton.setImageResource(R.drawable.eventbrite_icon)
                }
                else -> holder.partnersButton.visibility = View.INVISIBLE
            }
        }

        eventModel.getDistance(location = Pair(location.lat, location.lng)).let {
            if (it.isNullOrEmpty().not()) {
                holder.textViewDistance.text = it
                holder.textViewDistance.visible()
            } else {
                holder.textViewDistance.gone()
            }
        }

        holder.card.setOnClickListener { onClickEventListener?.invoke(eventModel) }
        holder.share.setOnClickListener { onClickShareListener?.invoke(eventModel) }
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onViewRecycled(holder: GoinSugestionAdapter.ViewHolder) {
        super.onViewRecycled(holder)
        val disposable = holder.textViewDistance.tag as? Disposable
        disposable?.dispose()
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val cardEventImage = itemView.card_event_image
        val share = itemView.image_view_share
        val placeAddress = itemView.card_event_location
        val eventName = itemView.card_event_name
        val cardName = itemView.card_event_name
        val background: ImageView = itemView.iv_background
        val partnersButton: ImageView = itemView.partners_button
        val txtDay = itemView.txt_day
        val txtMonth = itemView.txt_month
        val textViewDistance = itemView.textViewDistance
        val card = itemView.card_view_event
    }
}
