package br.com.goin.features.wallet.ticket

import br.com.goin.features.wallet.model.Ticket
import io.reactivex.disposables.CompositeDisposable
import java.io.Serializable

class TicketPresenterImpl(val view: TicketsView) : TicketsPresenter {

    private var disposable = CompositeDisposable()
    private var tickets: MutableList<Ticket>? = null

    override fun onCreate() {
        view.configureRecyclerView()
        onTicketsReceived()
    }

    override fun onReceiveTickets(serializable: Serializable?) {
        this.tickets = serializable as ArrayList<Ticket>
    }

    override fun onDestroy() {
        disposable.dispose()
    }

    private fun onTicketsReceived() {
        tickets?.let {
            when {
                it.isEmpty() -> {
                    view.configureEmptyState()
                }
                it.isNotEmpty() -> {
                    view.onTicketLoaded(it)
                }
            }
        }
    }
}