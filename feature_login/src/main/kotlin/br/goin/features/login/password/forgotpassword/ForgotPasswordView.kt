package br.goin.features.login.password.forgotpassword

interface ForgotPasswordView {

    fun forgotPasswordSuccess()
    fun showLoading()
    fun hideLoading()
    fun configureOnClickListener()
    fun showErrorEmailNotFound()
    fun configureTextChanges()
    fun emailNotValid()
    fun emailIsValid()
    fun sendToPin()
}