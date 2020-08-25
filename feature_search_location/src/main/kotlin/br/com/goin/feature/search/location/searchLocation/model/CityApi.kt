package br.com.goin.feature.search.location.searchLocation.model

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface CityApi {

    @GET("cities")
    fun fetchCities(@Query("name") name: String? = null,
                    @Query("lat") lat: Double?= null,
                    @Query("lng") lng: Double?= null,
                    @Query("radius") radius: Int?= null): Observable<List<CityResponse>>
}