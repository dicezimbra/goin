package br.goin.features.login.signUp

interface SignUpPresenter {
    fun onCreate()
    fun onSignUp(email: String?, password: String?, passwordConfirmation: String?, name: String?, emailValid: Boolean, consentToUseTerms: Boolean)
    fun onDestroy()
}