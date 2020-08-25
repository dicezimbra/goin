package br.com.goin.feature.profile.myinterests

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.goin.component.model.event.Event
import br.com.goin.component.model.event.api.Interrest
import br.com.goin.feature.profile.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.view_myinterests_item.view.*

class MyInterestAdapter(val interrests: List<Interrest>) : RecyclerView.Adapter<MyInterestAdapter.EventsViewHolder>() {

    var onClickListener: (Interrest) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyInterestAdapter.EventsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return EventsViewHolder(inflater.inflate(R.layout.view_myinterests_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyInterestAdapter.EventsViewHolder, position: Int) {
        val interrest = interrests[position]
        holder.bind(interrest)
    }

    override fun getItemCount(): Int {
        return interrests.size
    }

    inner class EventsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Interrest) {
            itemView.setOnClickListener { onClickListener(item) }
            itemView.card_event_name.text = item.name
            Glide.with(itemView.context!!)
                    .load(item.image)
                    .apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.placeholderOf(R.drawable.background_circle_profile))
                    .into(itemView.image)
        }
    }
}

