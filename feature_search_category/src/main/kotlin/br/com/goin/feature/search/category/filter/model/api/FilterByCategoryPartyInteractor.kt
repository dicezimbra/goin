package br.com.goin.feature.search.category.filter.model.api

import br.com.goin.feature.search.category.filter.model.ChipTagResponse
import io.reactivex.Observable

interface FilterByCategoryPartyInteractor {

    fun getChipTags(): Observable<ChipTagResponse>
}