package br.com.goin.feature.search.event.model

import br.com.goin.base.BuildConfig
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import br.com.goin.feature.search.event.model.api.*
import io.reactivex.Observable

class SearchRepository {

    private val service = RetrofitService(SearchApi::class.java, BuildConfig.BASE_URL)

    fun onSearchCall(querySearch: String): Observable<List<CategorySearch>> {
        return service.apiService.onSearchCall(querySearch)
    }

    fun onSearchByName(querySearch: String, cameFrom: String?): Observable<GraphQLResponse<SearchByNameResponse>> {
        val graphqlQuery = GraphqlBody.Builder(SearchQueries.SEARCH_BY_NAME)
                .`var`("name", querySearch)
                .`var`("categoryId", cameFrom)
                .build()

        return service.apiService.onSearchByName(graphqlQuery)
    }
}