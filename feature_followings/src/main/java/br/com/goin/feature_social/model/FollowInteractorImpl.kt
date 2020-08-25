package br.com.goin.feature_social.model

import br.com.goin.component.session.user.UserModel
import br.com.goin.feature_social.FollowRelation
import io.reactivex.Completable
import io.reactivex.Observable

class FollowInteractorImpl : FollowInteractor {
    private val repository = FollowRepository()

    override fun requestFollowersList(userId: String?, type: FollowRelation): Observable<List<UserModel>> {
        return repository.fetchSocialRelation(userId, type)
    }

    override fun followUser(userId: String?): Completable {
        return repository.followUser(userId)
    }

    override fun unfollowUser(userId: String?): Completable {
        return repository.unfollowUser(userId)
    }
}