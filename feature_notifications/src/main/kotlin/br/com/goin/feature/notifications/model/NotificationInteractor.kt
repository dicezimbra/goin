package br.com.goin.feature.notifications.model

import io.reactivex.Completable
import io.reactivex.Observable

interface NotificationInteractor{

    companion object {
        val instance: NotificationInteractor by lazy { NotificationInteractorImpl() }
    }

    fun fetch(lastId: String?): Observable<List<Notification>>
    fun followUser(userId: String): Completable
    fun unfollowUser(userId: String): Completable
}