package br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.session

import android.util.Log
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.model.EventSessions
import br.com.goin.base.extensions.ioThread
import br.com.goin.component.model.uber.UberInformation
import br.com.goin.component.model.uber.UberInteractor
import br.com.goin.component.session.user.location.UserLocation
import br.com.goin.component.session.user.location.UserLocationInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.io.Serializable

class SessionPresenterImpl(val view: SessionView) : SessionPresenter {

    private var sessions: List<Session>? = null
    private val uberInteractor = UberInteractor.instance
    private val userLocationInteractor = UserLocationInteractor.instance
    private val disposable = CompositeDisposable()
    private lateinit var userLocation: UserLocation

    override fun onCreate() {
        userLocation = userLocationInteractor.fetch()

        sessions?.let {
            view.configureRecyclerView(it)
        }

        loadUberForSession(userLocation.lat, userLocation.lng)
    }

    override fun onDestroy(){
        disposable.dispose()
    }

    override fun onReceivedSerializable(serializable: Serializable?) {
        val eventSessions = serializable as? EventSessions
        sessions = eventSessions?.sessions?.map {
            Session(lat = it.addressInfo?.lat,
                    lng = it.addressInfo?.lng,
                    week = eventSessions.week,
                    date = eventSessions.date,
                    address = it.addressInfo?.address ?: "",
                    clubName = it.clubName,
                    details = it.details)
        }
    }

    override fun onUberClicked(session: Session) {
        view.openUber(session, userLocation)
    }

    private fun loadUberForSession(latitude: Double, longitude: Double) {
        sessions?.forEachIndexed { index, session ->
            uberInteractor.fetchUberInformation(latitude, longitude, session.lat, session.lng)
                    .ioThread()
                    .subscribe({ uber: UberInformation? ->
                        session.uberInformation = uber
                        view.notifyAdapterPosiiton(index)

                    }, { t: Throwable ->
                        Log.e("MovieSession", t.message
                                , t)
                    })
                    .addTo(disposable)
        }
    }
}