package br.com.features.welcome

class WelcomePresenterImpl(val view: WelcomeView) : WelcomePresenter {

    override fun onCreate() {
        view.configureAdapter()
    }

    override fun startHome() {
        view.goToHome()
    }

    override fun startLogIn() {
        view.goToLogIn()
    }
}