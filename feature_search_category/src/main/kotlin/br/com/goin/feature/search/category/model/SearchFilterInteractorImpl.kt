package br.com.goin.feature.search.category.model

import br.com.goin.feature.search.category.model.api.SearchFilterRequest
import io.reactivex.Observable

class SearchFilterInteractorImpl: SearchFilterInteractor{

    private val repository = SearchFilterRepository()
    private val mapper = SearchFilterMapper()

    override fun search(request: SearchFilter, searchFilterTimePeriod: SearchFilterTimePeriod, page: Int): Observable<SearchFilterPage> {
        return repository.search(mapper.map(request, searchFilterTimePeriod), limit= 20, page= page).map { mapper.map(it.data?.searchFilter) }
    }

    override fun getSubCategories(categoryId: String): Observable<ResponseSubcategories> {
        return repository.getSubCategories(categoryId).map { it.data }
    }
}
