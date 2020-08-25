package br.com.goin.feature_theater.model

import br.com.goin.base.GraphqlQuery
import br.com.goin.base.OutsmartResponse
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface TheaterApi {

    @POST("graphql")
    fun sessionsByClub(@Body query: GraphqlQuery): Observable<GraphQLResponse<TheaterResponse>>
    @POST("graphql")
    fun getClub(@Body query: GraphqlQuery): Observable<GraphQLResponse<ClubResponse>>
}