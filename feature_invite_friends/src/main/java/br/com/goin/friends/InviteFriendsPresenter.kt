package br.com.goin.friends

import android.os.Bundle
import br.com.goin.component.session.user.UserModel
import br.com.goin.friends.model.FriendToInvite

interface InviteFriendsPresenter {

    fun onCreate()
    fun onRefresh()
    fun onDestroy()
    fun onReceivedEventId(eventId: String?)
    fun inviteFriend(friend: FriendToInvite)
}