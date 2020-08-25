package br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.session.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.goin.feature.theaters.R
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.model.EventSessionDetailInfo
import kotlinx.android.synthetic.main.item_time_session.view.*

class SessionComponentListTimes(private var eventSessionDetailInfos: List<EventSessionDetailInfo>): androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    var onClickListener: ((EventSessionDetailInfo) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_time_session, parent, false)

        return InTheaterSessionHolder(view)
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val item = eventSessionDetailInfos[position]
        (holder as InTheaterSessionHolder).bindItem(item)
        holder.itemView.setOnClickListener { onClickListener?.invoke(item) }
    }

    override fun getItemCount(): Int {
        return eventSessionDetailInfos.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class InTheaterSessionHolder(itemView: View) :
            androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun bindItem(item: EventSessionDetailInfo) {
            itemView.textViewTime.text = item.hour
        }
    }
}