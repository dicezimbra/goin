package br.com.goin.findfriends

import br.com.goin.component.session.user.UserModel

interface FindFriendsView {

    fun setupToolbar()
    fun configureRecyclerView()
    fun configureFriendsList(friendsList: List<UserModel>)
    fun showLoading()
    fun hideLoading()
    fun showSnackBarOnError()
    fun configureSearch()
    fun showEmptyMessage()
    fun showFriendsList()
    fun changeUserStatus(userId: String?, followStatus: Boolean)
}
