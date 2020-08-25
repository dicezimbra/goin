package br.goin.features.login.manager

import android.content.Context
import android.content.Intent
import br.com.goin.component.session.user.UserSessionInteractor
import br.goin.features.login.login.LoginActivity
import br.goin.features.login.login.preview.LoginPreviewActivity

class LoginManager {

    private var listenerToInvoke: (() -> Unit)? = null
    private val userSessionManager = UserSessionInteractor.instance

    var FROM_APP_KEY = "fromApp"
    companion object {
        val instance: LoginManager by lazy { LoginManager() }
    }

    fun makeLogin(context: Context, listener: () -> Unit) {
        listenerToInvoke = listener

        when(userSessionManager.isLoggedIn()){
            true -> { listenerToInvoke?.invoke() }
            false -> { openLoginActivity(context) }
        }
    }

    private fun openLoginActivity(context: Context) {
        LoginPreviewActivity.starter(context, true)
    }

    fun isLoggedIn() : Boolean {
       return userSessionManager.isLoggedIn()
    }

    fun loginComplete(){
        listenerToInvoke?.invoke()
    }
}