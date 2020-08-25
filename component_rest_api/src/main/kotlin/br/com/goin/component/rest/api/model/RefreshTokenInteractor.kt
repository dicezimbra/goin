package br.com.goin.component.rest.api.model

import io.reactivex.Completable

interface RefreshTokenInteractor {

    companion object {
        val instance: RefreshTokenInteractor by lazy { RefreshTokenInteractorImpl() }
    }

    fun refreshToken(): Completable?
}