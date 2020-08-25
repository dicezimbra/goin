package br.com.goin.feature.feed.model.friends

import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import io.reactivex.Observable

interface FriendsInteractor {

    companion object {
        val instance: FriendsInteractor by lazy { FriendsInteractorImpl() }
    }

    fun searchFriends(queryFriends: String): Observable<GraphQLResponse<SearchFriendsResponse>>
}