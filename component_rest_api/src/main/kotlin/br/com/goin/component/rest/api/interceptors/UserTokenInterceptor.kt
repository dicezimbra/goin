package br.com.goin.component.rest.api.interceptors

import android.annotation.SuppressLint
import android.util.Log
import br.com.goin.component.rest.api.model.RefreshTokenInteractor
import br.com.goin.component.session.user.UserSessionInteractor
import br.com.goin.base.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class UserTokenInterceptor : Interceptor {

    private val refreshTokenInteractor = RefreshTokenInteractor.instance

    private val userSessionInteractor = UserSessionInteractor.instance
    private val defaultIdentityProvider = BuildConfig.COGNITO_IP_PREFIX + BuildConfig.COGNITO_USER_POOL_ID

    @SuppressLint("CheckResult")
    @Throws(IOException::class)

    override fun intercept(chain: Interceptor.Chain): Response? {
        val request = chain.request()
        val builder = request.newBuilder()

        setIdentityProviderHeader(builder, request)
        setTokenHeader(builder)

        var response = chain.proceed(builder.build())
        if (response?.code() == 401) {
            synchronized(this) {

                refreshTokenInteractor.refreshToken()?.subscribe({}, { throwable ->
                    Log.e("UserTokenInterceptor", throwable.message, throwable)
                })

                setTokenHeader(builder)
                response = chain.proceed(builder.build())
            }
        }

        return response
    }

    private fun setTokenHeader(builder: Request.Builder) {
        val token = userSessionInteractor.fetchToken()
        token?.let {
            builder.addHeader("token", it.token)
            Log.e("GOIN", "TOKEN  = ${it.token}")
        }
    }

    private fun setIdentityProviderHeader(builder: Request.Builder, request: Request) {
        if(request.header("identity-provider") == null) {
            val identityProvider = userSessionInteractor.fetchToken()?.identityProvider
                    ?: defaultIdentityProvider

            builder.addHeader("identity-provider", identityProvider)
            Log.e("GOIN", "Identity-Provider  = $identityProvider")
        }
    }
}
