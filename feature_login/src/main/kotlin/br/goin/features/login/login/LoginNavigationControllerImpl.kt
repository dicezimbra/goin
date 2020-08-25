package br.goin.features.login.login

import android.content.Context
import br.com.goin.component.navigation.LoginNavigationController
import br.goin.features.login.login.preview.LoginPreviewActivity
import br.goin.features.login.manager.LoginManager
import br.goin.features.login.password.forgotpassword.ForgotPasswordActivity

class LoginNavigationControllerImpl : LoginNavigationController {

    override fun goToLogin(context: Context, listener: () -> Unit) {
        LoginManager.instance.makeLogin(context, listener)
    }

    override fun goToForgotPassword(context: Context, fromApp: Boolean) {
        ForgotPasswordActivity.starter(context, fromApp)
    }
}