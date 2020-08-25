package br.com.goin.feature.search.category.filter.model.api

import br.com.goin.base.BuildConfig
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.feature.search.category.filter.model.ChipTagResponse
import br.com.goin.feature.search.category.filter.model.Mapper
import io.reactivex.Observable

class FilterByCategoryPartyRepository {

    val service = RetrofitService(FilterByCategoryPartyApi::class.java, BuildConfig.BASE_URL)
    val mapper = Mapper()

    fun getChipTags(): Observable<ChipTagResponse> {

        return service.apiService.getChipTags().map {
            mapper.chipTagsMap(it)
        }
    }
}