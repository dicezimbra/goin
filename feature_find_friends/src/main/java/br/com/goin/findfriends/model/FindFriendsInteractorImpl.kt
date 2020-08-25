package br.com.goin.findfriends.model

import br.com.goin.component.session.user.UserModel
import io.reactivex.Completable
import io.reactivex.Observable

class FindFriendsInteractorImpl : FindFriendsInteractor {

    private val repository = FindFriendsRepository()

    override fun requestFollowersList(searchText: String): Observable<List<UserModel>> {
        return repository.fetchSearchFriends(searchText)
    }

    override fun followUser(userId: String?): Completable {
        return repository.followUser(userId)
    }

    override fun unfollowUser(userId: String?): Completable {
        return repository.unfollowUser(userId)
    }

}
