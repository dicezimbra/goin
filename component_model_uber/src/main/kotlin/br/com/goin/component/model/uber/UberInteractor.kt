package br.com.goin.component.model.uber

import io.reactivex.Observable

interface UberInteractor {

    companion object {
        val instance: UberInteractor by lazy { UberInteractorImpl() }
    }

    fun fetchUberInformation(startLatitude: Double, startLongitude: Double, goalLatitude: Double?, goalLongitude: Double?): Observable<UberInformation>
}