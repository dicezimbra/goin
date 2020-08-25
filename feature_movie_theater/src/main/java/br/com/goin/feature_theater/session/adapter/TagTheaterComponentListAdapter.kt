package br.com.goin.feature_theater.session.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.goin.feature_theater.R
import kotlinx.android.synthetic.main.view_theater_session_component_list_item_tag.view.*

class TagTheaterComponentListAdapter(private var tags: List<String>) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_theater_session_component_list_item_tag, parent, false)

        return InTheaterSessionHolder(view)
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val item = tags[position]
        (holder as InTheaterSessionHolder).bindItem(item)
    }

    override fun getItemCount(): Int {
        return tags.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class InTheaterSessionHolder(itemView: View) :
            androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        fun bindItem(item: String) {
            itemView.tv_tag.text = item
        }
    }
}