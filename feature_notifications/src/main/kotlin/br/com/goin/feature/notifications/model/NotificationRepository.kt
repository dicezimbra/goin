package br.com.goin.feature.notifications.model

import br.com.goin.base.BuildConfig
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import io.reactivex.Completable
import io.reactivex.Observable

class NotificationRepository{

    var service = RetrofitService(NotificationApi::class.java, BuildConfig.BASE_URL)

    fun fetch(lastId: String?): Observable<List<Notification>> {
        val body = GraphqlBody.Builder(NotificationsQueries.GET_NOTIFICATIONS)
                .`var`("lastId", lastId)
                .`var`("count", null)
                .build()

       return service.apiService.fetch(body).map { it.data?.notification }
    }

    fun followUser(userId: String): Completable {
        val body = GraphqlBody.Builder(NotificationsQueries.FOLLOW_USER)
                .`var`("userId", userId)
                .build()

        return service.apiService.follow(body = body)
    }

    fun unfollowUser(userId: String): Completable {
        val body = GraphqlBody.Builder(NotificationsQueries.UNFOLLOW_USER)
                .`var`("userId", userId)
                .build()

        return service.apiService.follow(body = body)
    }
}