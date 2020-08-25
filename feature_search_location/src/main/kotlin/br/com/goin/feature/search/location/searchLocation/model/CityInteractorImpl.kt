package br.com.goin.feature.search.location.searchLocation.model

import io.reactivex.Observable

class CityInteractorImpl : CityInteractor {

    private val repository = CityRepository()

    override fun fetchByName(name: String): Observable<List<CityLocation>> {
        return repository.fetchCities(name)
                .flatMapIterable { it }
                .map {
                    CityLocation(lat = it.lat, lng = it.lng, title = "${it.city}, ${it.state}", uf= it.uf,
                            toolbarTitle = "(${it.city}- ${it.uf})", city = it.city, state = it.state)
                }
                .toList().toObservable()
    }

    override fun fetchByLocation(lat: Double, lng: Double): Observable<List<CityLocation>> {
        return repository.fetchCities(lat = lat, lng = lng, radius = 20)
                .flatMapIterable { it }
                .map {
                    CityLocation(lat = it.lat, lng = it.lng, title = "${it.city}, ${it.state}", uf= it.uf,
                            toolbarTitle = "(${it.city}- ${it.uf})", city = it.city, state = it.state)
                }
                .toList().toObservable()
    }
}