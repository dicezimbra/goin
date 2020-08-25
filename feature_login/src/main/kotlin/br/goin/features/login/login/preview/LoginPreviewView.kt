package br.goin.features.login.login.preview

import br.com.goin.component.session.user.UserModel

interface LoginPreviewView {

    fun onConfigureClickListeners()
    fun facebookSignUp()
    fun googleSignUp()
    fun signIn()
    fun signUp()
    fun saveEmailOnSharedPreferences(userModel: UserModel)
    fun goToTabsMain()
    fun showDialogOnError(code: Int)
}