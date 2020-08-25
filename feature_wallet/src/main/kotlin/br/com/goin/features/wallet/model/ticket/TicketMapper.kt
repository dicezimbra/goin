package br.com.goin.model.ticket

import br.com.goin.base.dtos.TicketDetails
import br.com.goin.base.model.PurchaseModel
import br.com.goin.features.wallet.model.TicketModel
import java.util.*

class TicketMapper {

    fun map(dtos: List<TicketDetails>, cardType: TicketModel.CardType?): List<TicketModel> {
        val list = ArrayList<TicketModel>()
        for (t in dtos) {
            val ticketModel = TicketModel()
            ticketModel.id = t.id
            ticketModel.price = t.price
            ticketModel.name = t.name
            ticketModel.description = t.description
            ticketModel.total = t.quantity
            ticketModel.eventAddress = t.eventAddress
            ticketModel.originType = t.originType
            ticketModel.qrCode = t.qrCode
            ticketModel.ticketType = t.ticketType
            ticketModel.isValid = t.isValid
            ticketModel.paymentId = t.paymentId

            val eventDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
            eventDateCalendar.timeZone = TimeZone.getTimeZone("GMT")
            val purchaseDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
            purchaseDateCalendar.timeZone = TimeZone.getTimeZone("GMT")

            if (t.eventDate != null) {
                eventDateCalendar.timeInMillis = t.eventDate
                ticketModel.date = eventDateCalendar
            }
            if (t.paymentDate != null) {
                purchaseDateCalendar.timeInMillis = t.paymentDate
                ticketModel.purchaseDate = purchaseDateCalendar
            }


            ticketModel.event = t.event
            ticketModel.isHalfPrice = t.isHalfPrice
            ticketModel.remaining = t.remaining
            ticketModel.club = t.clubName
            ticketModel.eventName = t.eventName
            ticketModel.bought = if (t.numTickets == null) 0 else t.numTickets
            ticketModel.userName = t.buyerName
            ticketModel.isVipBox = t.enumTicketType != null && t.enumTicketType == PurchaseModel.TicketType.VIP
            ticketModel.vipCapacity = if (t.capacity == null) 0 else t.capacity
            if (ticketModel.isVipBox && cardType == TicketModel.CardType.MY_TICKET) {
                ticketModel.ticketCardType = TicketModel.CardType.MY_TICKET_VIP_BOX
            } else {
                ticketModel.ticketCardType = cardType
            }
            if (t.paymentStatus != null) {
                if (ticketModel.isVipBox) {
                    when (t.paymentStatus) {
                        TicketDetails.PaymentStatus.Unpaid -> ticketModel.buyStatus = TicketModel.TicketStatus.VIP_BOX_PENDING
                        TicketDetails.PaymentStatus.Pending -> ticketModel.buyStatus = TicketModel.TicketStatus.VIP_BOX_PENDING
                        TicketDetails.PaymentStatus.Confirmed -> ticketModel.buyStatus = TicketModel.TicketStatus.VIP_BOX_CONFIRMED
                        TicketDetails.PaymentStatus.AwaitingFriends -> ticketModel.buyStatus = TicketModel.TicketStatus.VIP_BOX_FRIENDS_PENDING
                        else -> ticketModel.buyStatus = TicketModel.TicketStatus.PENDING
                    }
                } else {
                    when (t.paymentStatus) {
                        TicketDetails.PaymentStatus.Confirmed -> ticketModel.buyStatus = TicketModel.TicketStatus.CONFIRMED
                        TicketDetails.PaymentStatus.Pending -> ticketModel.buyStatus = TicketModel.TicketStatus.PENDING
                        else -> ticketModel.buyStatus = TicketModel.TicketStatus.PENDING
                    }
                }
            } else {
                if (cardType != null && cardType == TicketModel.CardType.TICKET_FOR_SELL) {
                    ticketModel.buyStatus = TicketModel.TicketStatus.FOR_SELL
                } else {
                    ticketModel.buyStatus = TicketModel.TicketStatus.PENDING
                }
            }
            list.add(ticketModel)
        }
        return list
    }
}