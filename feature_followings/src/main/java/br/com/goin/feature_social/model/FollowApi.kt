package br.com.goin.feature_social.model

import br.com.goin.base.dtos.GraphqlQuery
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.HashMap

interface FollowApi {
    @POST("graphql")
    fun getFollowers(@Body body: GraphqlQuery): Observable<GraphQLResponse<HashMap<String, List<UserCardDetailsResponse>>>>

    @POST("graphql")
    fun followUser(@Body body: GraphqlQuery): Completable

    @POST("graphql")
    fun unfollowUser(@Body body: GraphqlQuery): Completable
}