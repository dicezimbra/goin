package br.com.goin.feature.search.event.model

import br.com.goin.feature.search.event.model.api.SearchByNameResponse
import br.com.goin.feature.search.event.model.api.SearchMapper
import br.com.goin.feature.search.event.model.api.SearchResponse
import br.com.goin.feature.search.event.model.api.SearchResponseByCategory
import io.reactivex.Observable

class SearchInteractorImpl : SearchInteractor {

    private val repository = SearchRepository()
    private val mapper = SearchMapper()

    override fun onSearchCall(querySearch: String): Observable<SearchResponseByCategory> {
        return repository.onSearchCall(querySearch).map { mapper.mapper(it) }
    }

    override fun onSearchMovies(querySearch: String, cameFrom: String?): Observable<SearchByNameResponse> {
        return repository.onSearchByName(querySearch, cameFrom).map { it.data }
    }
}