package br.com.goin.feature_social.model

import br.com.goin.base.BuildConfig
import br.com.goin.base.dtos.GraphqlQuery
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.component.session.user.UserModel
import br.com.goin.feature_social.FollowRelation
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

class FollowRepository {

    private var service = RetrofitService(FollowApi::class.java, BuildConfig.BASE_URL)

    enum class ListType {
        Following, Followers, Friends
    }

    private data class FollowType(
            var specialKey: String? = null,
            var listType: ListType? = null,
            var query: String? = null
    )

    fun fetchSocialRelation(userId: String?, followRelation: FollowRelation): Observable<List<UserModel>> {
        val followType = getFollowType(userId, followRelation)
        val graphqlQuery = GraphqlQuery.builder(followType.query).apply {
            if (userId != null) {
                this.`var`("userId", userId)
            }
        }.build()

        return service.apiService.getFollowers(graphqlQuery).map { it.data }.map {
            FollowMapper.mapToModel(it[followType.specialKey], followType.listType) }
    }

    fun followUser(userId: String?): Completable {
        val graphqlQuery = GraphqlQuery
                .builder(FollowQueries.FOLLOW_USER)
                .`var`("userId", userId)
                .build()

        return service.apiService.followUser(graphqlQuery)
    }

    fun unfollowUser(userId: String?): Completable {
        val graphqlQuery = GraphqlQuery
                .builder(FollowQueries.UNFOLLOW_USER)
                .`var`("userId", userId)
                .build()

        return service.apiService.followUser(graphqlQuery)
    }

    private fun getFollowType(userId: String?, type: FollowRelation): FollowType {
        val followType = FollowType()

        when (type) {
            FollowRelation.FOLLOWERS -> {
                followType.specialKey = "getUserFollowers"
                followType.listType = ListType.Followers
                followType.query = if (userId != null) {
                    FollowQueries.GET_USER_FOLLOWERS
                } else {
                    FollowQueries.GET_CURRENT_USER_FOLLOWERS
                }

            }
            FollowRelation.FOLLOWING -> {
                followType.specialKey = "getUserFollowing"
                followType.listType = ListType.Following
                followType.query = if (userId != null) {
                    FollowQueries.GET_USER_FOLLOWING
                } else {
                    FollowQueries.GET_CURRENT_USER_FOLLOWING
                }
            }
        }

        return followType
    }
}