package br.com.goin.feature.search.category.model

import br.com.goin.feature.search.category.model.api.SearchFilterRequest
import io.reactivex.Observable

interface SearchFilterInteractor {

    companion object {
        val instance: SearchFilterInteractor by lazy { SearchFilterInteractorImpl() }
    }

    fun getSubCategories(categoryId: String): Observable<ResponseSubcategories>
    fun search(request: SearchFilter, searchFilterTimePeriod: SearchFilterTimePeriod, page: Int): Observable<SearchFilterPage>
}