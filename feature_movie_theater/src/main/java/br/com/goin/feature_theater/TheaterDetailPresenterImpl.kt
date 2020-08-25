package br.com.goin.feature_theater

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.extensions.useCache
import br.com.goin.base.extensions.useMemoryCache
import br.com.goin.component.model.uber.UberInteractor
import br.com.goin.component.session.user.location.UserLocationInteractor
import br.com.goin.component_model_club.ClubInteractor
import br.com.goin.component_model_club.model.ClubModel
import br.com.goin.feature_theater.model.TheaterInteractor
import br.com.goin.feature_theater.model.TheaterResponse
import io.reactivex.disposables.Disposable

class TheaterDetailPresenterImpl(val view: TheaterDetailView) : TheaterDetailPresenter {

    private val theaterInteractor = TheaterInteractor.instance
    private val clubInteractor = ClubInteractor.instance
    private var disposable: Disposable? = null
    private val userLocationInteractor = UserLocationInteractor.instance
    private val uberInteractor = UberInteractor.instance
    private var mClubModel: ClubModel? = null

    override fun onCreate(clubId: String) {

        view.configureToolbar()
        loadSessions(clubId)
        view.configureOnClickListeners()
    }

    override fun onDestroy() {
        disposable?.dispose()
    }

    override fun onClickShare() {

        view.shareClub(mClubModel!!)
    }

    override fun onInviteClicked() {

        view.goToInvite(mClubModel?.clubId)
    }

    override fun onMapClicked() {

        view.openMaps(mClubModel!!)
    }

    override fun onUberClicked() {
        view.configureUberButton(mClubModel!!)
    }

    private fun loadSessions(clubId: String) {

        disposable = clubInteractor.get(clubId)
                .useMemoryCache(key = "CLUB_CACHE", type = ClubModel::class.java)
                .ioThread()
                .flatMap {

                    mClubModel = it
                    view.configureMapPosition(mClubModel!!)
                    view.onReceiveTheater(mClubModel!!)

                    loadUberPrice(mClubModel!!,
                            userLocationInteractor.fetch().lat,
                            userLocationInteractor.fetch().lng)

                    theaterInteractor.sessionsByClub(clubId).ioThread()
                }
                .useCache(key = "CLUB_SESSION_CACHE", type = TheaterResponse::class.java)
                .ioThread()
                .doOnSubscribe { view.showProgress() }
                .subscribe({

                    view.configureSessionComponent(it)
                    view.hideProgress()
                }, { t ->

                    view.hideProgress()
                    Log.e("TheaterDetailPresenter", t.message)
                })
    }

    private fun loadUberPrice(clubModel: ClubModel, latitude: Double, longitude: Double) {

        disposable = uberInteractor.fetchUberInformation(clubModel.latitude!!.toDouble(),
                clubModel.longitude!!.toDouble(), latitude, longitude)
                .ioThread()
                .subscribe({
                    view.configureUber(it)
                }, {
                    Log.w("TheaterDetailPresenter", it)
                })
    }
}