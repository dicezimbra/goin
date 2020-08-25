package br.com.goin.features.wallet.ticket

import br.com.goin.features.wallet.model.Ticket
import br.com.goin.features.wallet.model.TicketModel
import br.com.goin.model.ticket.TicketDeleteResponse

interface TicketsView {
    fun configureEmptyState()
    fun onTicketLoaded(tickets: List<Ticket>)
    fun showLoading()
    fun hideLoading()
    fun configureRecyclerView()
    fun handleError(throwable: Throwable)
}