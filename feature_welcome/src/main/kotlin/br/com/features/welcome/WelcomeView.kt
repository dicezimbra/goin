package br.com.features.welcome

interface WelcomeView {
    fun configureAdapter()
    fun onBackPressedButton()
    fun goToLogIn()
    fun goToHome()
}