package br.com.goin.feature.notifications.model

import io.reactivex.Completable
import io.reactivex.Observable

class NotificationInteractorImpl: NotificationInteractor{

    val repository = NotificationRepository()

    override fun fetch(lastId: String?): Observable<List<Notification>> {
        return repository.fetch(lastId)
    }

    override fun followUser(userId: String): Completable {
        return repository.followUser(userId)
    }

    override fun unfollowUser(userId: String): Completable {
        return repository.unfollowUser(userId)
    }
}