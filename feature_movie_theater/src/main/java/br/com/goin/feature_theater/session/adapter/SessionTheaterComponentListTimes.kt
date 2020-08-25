package br.com.goin.feature_theater.session.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.goin.feature_theater.R
import br.com.goin.feature_theater.model.Info
import kotlinx.android.synthetic.main.item_theater_time_session.view.*

class SessionTheaterComponentListTimes(private var eventSessionDetailInfos: List<Info>): androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    var onClickListener: ((Info) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_theater_time_session, parent, false)

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

        fun bindItem(item: Info) {
            itemView.textViewTime.text = item.hour
        }
    }
}