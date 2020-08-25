package br.com.goin.component.terms.model

import br.com.goin.base.BuildConfig
import br.com.goin.component.rest.api.RetrofitService
import io.reactivex.Single

class TermsRepository {
    companion object {
        var service = RetrofitService(TermsApi::class.java, BuildConfig.BASE_URL)
    }

    fun getTerms(language: String?, type: String): Single<TermsListModel> {
        return service.apiService.getTerms(language, type)
    }
}