package br.com.goin.feature.feed.post.checkIn

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.feature.feed.model.checkIn.CheckInInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class CheckInPresenterImpl(val view: CheckInView) : CheckInPresenter {

    private val interactor = CheckInInteractor.instance
    private val disposables = CompositeDisposable()
    private var myLocation: FloatArray? = null

    override fun onCreate() {
        view.configureSearchView()
        view.configureOnClickListeners()
        view.askPermissionsLocation()
    }


    override fun onReceiveLocation(location: FloatArray?) {
        myLocation = location
        myLocation?.let {
            loadEventsToCheckIn(it)
        }
    }

    override fun loadEventsToCheckIn(myLocation: FloatArray) {
        interactor.getEventsToCheckIn(myLocation)
                .ioThread()
                .doOnSubscribe {
                    view.showLoading()
                }
                .subscribe({
                    view.onReceiveEventsToCheckIn(it)
                    view.hideLoading()
                }, { throwable: Throwable ->
                    view.showNoEventsAvaiable()

                    Log.e("CheckInPresenter", throwable.message, throwable)
                }).addTo(disposables)
    }

    override fun logOnAnalytics(action: String, label: String?) {

    }
}

