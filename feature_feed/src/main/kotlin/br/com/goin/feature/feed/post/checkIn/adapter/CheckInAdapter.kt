package br.com.goin.feature.feed.post.checkIn.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.goin.feature.feed.R
import br.com.goin.feature.feed.model.checkIn.CheckIn

class CheckInAdapter(
        private var eventList: List<CheckIn> = arrayListOf()) : RecyclerView.Adapter<CheckInAdapter.CheckInViewHolder>() {

    var onEvenClick: ((checkInModel: CheckIn) -> Unit) = {}
    private var filteredEvents: List<CheckIn> = ArrayList(eventList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckInViewHolder {
        return CheckInViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_checkin_event, parent, false))
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: CheckInViewHolder, position: Int) {
        val checkInModel: CheckIn = eventList[position]
        holder.placeName.text = checkInModel.name

        holder.placeName.setOnClickListener {
            onEvenClick(checkInModel)
        }
    }

    class CheckInViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeName: TextView = itemView.findViewById(R.id.item_check_in_event_name)
    }

    private fun updateList(list: List<CheckIn>) {
        eventList = list
        notifyDataSetChanged()
    }

    fun filter(text: String) {
        val temp: ArrayList<CheckIn> = arrayListOf()
        val tempText = text.toLowerCase()

        for (event: CheckIn in filteredEvents) {
            if (event.name != null && event.name!!.toLowerCase().contains(tempText)
                    || event.club != null && event.club!!.toLowerCase().contains(tempText)) {
                temp.add(event)
            }
        }
        updateList(temp)
    }

}