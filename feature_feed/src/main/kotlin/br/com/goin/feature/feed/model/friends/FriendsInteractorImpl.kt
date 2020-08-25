package br.com.goin.feature.feed.model.friends

import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import io.reactivex.Observable

class FriendsInteractorImpl : FriendsInteractor {

    private val repository = FriendsRepository()

    override fun searchFriends(queryFriends: String): Observable<GraphQLResponse<SearchFriendsResponse>> {
        return repository.searchFriends(queryFriends)
    }

}