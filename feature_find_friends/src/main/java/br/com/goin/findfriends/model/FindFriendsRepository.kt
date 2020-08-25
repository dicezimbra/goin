package br.com.goin.findfriends.model

import br.com.goin.base.BuildConfig
import br.com.goin.base.dtos.GraphqlQuery
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.component.session.user.UserModel
import io.reactivex.Completable
import io.reactivex.Observable

class FindFriendsRepository {

    private var service = RetrofitService(FindFriendsApi::class.java, BuildConfig.BASE_URL)

    fun fetchSearchFriends(searchText: String): Observable<List<UserModel>> {
        val graphqlQuery = GraphqlQuery.builder(FindFriendsQuery.SEARCH_USER)
                .`var`("query", searchText)
                .build()

        return service.apiService.getSearchFriendsList(graphqlQuery).map {
            FindFriendsMapper.mapToModel(it.data?.searchUsers)
        }
    }

    fun followUser(userId: String?): Completable {
        val graphqlQuery = GraphqlQuery
                .builder(FindFriendsQuery.FOLLOW_USER)
                .`var`("currentUserId", userId)
                .build()

        return service.apiService.followUser(graphqlQuery)
    }

    fun unfollowUser(userId: String?): Completable {
        val graphqlQuery = GraphqlQuery
                .builder(FindFriendsQuery.UNFOLLOW_USER)
                .`var`("currentUserId", userId)
                .build()

        return service.apiService.followUser(graphqlQuery)
    }

}
