package br.com.goin.feature.configuration

import br.com.goin.component.session.user.UserModel

interface ConfigView {
    fun configureOnCLickListener()
    fun configureProfile(userModel: UserModel)
    fun onClickInvitefriend(userModel: UserModel)
    fun configureLoggedOff()
    fun configureLoggedIn()
    fun hideChangePassword()
}