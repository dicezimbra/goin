package br.com.goin.features.fullticket

import java.io.Serializable

interface FullTicketPresenter {
    fun onCreate()
    fun onReceiveTicket(serializable: Serializable?)
    fun onDestroy()
    fun validateTicket()
}