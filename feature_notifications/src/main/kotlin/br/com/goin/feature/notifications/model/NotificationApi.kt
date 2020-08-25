package br.com.goin.feature.notifications.model

import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationApi {

    @POST("graphql")
    fun fetch(@Body body: GraphqlBody): Observable<GraphQLResponse<NotificationResponse>>

    @POST("graphql")
    fun follow(@Body body: GraphqlBody): Completable

    @POST("graphql")
    fun unfollow(@Body body: GraphqlBody): Completable
}