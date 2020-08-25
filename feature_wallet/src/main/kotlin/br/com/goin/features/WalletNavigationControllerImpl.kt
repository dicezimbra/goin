package br.com.goin.features

import android.content.Context
import br.com.goin.component.navigation.WalletNavigationController
import br.com.goin.eventticket.TicketsActivity

class WalletNavigationControllerImpl : WalletNavigationController {
    override fun goToEventTickets(context: Context, eventId: String) {
        TicketsActivity.starter(context, eventId)
    }

}