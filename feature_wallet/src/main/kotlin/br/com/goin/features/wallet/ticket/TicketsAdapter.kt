package br.com.goin.features.wallet.ticket

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import br.com.goin.base.extensions.visible
import br.com.goin.feature.wallet.R
import br.com.goin.features.wallet.model.Ticket
import br.com.goin.features.wallet.model.Validate
import br.com.goin.features.wallet.model.Validate.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import jp.wasabeef.glide.transformations.GrayscaleTransformation
import kotlinx.android.synthetic.main.item_my_tickets.view.*

class TicketsAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<TicketsViewHolder>() {

    var tickets: List<Ticket> = arrayListOf()
    var onClickListener: ((Ticket) -> Unit) = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketsViewHolder {
        return TicketsViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_my_tickets, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TicketsViewHolder, position: Int) {
        val ticket = tickets[position]
        val detail = ticket.detail
        holder.itemView.titleMyTickets.text = ticket.title

        when(ticket.validate){
            VALID -> {
                holder.itemView.alpha = 1f
                holder.itemView.subtitleMyTickets.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.grapefruit))
                Glide.with(holder.itemView.context)
                        .load(ticket.image)
                        .into(holder.walletImgTicket)
            }
            else -> {
                holder.itemView.alpha = 0.5f
                holder.itemView.subtitleMyTickets.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.slate_grey))
                Glide.with(holder.itemView.context)
                        .load(ticket.image)
                        .apply(bitmapTransform(GrayscaleTransformation()))
                        .into(holder.walletImgTicket)
            }
        }

        ticket.placeName?.let {
            holder.itemView.subtitleMyTickets.visible()
            holder.itemView.subtitleMyTickets.text = ticket.placeName
        }

        detail?.let{
            holder.itemView.dateMyTickets.text = ticket.dateText
            holder.walletDate.text = ticket.dateText
        }

        holder.constraintMyTickets.setOnClickListener { onClickListener(ticket) }
    }

    override fun getItemCount(): Int {
        return tickets.size
    }
}
