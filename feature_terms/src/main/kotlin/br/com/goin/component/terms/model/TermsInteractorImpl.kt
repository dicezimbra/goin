package br.com.goin.component.terms.model

import io.reactivex.Single

class TermsInteractorImpl : TermsInteractor {
    private val repository = TermsRepository()

    override fun fetchTerms(language: String?, type: String): Single<List<TermsModel>> {
        return repository.getTerms(language, type).map { it.data }
    }
}