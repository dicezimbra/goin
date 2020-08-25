package br.com.goin.feature.profile

import br.com.goin.component.session.user.ActionType
import br.com.goin.component.session.user.UserModel

interface ProfileView {
    fun setupClickListeners(currentUserId: String)
    fun handleError(throwable: Throwable)
    fun configurePostCount(postCount: Int?)
    fun configureProfile(user: UserModel)
    fun configureFollowing(followingCount: Int?)
    fun configureFollowers(followersCount: Int?)
    fun configureViewPager(userId: String)
    fun configureInviteButton(followByMe: Boolean)
    fun configureToolbarForCurrentUser()
    fun configureToolbarForOtherUsers()
    fun showLoading()
    fun hideLoading()
    fun configureScore(scoreCount: Int?)
    fun logMyProfileScreenName()
    fun logProfileScreenName()
}