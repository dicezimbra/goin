package br.com.goin.features.wallet.ticket

import androidx.constraintlayout.widget.ConstraintLayout
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import br.com.goin.feature.wallet.R

class TicketsViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

    var walletTitle: TextView
    var walletTypeDescription: TextView
    var walletDate: TextView
    var walletUrlTicket: TextView? = null
    var walletQuantityPeople: TextView
    var walletImgTicket: ImageView
    var constraintMyTickets: ConstraintLayout

    init {
        walletTitle = itemView.findViewById(R.id.titleMyTickets)
        walletTypeDescription = itemView.findViewById(R.id.subtitleMyTickets)
        walletDate = itemView.findViewById(R.id.dateMyTickets)
        walletQuantityPeople = itemView.findViewById(R.id.quantityPeopleMyTickets)
        walletImgTicket = itemView.findViewById(R.id.img_my_tickets)
        constraintMyTickets = itemView.findViewById(R.id.constraint_my_tickets)
    }
}
