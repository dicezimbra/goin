package br.com.goin.features.fullticket

import br.com.goin.features.wallet.model.Ticket

interface FullTicketView {
    fun initActions(ticketModel: Ticket)
    fun configureTicket(ticketModel: Ticket)
    fun configureAnimations()
    fun configureOnClickListeners(ticket: Ticket)
    fun showSuccessDialog()
    fun cutOffTicket()
    fun showLoadingDialog()
    fun showErrorDialog()
}