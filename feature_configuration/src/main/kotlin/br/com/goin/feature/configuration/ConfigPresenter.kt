package br.com.goin.feature.configuration

import br.com.goin.component.session.user.UserModel

interface ConfigPresenter {
    fun onCreate()
    fun onClickInviteFriend()
    fun updateUser(updateUser: UserModel)
    fun onResume()
    fun clearPushNotification()
    fun onDestroy()
    fun checkFromFacebook(identityProvider: String)
}