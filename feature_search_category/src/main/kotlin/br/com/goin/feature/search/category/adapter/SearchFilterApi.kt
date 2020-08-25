package br.com.goin.feature.search.category.adapter

import br.com.goin.base.GraphqlQuery
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import br.com.goin.feature.search.category.model.ResponseSubcategories
import br.com.goin.feature.search.category.model.api.SearchFilterResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface SearchFilterApi {

    @POST("graphql")
    fun search(@Body body: GraphqlBody): Observable<GraphQLResponse<SearchFilterResponse>>

    @POST("graphql")
    fun getSubcategories(@Body body: GraphqlQuery): Observable<GraphQLResponse<ResponseSubcategories>>
}