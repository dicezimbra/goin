package br.com.goin.feature.feed.post

import android.graphics.Bitmap
import br.com.goin.component.session.user.UserModel
import br.com.goin.feature.feed.model.checkIn.CheckIn
import br.com.goin.feature.feed.model.friends.SearchFriendsResponse

interface PostActivityPresenter {
    fun onCreate()
    fun onReceiveUser(user: UserModel?)
    fun onReceiveFeelings(feeling: String?)
    fun onReceiveEventCheckedIn(event: CheckIn?)
    fun searchFriends(queryFriends: String)
    fun onReceiveFriends(data: SearchFriendsResponse?)
    fun onReceiveEvent(event: Any?)

}