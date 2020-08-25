package br.com.goin.component.rest.api.model

import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshTokenApi {

    @POST("refreshToken")
    fun refreshToken(@Body body: RefreshTokenRequest): Observable<RefreshTokenResponse>

}