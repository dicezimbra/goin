package br.com.goin.component.terms.model

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface TermsApi {

    @GET("get-policies")
    fun getTerms(@Query("language") language: String?, @Query("type") type: String): Single<TermsListModel>
}