package br.goin.features.login.login

interface LoginPresenter {
    fun onCreate()
    fun onStart()
    fun onSignIn(email: String?, password: String?, emailValid: Boolean)
    fun onDestroy()
    fun onFacebookLogged(accessToken: String, identityProvider: String)
    fun verifyIfIsValid(s: String?)
     fun verifyPassword(pass: String)
}