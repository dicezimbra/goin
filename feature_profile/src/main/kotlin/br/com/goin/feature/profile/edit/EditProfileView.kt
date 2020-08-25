package br.com.goin.feature.profile.edit

import br.com.goin.component.session.user.UserModel

interface EditProfileView {
    fun configureToolbar()
    fun loadUserInfo(it: UserModel)
    fun onSuccessUpdate()
    fun configureClickListeners()
    fun showLoading()
    fun showError(e: Throwable?)
    fun showMessageError(id: Int)
}