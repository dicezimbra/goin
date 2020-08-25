package br.com.goin.component.rest.api.model

import br.com.goin.component.session.user.UserSessionInteractor
import br.com.goin.base.BuildConfig
import io.reactivex.Completable

class RefreshTokenInteractorImpl : RefreshTokenInteractor {

    private val IDENTITY_PROVIDER = BuildConfig.COGNITO_IP_PREFIX + BuildConfig.COGNITO_USER_POOL_ID

    private val repository = RefreshTokenRepository()
    private val userSessionInteractor = UserSessionInteractor.instance

    override fun refreshToken(): Completable? {
        val token = userSessionInteractor.fetchToken()
        val user = userSessionInteractor.fetchUser()

        token?.let { tokenSafe ->
            user?.let { userSafe ->
                return repository.refreshToken(refreshToken = tokenSafe.refreshToken, email = userSafe.email)
                        .doOnNext {
                            userSessionInteractor.saveToken(token = it.token, refreshToken = it.refreshToken
                                    ?: tokenSafe.refreshToken, identityProvider = IDENTITY_PROVIDER)
                        }

                        .ignoreElements()
            }
        }

        return null
    }
}
