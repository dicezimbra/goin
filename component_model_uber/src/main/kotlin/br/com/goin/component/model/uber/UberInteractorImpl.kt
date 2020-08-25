package br.com.goin.component.model.uber

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.rxkotlin.zipWith

class UberInteractorImpl : UberInteractor {

    private val repository = UberRepository()

    override fun fetchUberInformation(startLatitude: Double, startLongitude: Double, goalLatitude: Double?, goalLongitude: Double?): Observable<UberInformation> {
        return requestEstimatedPrice(startLatitude, startLongitude, goalLatitude, goalLongitude).zipWith(requestEstimatedTime(startLatitude, startLongitude))
                .map { (price, time) ->
                    val finalPrice = if(price >= 0) price else null
                    val finalTime = if(time >= 0) time else null
                    UberInformation(finalPrice, finalTime)
                }
    }

    private fun requestEstimatedPrice(startLatitude: Double, startLongitude: Double, goalLatitude: Double?, goalLongitude: Double?): Observable<Float> {
        if (goalLatitude == null && goalLongitude == null) {
            return Observable.empty()
        }

        return repository.requestEstimatedPrice(startLatitude, startLongitude, goalLatitude!!, goalLongitude!!)
                .onErrorReturn { null }
                .flatMapIterable { it.prices }
                .map { it.low_estimate }
                .onErrorResumeNext(Observable.just(-1f))
                .reduce { t1: Float, t2: Float -> Math.min(t1, t2) }.toObservable()
    }

    private fun requestEstimatedTime(startLatitude: Double, startLongitude: Double): Observable<Int> {
        return repository.requestEstimatedTime(startLatitude, startLongitude)
                .flatMapIterable { it.times }
                .map { it.estimate }
                .onErrorResumeNext(Observable.just(-1))
                .reduce { t1: Int, t2: Int -> Math.min(t1, t2) }.toObservable()
    }
}