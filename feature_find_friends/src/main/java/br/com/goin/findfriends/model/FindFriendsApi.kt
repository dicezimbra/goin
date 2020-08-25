package br.com.goin.findfriends.model

import br.com.goin.base.dtos.GraphqlQuery
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface FindFriendsApi {

    @POST("graphql")
    fun getSearchFriendsList(@Body body: GraphqlQuery): Observable<GraphQLResponse<FindFriendsResponse>>

    @POST("graphql")
    fun followUser(@Body body: GraphqlQuery): Completable

    @POST("graphql")
    fun unfollowUser(@Body body: GraphqlQuery): Completable

}
