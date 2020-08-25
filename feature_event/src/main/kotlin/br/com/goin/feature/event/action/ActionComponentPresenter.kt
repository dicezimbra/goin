package br.com.goin.feature.event.action

import br.com.goin.component.model.event.Event

interface ActionComponentPresenter {
    fun onCreate()
    fun onReceiveEventModel(event: Event)
    fun onCheckinClicked()
    fun onInviteClicked()
    fun reloadEventModel(event: Event)
    fun onDestroy()
}