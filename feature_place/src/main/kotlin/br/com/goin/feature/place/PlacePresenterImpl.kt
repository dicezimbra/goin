package br.com.goin.feature.place

import android.location.Location
import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.extensions.useCache
import br.com.goin.base.extensions.useMemoryCache
import br.com.goin.component.model.event.EventInteractor
import br.com.goin.component.model.event.api.response.ApiVoucher
import br.com.goin.component.model.uber.UberInteractor
import br.com.goin.component.session.user.UserSessionInteractor
import br.com.goin.component.session.user.location.UserLocationInteractor
import br.com.goin.component_model_club.ClubInteractor
import br.com.goin.component_model_club.model.ClubModel
import com.google.android.gms.measurement.AppMeasurement
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class PlacePresenterImpl(val view: PlaceView) : PlacePresenter {

    private val eventInteractor = EventInteractor.instance
    private val interactor = ClubInteractor.instance
    private val uberInteractor = UberInteractor.instance
    private var userSession = UserSessionInteractor.instance.fetchUser()
    private val disposable = CompositeDisposable()
    private var placeId: String? = ""
    private var categoryName: String? = ""
    private var club: ClubModel? = null

    override fun onCreate() {
        loadPlace()
    }

    override fun onReceiveLocation(location: Location) {
        club?.let {
            loadUberInformation(it, latitude = location.latitude, longitude = location.longitude)
        }
    }

    override fun onReceivePlaceId(placeId: String?) {
        this.placeId = placeId
    }

    override fun onReceiveCategoryName(categoryName: String?) {
        this.categoryName = categoryName
    }

    override fun onDestroy() {
        disposable.dispose()
    }

    override fun onMapClicked() {
        club?.let {
            view.openMaps(it)
        }
    }

    override fun onCheckinClicked() {
        club?.let {
            view.showCheckingDialog(it)
        }
    }

    override fun onInviteFriendClicked() {
        userSession = UserSessionInteractor.instance.fetchUser()
        userSession?.let {
            view.inviteFriend(it)
        }
    }

    override fun onFollowClicked() {
        club?.let {
            if (it.isFollowing) {
                view.showDialogUnconfirmPresence(it)
            } else {
                it.isFollowing = true

                follow(it)
                view.configureButtonConfirmFollow(it.isFollowing)
            }
        }
    }

    override fun onConfirmUnfollow() {
        club?.let {
            it.isFollowing = false
            unfollow(it.clubId)
            view.configureButtonConfirmFollow(it.isFollowing)
        }
    }

    private fun unfollow(placeId: String) {
        interactor.removeInterest(placeId)
                .ioThread()
                .subscribe({}, { t -> Log.w("PlacePresenter", t) })
                .addTo(disposable)
    }

    private fun follow(club: ClubModel) {
        interactor.putInterest(club.clubId, club.clubName ?: "", club.coverImage ?: "")
                .ioThread()
                .subscribe({}, { t -> Log.w("PlacePresenter", t) })
                .addTo(disposable)
    }

    private fun loadPlace() {
        placeId?.let { placeId ->
            interactor.get(placeId)
                    .useMemoryCache("PLACE_CACHE_$placeId", ClubModel::class.java)
                    .ioThread()
                    .doOnSubscribe { view.showLoading() }
                    .subscribe({
                        view.hideLoading()
                        this.club = it
                        view.configurePlace(it, categoryName)
                        view.configureReadMore(it)
                        view.configureOnClicks(it)
                        view.configureToolbar(it.clubName ?: "")
                        view.retriveCurrentPosition()
                        view.initializeMap(it)

                        if (it.galleryUrls != null && it.galleryUrls!!.isNotEmpty()) {
                            view.configureGallery(it.galleryUrls!!)
                        }

                        if (it.events != null && it.events!!.isNotEmpty()) {
                            view.configureEvents(it.events!!)
                        }

                        if (it.giftsGallery != null && it.giftsGallery!!.isNotEmpty()){
                            view.configureVouchers(it.giftsGallery)
                        }

                    }, { t ->
                        view.handleError(t)
                        Log.e("PlacePresenter", t.message, t)
                    })
                    .addTo(disposable)
        }
    }

    private fun loadUberInformation(club: ClubModel, latitude: Double, longitude: Double) {
        uberInteractor.fetchUberInformation(club.latitude?.toDouble()
                ?: 0.0, club.longitude?.toDouble() ?: 0.0, latitude, longitude)
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
                }).addTo(disposable)
    }

    override fun claimTicket(voucher: ApiVoucher){
        userSession?.let {
            club?.let { club ->
                eventInteractor.claimVoucher(voucher.giftId, club.clubId, it.name)
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
}