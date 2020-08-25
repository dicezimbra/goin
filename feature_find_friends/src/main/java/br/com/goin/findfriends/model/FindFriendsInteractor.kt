package br.com.goin.findfriends.model

import br.com.goin.component.session.user.UserModel
import io.reactivex.Completable
import io.reactivex.Observable

interface FindFriendsInteractor {

    companion object {
        val instance: FindFriendsInteractor = FindFriendsInteractorImpl()
    }

    fun requestFollowersList(searchText: String): Observable<List<UserModel>>
    fun followUser(userId: String?): Completable
    fun unfollowUser(userId: String?): Completable
}
