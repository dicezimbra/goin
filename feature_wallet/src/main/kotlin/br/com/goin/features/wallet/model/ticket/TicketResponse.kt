package br.com.goin.features.wallet.model.ticket

import br.com.goin.features.wallet.model.EventTicket
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TicketResponse(@SerializedName("getEventTickets") val eventTickets: List<EventTicket>): Serializable