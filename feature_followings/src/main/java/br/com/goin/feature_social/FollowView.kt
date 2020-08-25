package br.com.goin.feature_social

import br.com.goin.component.session.user.UserModel

interface FollowView {
    fun setupToolbar()
    fun configureRecyclerView()
    fun setupEmptyListMessage()
    fun configureUserList(list: List<UserModel>)
    fun changeUserStatus(userId: String?, followStatus: Boolean)
    fun showLoading()
    fun hideLoading()
    fun showSnackBarOnError()
}