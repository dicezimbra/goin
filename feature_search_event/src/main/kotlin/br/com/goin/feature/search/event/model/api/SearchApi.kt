package br.com.goin.feature.search.event.model.api

import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import br.com.goin.feature.search.event.model.CategorySearch
import io.reactivex.Observable
import retrofit2.http.*

interface SearchApi {

    @GET("search-by-expression")
    fun onSearchCall(@Query("expression") expression: String): Observable<List<CategorySearch>>

    @POST("graphql")
    fun onSearchByName(@Body graphqlQuery: GraphqlBody): Observable<GraphQLResponse<SearchByNameResponse>>
}