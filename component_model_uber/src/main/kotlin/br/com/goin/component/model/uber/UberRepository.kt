package br.com.goin.component.model.uber

import br.com.goin.component.model.uber.api.*
import br.com.goin.component.rest.api.RetrofitService
import io.reactivex.Observable

class UberRepository {

    private val service = RetrofitService(UberApi::class.java, "https://api.uber.com")

    private val authToken = "Token 0rdzwbkE37saPIdLiKo-Co3njKIL3vvRSdyXaD93"

    fun requestEstimatedPrice(startLatitude: Double, startLongitude: Double, goalLatitude: Double, goalLongitude: Double): Observable<UberPricesResponse<UberEstimatedPriceModel>> {
        return service.apiService.getUberEstimatedPrice(authToken, startLatitude, startLongitude,
                goalLatitude, goalLongitude)
    }

    fun requestEstimatedTime(startLatitude: Double, startLongitude: Double): Observable<UberTimeResponse<UberEstimatedTimeModel>> {
        return service.apiService.getUberEstimatedTime(authToken, startLatitude, startLongitude)
    }
}