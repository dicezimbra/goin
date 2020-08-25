package br.com.goin.feature.splash.model

import io.reactivex.Observable

interface UpdateInteractor{

    companion object {
        val instance by lazy { UpdateInteractorImpl() }
    }

    fun hasUpdates(version: String): Observable<VerifyUpdateStauts>
}