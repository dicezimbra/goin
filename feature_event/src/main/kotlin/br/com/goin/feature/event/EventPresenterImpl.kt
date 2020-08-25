package br.com.goin.feature.event

import android.location.Location
import android.util.Log
import br.com.goin.component.rest.api.helpers.ErrorHelper
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.extensions.useCache
import br.com.goin.base.extensions.useMemoryCache
import br.com.goin.component.model.event.EventInteractor
import br.com.goin.component.model.event.Event
import br.com.goin.component.model.event.api.response.ApiVoucher
import br.com.goin.component.model.uber.UberInteractor
import br.com.goin.component.session.user.UserSessionInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import retrofit2.HttpException
import java.io.Serializable

private const val USER_NOT_AUTHORIZED = "403 user is not authorized to see event details"

class EventPresenterImpl(val view: EventView) : EventPresenter {

    private val uberInteractor = UberInteractor.instance
    private val interactor = EventInteractor.instance
    private val userSession = UserSessionInteractor.instance.fetchUser()

    private var eventId: String? = null
    private var clubId: String? = null
    private var eventType: String? = null
    private var location: Location? = null
    private var event: Event? = null

    private val disposables = CompositeDisposable()

    override fun onCreate() {
        view.configureToolbar()
        view.configureOnClickListeners()
        view.verifyGpsEnabled()
        loadEvent()
    }

    override fun onDestroy() {
        disposables.dispose()
    }

    override fun onReceiveLocation(it: Location) {
        location = it
        event?.let {
            loadUberInformation(it, latitude = location!!.latitude, longitude = location!!.longitude)
        }
    }

    override fun onReceivedClubId(serializableExtra: Serializable?) {
        clubId = serializableExtra as? String
    }

    override fun onReceivedEventId(serializableExtra: Serializable?) {
        eventId = serializableExtra as? String
    }

    override fun onShareClicked() {
        event?.let {
            view.shareEvent(it)
        }
    }

    override fun onUberClicked() {
        event?.let {
            view.configureUberButton(it)
        }
    }

    override fun logOnAnalytics(action: String) {
        event?.name?.let {
            view.logOnAnalytics(action, it)
        }
    }

    override fun onLikeClicked() {
        event?.let {
            if (it.isCheckedIn()) {
                view.showCannotRemoveLikeDialog()
            } else {
                if (it.isConfirmed) {
                    view.showDialogUnconfirmPresence(it)
                } else {
                    it.isConfirmed = true

                    confirmPresence(it)
                    view.configureButtonConfirmLike(it.isConfirmed)
                }
            }
        }
    }


    override fun onConfirmUnLike() {
        event?.let {
            it.isConfirmed = false
            unconfirmPresence(it.id)
            view.configureButtonConfirmLike(it.isConfirmed)
        }
    }

    override fun onMapClicked() {
        event?.let {
            view.openMaps(it)
        }
    }

    override fun onClickBuyTicket() {
        event?.let {
            if (it.isCorporative()) {
                confirmStefaniniTicket()
            } else {

                when (it.originAction) {
                    Event.OriginAction.PAGAMENTO_AUTENTICADO.type -> view.loginInIngresse(it)
                    Event.OriginAction.SITE.type -> view.openAloIngressos(it)
                    Event.OriginAction.PAGAMENTO.type -> view.openPaymentScreen(it)
                    else -> view.openTickets(it)
                }
            }
        }
    }

    private fun loadEvent() {
        eventId?.let { eventId ->
            interactor.getDetail(eventId, clubId)
                    .useMemoryCache("EVENT$eventId", Event::class.java)
                    .ioThread()
                    .doOnSubscribe { view.showLoading() }
                    .doOnSubscribe { if (event == null) view.showLoading() }
                    .subscribe({
                        this.event = it
                        view.onEventLoaded(it)
                        view.hideLoading(it.isCorporative())
                        view.configureActionsComponent(it)
                        view.configureButtonConfirmLike(it.isConfirmed)
                        configureBuyButton()

                        if(it.vouchers == null || it.vouchers!!.isEmpty()){
                            view.configureEmptyVouchers()

                        }else{
                            view.configureVouchers(it.vouchers!!)
                        }

                        userSession.let { userModel ->
                            if(it.categoryEventType == "NAME_ON_LIST") {
                                view.configureEventAttendance(eventType, eventId, userModel, it)
                            }
                        }

                        if (it.lat != null && it.lng != null) {
                            view.configureMapPosition(it.lat!!, it.lng!!)
                        }

                        view.configureExpandableLayout(it)
                        location?.let { location ->
                            loadUberInformation(it, location.latitude, location.longitude)
                        }

                    }, { throwable: Throwable ->

                        val errorMessage = ErrorHelper.getErrorMessage(throwable)
                        when {
                            errorMessage != null && errorMessage == USER_NOT_AUTHORIZED -> {
                                view.showDialogNotAuthorized()
                            }
                            else -> {
                                view.handleError(throwable)
                            }
                        }

                        Log.w("EventPresenter", throwable)
                    }).addTo(disposables)
        }
    }

    override fun configureBuyButton() {
        event?.let {
            when {
                it.isCorporative() -> configureCorporativeEvent()
                it.buttonColor != null && it.categoryEventType != "NAME_ON_LIST" -> view.configureBuyButtonByColor(it)
                it.categoryEventType != "NAME_ON_LIST" -> view.configureGoInButton()
            }
        }
    }

    override fun loadUberInformation(latitude: Double, longitude: Double) {
        event?.let {
            loadUberInformation(it, latitude, longitude)
        }
    }

    override fun claimTicket(voucher: ApiVoucher){
        userSession?.let { user ->
            event?.let { event ->
                interactor.claimVoucher(voucher.giftId, event.clubId ?: "", user.name)
                        .ioThread()
                        .doOnSubscribe { view.showLoadingVoucher() }
                        .doOnTerminate { view.hideLoadingVoucher() }
                        .subscribe({

                            view.claimTicketSuccess(voucher)
                        }, { t ->

                            view.claimTicketError()
                            Log.e("EventPresenter", t.message, t)
                        })
            }
        }
    }

    private fun confirmPresence(event: Event) {
        disposables.add(interactor.putInterest(event.id, event.name, event.photoUrl?: "")
                .ioThread()
                .subscribe({
                }, { t -> Log.w("MovieDetailPresenter", t) }))
    }

    private fun unconfirmPresence(eventId: String) {
        disposables.add(interactor.removeInterest(eventId)
                .ioThread()
                .subscribe({
                }, { t -> Log.w("MovieDetailPresenter", t) }))
    }

    private fun loadUberInformation(event: Event, latitude: Double, longitude: Double) {
        if(event.lat != null && event.lng != null) {
            event.lat?.let { lat ->
                event.lng?.let { lng ->
                    uberInteractor.fetchUberInformation(lat, lng, latitude, longitude)
                            .ioThread()

                            .subscribe({
                                it.price?.let { price ->
                                    view.configureUberPrice(price)
                                }

                                it.time?.let { time ->
                                    view.configureUberTime(time)
                                }

                            }, {
                                Log.w("EventPresenter", it)
                            }).addTo(disposables)
                }
            }
        }
    }

    private fun confirmTokenValid(event: Event) {
        interactor.confirmTokenValid(Event.OriginAction.INGRESSE.type)
                .ioThread()
                .subscribe({ isConfirmed ->
                    if (isConfirmed) {
                        view.openTickets(event)
                    } else {
                        view.loginInIngresse(event)
                    }
                }, {
                    Log.w("EventPresenter", it)
                }).addTo(disposables)
    }

    private fun confirmStefaniniTicket() {
        eventId?.let { eventId ->
            interactor.confirmStefaniniTicket(eventId)
                    .ioThread()
                    .doOnSubscribe { view.showButtonProgress() }
                    .doOnTerminate { view.hideButtonProgress() }
                    .subscribe({
                        view.showDialogConfirmedPresenceSuccess()
                        view.hideBuyTicketButton()

                    }, { throwable: Throwable ->
                        val errorMessage = ErrorHelper.getErrorMessage(throwable)
                        errorMessage?.let {
                            view.showErrorOnConfirmAttendance(errorMessage)
                        }

                        Log.w("EventPresenter", throwable)
                    }).addTo(disposables)
        }
    }

    private fun configureCorporativeEvent() {
        event?.let {
            view.showConfirmAttendanceButton()
            view.hideShareButton()
            view.hideLikeButton()
        }
    }

    override fun onAttendanceConfirm(eventId: String?, userName: String?) {
        interactor.confirmNameOnList(eventId, userName)
                .ioThread()
                .doOnSubscribe {
                    view.loadingNameOnList()
                }
                .doOnTerminate {
                    view.loadingNameOnListFinished()
                }
                .subscribe({
                    view.onSuccessAttendance()

                }, { throwable ->
                    val errorMessage = ErrorHelper.getErrorMessage(throwable)
                    errorMessage?.let {
                        view.onErrorAttendance(errorMessage)
                    }
                    Log.e("EventPresenter", throwable.localizedMessage, throwable)
                }).addTo(disposables)
    }
}