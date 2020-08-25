package br.com.goin.component.push

import io.reactivex.Completable

interface UserPushEndpointInteractor {

    companion object {
        var instance: UserPushEndpointInteractor = UserPushEndpointInteractorImpl()
    }

    fun updateMessagingToken(token: String?): Completable
    fun fetchPushToken(): String?
}
