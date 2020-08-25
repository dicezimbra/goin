package br.com.goin.feature.event.discountDialog

import br.com.goin.component.model.event.Event

interface DiscountPresenter {
    fun onCreate()
    fun onConfirmDiscountCode(code: String)
    fun onClickNo()
    fun onReceivedEvent(event: Event)
    fun onDestroy()
}