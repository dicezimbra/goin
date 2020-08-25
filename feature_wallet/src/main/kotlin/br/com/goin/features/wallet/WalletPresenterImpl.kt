package br.com.goin.features.wallet

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.extensions.useCache
import br.com.goin.base.extensions.useMemoryCache
import br.com.goin.component.session.user.UserSessionInteractor
import br.com.goin.features.wallet.model.Ticket
import br.com.goin.model.ticket.TicketInteractor
import com.google.gson.reflect.TypeToken
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

private const val TICKET = "TICKET"
private const val VOUCHER = "VOUCHER"
private const val ALL = "ALL"

class WalletPresenterImpl(val view: WalletView): WalletPresenter{

    private val interactor = TicketInteractor.instance
    private val userSessionInteractor = UserSessionInteractor.instance
    private val disposable = CompositeDisposable()

    override fun onCreate(){
        view.configureToolbar()
    }

    override fun onResume(){
        val user = userSessionInteractor.fetchUser()
        user?.let {
            loadWallet(it.id)
        }
    }

    override fun onDestroy(){
        disposable.dispose()
    }

    private fun loadWallet(userId: String){
        interactor.fetchWallet(userId)
                .useCache("WALLET_$userId", object: TypeToken<ArrayList<Ticket>>() {}.type)
                .ioThread()
                .doOnSubscribe { view.showLoading() }
                .doOnTerminate { view.hideLoading() }
                .subscribe({
                    val tickets = configureTickets(it)
                    view.configureView(tickets)
                }, { t ->
                    view.handleError(t)
                    Log.e("WalletPresenter", t.message, t)
                }).addTo(disposable)
    }

    private fun configureTickets(it: List<Ticket>): MutableMap<String, List<Ticket>> {
        val tickets = it.groupBy { it.type ?: "" }.toMutableMap()
        tickets[ALL] = it

        if (!tickets.containsKey(VOUCHER)) {
            tickets.put(VOUCHER, arrayListOf())
        }

        if (!tickets.containsKey(TICKET)) {
            tickets.put(TICKET, arrayListOf())
        }
        return tickets
    }
}