package br.com.goin.friends.model

import br.com.goin.base.BuildConfig
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import io.reactivex.Observable

class InviteFriendsRepository {

    val service = RetrofitService(InvinteFriendsApi::class.java, BuildConfig.BASE_URL)

    fun getFriendsToInvite(eventId: String): Observable<List<FriendToInvite>> {
        val graphqlQuery = GraphqlBody.Builder(InviteFriendsQueries.GET_FRIENDS_TO_INVITE)
                .`var`("eventId", eventId)
                .build()

        return service.apiService.getFollowers(graphqlQuery).map { it.data?.get("friendsToInvite") }
    }

    fun inviteFriend(eventId: String, invitedId: String): Observable<GraphQLResponse<InviteToEventResponse>> {
        val graphqlQuery = GraphqlBody.Builder(InviteFriendsQueries.INVITE_FRIEND)
                .`var`("eventId", eventId)
                .`var`("invitedId", invitedId)
                .build()

        return service.apiService.inviteFriend(graphqlQuery)
    }
}