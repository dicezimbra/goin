package br.com.goin.feature.feed.model.checkIn

import io.reactivex.Observable

interface CheckInInteractor {

    companion object {
        val instance: CheckInInteractor by lazy { CheckInInteractorImpl() }
    }

    fun getEventsToCheckIn(myLocation: FloatArray): Observable<List<CheckIn>>


}