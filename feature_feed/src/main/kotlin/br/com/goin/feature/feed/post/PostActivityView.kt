package br.com.goin.feature.feed.post

import br.com.goin.component.session.user.UserModel
import br.com.goin.feature.feed.model.checkIn.CheckIn
import br.com.goin.feature.feed.model.friends.PostUser

interface PostActivityView {
    fun configHideKeyboard()
    fun loadUserPicture(it: UserModel)
    fun loadUserName(it: UserModel)
    fun configureOnClickListeners()
    fun openFeelings()
    fun openCheckIn()
    fun setFeelings(feelings: String?, eventCheckedIn: CheckIn?)
    fun photoSelected()
    fun videoSelected()
    fun setupTransaction()
    fun setupBroadcastReceiver()
    fun configurePostQuery()
    fun onFriendsLoaded(friends: MutableList<PostUser>)
    fun sendPost()
    fun currentTime(): Long
    fun onSuccessPost(createPost: CreatePost)
    fun setupIntents()
    fun registerReceiver()
}