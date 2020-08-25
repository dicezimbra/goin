package br.com.goin.component.model.category

import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface CategoriesApi {

    @POST("graphql")
    fun getCategories(@Body body: GraphqlBody): Observable<GraphQLResponse<CategoryResponse>>
}