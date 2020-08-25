package br.com.goin.feature.change.password

interface ChangePasswordPresenter {
    fun onCreate()
    fun onClickSend(oldPassword: String, password: String, passwordConfirmation: String)
    fun onDestroy()
    fun logOnAnalytics(action: String, label: String? = null)
}