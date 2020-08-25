package br.com.goin.feature.change.password

interface ChangePasswordView {

    fun changePasswordError(error: String?)
    fun showLoading()
    fun hideLoading()
    fun isValidPassword(isValid: Boolean, typeMessage: ChangePasswordPresenterImpl.PasswordError)
    fun configureOnClickListener()
    fun showAlertOldPasswordEqualNewPassword()
    fun changePasswordSuccess()
}