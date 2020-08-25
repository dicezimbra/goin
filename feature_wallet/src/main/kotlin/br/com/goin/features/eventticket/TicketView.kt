package br.com.goin.eventticket

import br.com.goin.component.model.event.Event
import br.com.goin.features.wallet.model.EventTicket

interface TicketView {
    fun showLoading()
    fun hideLoading()
    fun handleError(t: Throwable?)
    fun configureTickets(it: List<EventTicket>?)

    fun eventLoaded(it: Event?)
    fun configureRecyclerView()
    fun configurePlusClicks()
    fun configureMinusClicks()
    fun loadToolbar(event: Event?)
    fun configureToolbar()
}