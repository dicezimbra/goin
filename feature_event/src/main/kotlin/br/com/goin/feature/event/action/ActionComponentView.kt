package br.com.goin.feature.event.action

import br.com.goin.component.model.event.Event

interface ActionComponentView {
    fun showCheckingDialog(event: Event)
    fun showCheckingDisabledDialog(event: Event)
    fun configureOnClickListeners()
    fun configureActions()
    fun goToInvite(eventId: String)
    fun configureCheckin(event: Event)
    fun reloadEventModel(event: Event)
    fun openTickets(event: Event)
    fun openAloIngressos(event: Event)
    fun loginInIngresse(event: Event)
}