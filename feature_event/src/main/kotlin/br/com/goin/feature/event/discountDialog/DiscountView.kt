package br.com.goin.feature.event.discountDialog

import br.com.goin.component.model.event.Event

interface DiscountView {
    fun goToIngressoRapidoSite(event: Event)
    fun configureClickListener()
    fun showToastFillFields()
    fun goToTickets(event: Event, code: String)
    fun showToastDiscountCodeInvalid()
    fun clearDiscountCodeField()
    fun showLoading()
    fun hideLoading()
}