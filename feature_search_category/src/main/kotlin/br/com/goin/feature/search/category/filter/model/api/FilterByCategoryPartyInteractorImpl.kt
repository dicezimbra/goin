package br.com.goin.feature.search.category.filter.model.api

import br.com.goin.feature.search.category.filter.model.ChipTagResponse
import io.reactivex.Observable

class FilterByCategoryPartyInteractorImpl: FilterByCategoryPartyInteractor {

    companion object {

        val instance : FilterByCategoryPartyInteractor = FilterByCategoryPartyInteractorImpl()
        val repository = FilterByCategoryPartyRepository()
    }

    override fun getChipTags(): Observable<ChipTagResponse> {

       return repository.getChipTags()
    }
}