package br.com.goin.component.terms.model

import io.reactivex.Single

interface TermsInteractor {
    companion object {
        val instance: TermsInteractor = TermsInteractorImpl()
    }

    fun fetchTerms(language: String?, type: String): Single<List<TermsModel>>
}