package br.com.goin.feature.configuration

import android.util.Log
import br.com.goin.base.BaseApplication
import br.com.goin.component.push.UserPushEndpointInteractor
import br.com.goin.component.session.user.UserModel
import br.com.goin.component.session.user.UserSessionInteractor

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class ConfigPresenterImpl(val view: ConfigView) : ConfigPresenter {

    private val userInteractor = UserSessionInteractor.instance
    private val pushEndpointManager = UserPushEndpointInteractor.instance

    private val disposables = CompositeDisposable()

    override fun onCreate() {
        val user = userInteractor.fetchUser()
        user?.let {
            view.configureProfile(it)
        }

        view.configureOnCLickListener()
    }

    override fun checkFromFacebook(identityProvider: String) {
       val token =  userInteractor.fetchToken()?.identityProvider
        token?.let {
            if (it == identityProvider){
                view.hideChangePassword()
            }
        }

    }

    override fun onResume() {
        checkUserLoggedIn()
    }

    override fun onDestroy(){
        disposables.dispose()
    }

    override fun onClickInviteFriend() {
        val user = userInteractor.fetchUser()
        user?.let {
            view.onClickInvitefriend(it)
        }
    }

    override fun updateUser(updateUser: UserModel) {
        val user = userInteractor.fetchUser()
        user?.let {
            it.email = updateUser.email
            it.name = updateUser.name
            it.profilePicture = updateUser.profilePicture
            userInteractor.save(it)
        }
    }

    override fun clearPushNotification() {
        pushEndpointManager.updateMessagingToken(token = null)
                .subscribe({}, {throwable ->
                    Log.e("ConfigFragment", throwable.message, throwable)
                }).addTo(disposables)
    }

    private fun checkUserLoggedIn(){
        when(userInteractor.isLoggedIn()){
            true -> {
                configureProfile()
                view.configureLoggedIn()
            }
            else -> view.configureLoggedOff()
        }
    }

    private fun configureProfile() {
        val user = userInteractor.fetchUser()
        user?.let {
            view.configureProfile(it)
        }
    }
}