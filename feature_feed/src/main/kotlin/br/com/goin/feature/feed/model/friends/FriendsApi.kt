package br.com.goin.feature.feed.model.friends

import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface FriendsApi {

    @POST("graphql")
    fun searchFriends(@Body body: GraphqlBody): Observable<GraphQLResponse<SearchFriendsResponse>>

}