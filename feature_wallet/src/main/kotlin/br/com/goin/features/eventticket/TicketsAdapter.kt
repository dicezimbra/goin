package br.com.goin.features.eventticket

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.goin.base.utils.DateUtils
import br.com.goin.feature.wallet.R
import br.com.goin.features.wallet.model.EventTicket
import kotlinx.android.synthetic.main.item_event_ticket.view.*
import java.util.*

class TicketsAdapter (val itens: List<EventTicket>): RecyclerView.Adapter<TicketsAdapter.TicketsViewHolder>() {

    var onPlusClicked: ((totalValue: String, EventTicket) -> Unit)? = null
    var onMinusClicked: ((totalValue: String, EventTicket) -> Unit)? = null


    fun getTicketSelected(): EventTicket?{
        for (item: EventTicket in itens){
            if (item.buyQuantity == 1){
                return item
            }
        }
        return null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event_ticket, parent, false)
        return TicketsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itens.size
    }

    override fun onBindViewHolder(holder: TicketsViewHolder, position: Int) {
        holder.bind(itens[position])
    }

    inner class TicketsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var ticketInfo = itemView.ticketInfo
        var ticketDate = itemView.ticketdate
        var ticketValue = itemView.ticketValue
        var plus = itemView.plus
        var minus = itemView.minus
        var quantity = itemView.quantity

        fun bind(ticket: EventTicket) {


            if(ticket.isPlusActive){

                plus.setImageResource(R.drawable.plusplus)
                plus.setOnClickListener{
                    onPlusClicked?.invoke(String.format(Locale.getDefault(), "R$ %.2f", ticket.price), ticket)
                }

                minus.setOnClickListener{

                }
            }else {

                plus.setImageResource(R.drawable.icon_plusplus_gray)
                plus.setOnClickListener{
                }

                minus.setOnClickListener{
                    if(ticket.buyQuantity == 0) return@setOnClickListener
                    onMinusClicked?.invoke(String.format(Locale.getDefault(), "R$ %.2f", 0.00), ticket)
                }
            }

            if(ticket.isMinusActive) {
                minus.setImageResource(R.drawable.minusminus)
            }else{
                minus.setImageResource(R.drawable.minusminus_disabled)
            }

            quantity.text = "${ticket.buyQuantity}"
            ticketInfo.text = ticket.name
            ticketDate.text = DateUtils.convertCalendarToDateString(ticket.eventDate)
            ticketValue.text = String.format(Locale.getDefault(), "R$ %.2f", ticket.price)


        }
    }
}