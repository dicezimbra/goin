package br.com.goin.features.wallet.ticket

import java.io.Serializable

interface TicketsPresenter {
    fun onCreate()
    fun onDestroy()
    fun onReceiveTickets(serializable: Serializable?)
}