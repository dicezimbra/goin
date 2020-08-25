package br.com.goin.component.rest.api.model

import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.base.BuildConfig
import io.reactivex.Observable

class RefreshTokenRepository {

    companion object {
        var service = RetrofitService(RefreshTokenApi::class.java, BuildConfig.BASE_URL)
    }

    fun refreshToken(refreshToken: String, email: String): Observable<RefreshTokenResponse> {
        val body = RefreshTokenRequest(email = email, refreshToken = refreshToken)
        return service.apiService.refreshToken(body)
    }
}
