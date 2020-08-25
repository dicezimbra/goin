package br.com.goin.features.password.forgotpassword

interface ForgotPasswordPresenter {
    fun onCreate()
    fun forgotPassword(email: String)
    fun onDestroy()
    fun verifyIfEmaildValid(email: String)
}