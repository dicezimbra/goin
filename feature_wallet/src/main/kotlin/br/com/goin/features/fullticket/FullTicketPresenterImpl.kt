package br.com.goin.features.fullticket

import android.util.Log
import br.com.goin.base.extensions.clearCache
import br.com.goin.base.extensions.ioThread
import br.com.goin.component.rest.api.helpers.ErrorHelper
import br.com.goin.component.session.user.UserSessionInteractor
import br.com.goin.features.wallet.model.Ticket
import br.com.goin.features.wallet.model.TicketModel
import br.com.goin.model.ticket.TicketInteractor
import io.reactivex.disposables.CompositeDisposable
import java.io.Serializable

class FullTicketPresenterImpl(val view :FullTicketView): FullTicketPresenter{

    private var ticket: Ticket? = null
    private val user = UserSessionInteractor.instance.fetchUser()
    private val interactor = TicketInteractor.instance
    private val disposable = CompositeDisposable()

    override fun onCreate(){
        view.configureAnimations()
        ticket?.let {
            view.initActions(it)
            view.configureTicket(it)
            view.configureOnClickListeners(it)
        }
    }

    override fun onDestroy(){
        disposable.dispose()
    }

    override fun onReceiveTicket(serializable: Serializable?) {
        this.ticket = serializable as Ticket
    }

    override fun validateTicket(){
        ticket?.let {
            interactor.validate(it.ticketId ?: "")
                    .ioThread()
                    .subscribe({
                        clearCache("WALLET_${user?.id}")
                        view.cutOffTicket()
                    }, { t ->
                        view.showErrorDialog()
                        Log.e("FullTicket", t.message, t)
                    })
        }
    }
}