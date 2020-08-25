package br.com.goin.eventticket

import br.com.goin.base.model.PaymentModel
import br.com.goin.features.wallet.model.EventTicket

interface TicketPresenter {
    fun onReceiveEventId(stringExtra: String?)
    fun onReceiveIngresseToken(stringExtra: String?)
    fun onReceiveOriginType(stringExtra: String?)
    fun onReceiveCoupon(stringExtra: String?)
    fun onCreate()
    fun onDestroy()
    fun getIngresseToken(): String
    fun fillPaymentModel()
    fun fillTicketModel(ticket: EventTicket)
    fun getPaymentModel(): PaymentModel
}