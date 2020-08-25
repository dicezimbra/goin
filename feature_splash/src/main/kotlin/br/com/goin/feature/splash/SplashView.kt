package br.com.goin.feature.splash

import br.com.goin.component.session.user.UserModel

interface SplashView {
    fun optionalUpdate()
    fun forceUpdate()
    fun showLoading()
    fun hideLoading()
    fun goToNextActivity(userModel: UserModel?)
}