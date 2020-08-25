package br.com.goin.feature_social.model

import br.com.goin.component.session.user.UserModel
import br.com.goin.feature_social.FollowRelation
import io.reactivex.Completable
import io.reactivex.Observable

interface FollowInteractor {
    companion object {
        val instance: FollowInteractor = FollowInteractorImpl()
    }

    fun requestFollowersList(userId: String?, type: FollowRelation): Observable<List<UserModel>>
    fun followUser(userId: String?): Completable
    fun unfollowUser(userId: String?): Completable
}
