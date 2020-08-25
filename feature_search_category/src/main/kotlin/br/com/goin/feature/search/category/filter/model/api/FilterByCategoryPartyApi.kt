package br.com.goin.feature.search.category.filter.model.api

import br.com.goin.feature.search.category.filter.model.ChipTag
import io.reactivex.Observable
import retrofit2.http.GET

interface FilterByCategoryPartyApi {

    @GET("/baladas/get-tags")
    fun getChipTags() : Observable<List<ChipTag>>
}