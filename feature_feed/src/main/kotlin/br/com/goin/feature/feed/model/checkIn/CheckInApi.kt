package br.com.goin.feature.feed.model.checkIn

import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface CheckInApi {

    @POST("graphql")
    fun eventsToCheckin(@Body body: GraphqlBody): Observable<GraphQLResponse<CheckInResponse>>

}