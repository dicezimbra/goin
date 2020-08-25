package br.com.goin.eventticket

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.extensions.useCache
import br.com.goin.base.model.PaymentModel
import br.com.goin.base.model.PaymentTicket
import br.com.goin.features.wallet.model.EventTicket
import br.com.goin.model.ticket.TicketInteractor
import com.google.gson.reflect.TypeToken
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import br.com.goin.component.model.event.Event
import br.com.goin.component.model.event.EventInteractor

class TicketPresenterImpl(val view: TicketView) : TicketPresenter {


    var eventId: String? = null
    var coupon: String? = null
    var originType: String? = null
    var ingtoken: String? = null
    var event: Event? = null

    private var paymentModel: PaymentModel = PaymentModel()
    private val disposable = CompositeDisposable()
    private val ticketInteractor = TicketInteractor.instance
    private val eventInteractor = EventInteractor.instance

    override fun onReceiveEventId(eventId: String?) {
        this.eventId = eventId
    }

    override fun onReceiveIngresseToken(ingresseToken: String?) {
        this.ingtoken = ingresseToken
    }

    override fun onReceiveOriginType(originType: String?) {
        this.originType = originType
    }

    override fun onReceiveCoupon(coupon: String?) {
        this.coupon = coupon
    }

    override fun onCreate() {
        view.configureToolbar()
        loadEvent()
        loadTickets()
    }

    private fun loadTickets() {
        eventId?.let { it ->
            ticketInteractor.fetchEventTickets(it)
                    .useCache("EVENTTICKETS$it", object : TypeToken<ArrayList<EventTicket>>() {}.type)
                    .ioThread()
                    .doOnSubscribe { view.showLoading() }
                    .doOnTerminate { view.hideLoading() }
                    .subscribe({ tickets ->
                        view.configureRecyclerView()
                        view.configureTickets(tickets)
                        view.configureMinusClicks()
                    }, { t ->
                        view.handleError(t)
                        Log.e("WalletPresenter", t.message, t)
                    }).addTo(disposable)
        }
    }

    private fun loadEvent() {
        eventId?.let { eventId ->
            eventInteractor.getDetail(eventId, null)
                    .useCache("EVENT$eventId", Event::class.java)
                    .ioThread()
                    .doOnSubscribe { view.showLoading() }
                    .doOnSubscribe { if (event == null) view.showLoading() }
                    .subscribe({
                        this.event = it
                        view.eventLoaded(it)
                        view.loadToolbar(it)

                    }, { throwable: Throwable ->


                    }).addTo(disposable)
        }
    }

    override fun onDestroy() {
        disposable.dispose()
    }

    override fun getIngresseToken(): String {
        ingtoken?.let { return it }
    }

    override fun fillPaymentModel() {
        paymentModel.eventId = eventId
        paymentModel.originType = originType
        paymentModel.userToken = ingtoken
    }

    override fun fillTicketModel(ticket: EventTicket) {
        paymentModel?.tickets?.add(convertModel(ticket))
    }

    override fun getPaymentModel(): PaymentModel {
        paymentModel?.let {
            return it
        }
    }

    private fun convertModel(ticket: EventTicket): PaymentTicket {
        var paymentTicket = PaymentTicket()
        paymentTicket.quantity = 1
        paymentTicket.ticketId = ticket.id
        paymentTicket.info.name = ticket.name
        paymentTicket.info.price = ticket.price
        paymentTicket.info.description = ticket.description
        paymentTicket.info.ticketType = ticket.ticketType
        paymentTicket.info.isHalfPrice = ticket.isHalfPrice
        paymentTicket.info.remaining = ticket.remaining
        paymentTicket.info.eventDate = ticket.eventDate
        paymentTicket.info.eventName = ticket.eventName
        paymentTicket.info.eventAddress = ticket.eventAddress
        paymentTicket.info.clubName = ticket.clubName
        paymentTicket.info.available = ticket.available
        paymentTicket.info.capacity = ticket.capacity
        return paymentTicket
    }


}