package br.com.goin.feature.search.event.model

import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.feature.search.event.model.api.SearchByNameResponse
import br.com.goin.feature.search.event.model.api.SearchResponse
import br.com.goin.feature.search.event.model.api.SearchResponseByCategory
import io.reactivex.Observable

interface SearchInteractor {

    companion object {
        val instance: SearchInteractor = SearchInteractorImpl()
    }

    fun onSearchCall(querySearch: String): Observable<SearchResponseByCategory>
    fun onSearchMovies(querySearch: String, cameFrom: String?): Observable<SearchByNameResponse>
}