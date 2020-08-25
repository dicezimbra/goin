package br.com.goin.feature.splash.model

import br.com.goin.base.dtos.GraphqlQuery
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST


interface UpdateApi {
    @POST("graphql")
    fun hasUpdates(@Body graphqlQuery: GraphqlQuery): Observable<GraphQLResponse<QueryResponse>>
}
