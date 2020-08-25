package br.com.goin.features.password.newpassword

interface NewPasswordView {
    fun configureOnClickListeners()
    fun configureMatchPasswords()
    fun showErrorMessageCapitalize()
    fun showCorretPasswordMessageCapitalize()
    fun hideMessageCapitalize()
    fun showErrorPasswordsDontMatch()
    fun hideErrorPasswordsDontMatch()
    fun showLoading()
    fun hideLoading()
    fun resetPasswordSuccess()
    fun sendToSignUp()
    fun callIntent()
    fun getEmail(email: String)
}