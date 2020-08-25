package br.goin.features.login.login.preview

interface LoginPreviewPresenter {
    fun onCreate()
    fun onFacebookLogged(accessToken: String, identityProvider: String)
}