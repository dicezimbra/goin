package br.com.goin.feature.splash.model

import io.reactivex.Observable

class UpdateInteractorImpl: UpdateInteractor {

    private val repository = UpdateRepository()

    override fun hasUpdates(version: String): Observable<VerifyUpdateStauts> {
        return repository.hasUpdates(version).map { it.data?.getVersionStatus }
    }
}