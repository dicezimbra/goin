package br.com.goin.friends.model

import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface InvinteFriendsApi {

    @POST("graphql")
    fun getFollowers(@Body body: GraphqlBody): Observable<GraphQLResponse<HashMap<String,List<FriendToInvite>>>>

    @POST("graphql")
    fun inviteFriend(@Body body: GraphqlBody): Observable<GraphQLResponse<InviteToEventResponse>>
}
