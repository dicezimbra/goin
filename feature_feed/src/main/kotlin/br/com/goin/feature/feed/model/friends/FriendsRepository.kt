package br.com.goin.feature.feed.model.friends

import br.com.goin.base.BuildConfig
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import io.reactivex.Observable

class FriendsRepository {

    private val service = RetrofitService(FriendsApi::class.java, BuildConfig.BASE_URL)

    fun searchFriends(searchQuery: String): Observable<GraphQLResponse<SearchFriendsResponse>> {
        val graphqlQuery = GraphqlBody.Builder(FriendsQueries.SEARCH_FRIENDS)
                .`var`("query", searchQuery)
                .build()

        return service.apiService.searchFriends(graphqlQuery)
    }


}