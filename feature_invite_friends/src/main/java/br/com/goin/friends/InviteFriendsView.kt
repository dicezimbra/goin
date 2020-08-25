package br.com.goin.friends

import android.os.Bundle
import br.com.goin.component.session.user.FriendStatus
import br.com.goin.component.session.user.UserModel
import br.com.goin.friends.model.FriendToInvite

interface InviteFriendsView {

    fun configureEmptyView()
    fun inviteFrendsSuccess()
    fun showLoading()
    fun hideLoading()
    fun configureToolbar()
    fun receiveFriendsToInvite(userList: MutableList<FriendToInvite>)
    fun configureRecyclerView()
    fun updateInviteFriendItem()
    fun configureSearchView()
}