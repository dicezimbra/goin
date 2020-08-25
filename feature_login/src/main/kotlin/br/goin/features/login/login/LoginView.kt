package br.goin.features.login.login

interface LoginView {
    fun setupViews()
    fun saveEmailOnSharedPreferences()
    fun setEmailFromSharedPreferences()
    fun savePasswordOnSharedPreferences(passwordEncrypted: String)
    fun getPasswordFromSharedPreferences(): String?
    fun goToForgotPassword()
    fun goToChooseProfilePicture()
    fun goToTabsMain()
    fun hideLoading()
    fun showLoading()
    fun validateEmail()
    fun notValidEmail()
    fun passwordNotValid()
    fun validatePassword()
    fun validPassField()
    fun invalidPassField()
    fun validEmail()
    fun showDialogOnError(invalid_user: Int)
}