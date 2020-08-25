package br.com.goin.model.categories

import br.com.goin.base.GraphqlQuery
import br.com.goin.base.OutsmartResponse
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.feature_all_categories.model.CategoriesList
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface CategoriesApi {

    @POST("graphql")
    fun getCategories(@Body body: GraphqlQuery): Observable<GraphQLResponse<CategoriesList>>
}