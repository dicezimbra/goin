package br.com.goin.feature.search.location.searchLocation.model

import br.com.goin.base.BuildConfig
import br.com.goin.component.rest.api.RetrofitService
import io.reactivex.Observable

class CityRepository{

    private val service = RetrofitService(CityApi::class.java, BuildConfig.BASE_URL)

    fun fetchCities(name: String? = null, lat: Double? = null, lng: Double? = null, radius: Int? = null): Observable<List<CityResponse>> {
        return service.apiService.fetchCities(lat= lat,lng= lng, radius= radius, name= name)
    }
}