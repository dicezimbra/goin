package br.com.goin.feature.search.location.searchLocation.model

import io.reactivex.Observable

interface CityInteractor{

    companion object {
        val instance by lazy {CityInteractorImpl()}
    }

    fun fetchByLocation(lat: Double, lng: Double): Observable<List<CityLocation>>
    fun fetchByName(name: String): Observable<List<CityLocation>>
}