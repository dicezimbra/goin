package br.com.goin.component.model.uber.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface UberApi {

    @GET("/v1.2/estimates/price")
    fun getUberEstimatedPrice(
        @Header("Authorization") authToken: String,
        @Query("start_latitude") start_latitude: Double,
        @Query("start_longitude") start_longitude: Double,
        @Query("end_latitude") end_latitude: Double,
        @Query("end_longitude") end_longitude: Double
    ): Observable<UberPricesResponse<UberEstimatedPriceModel>>

    @GET("/v1.2/estimates/time")
    fun getUberEstimatedTime(
        @Header("Authorization") authToken: String,
        @Query("start_latitude") start_latitude: Double,
        @Query("start_longitude") start_longitude: Double
    ): Observable<UberTimeResponse<UberEstimatedTimeModel>>
}
