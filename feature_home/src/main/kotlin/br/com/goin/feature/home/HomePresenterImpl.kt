package br.com.goin.feature.home

import br.com.goin.component.session.user.UserSessionInteractor

class HomePresenterImpl(val view: HomeView) : HomePresenter {

    private val userSessionInteractor = UserSessionInteractor.instance
    private var deepLink: String? = null

    override fun onCreate() {
        view.goToHome()
        view.configureTabHome()
        view.configureOnClickListeners()

        when(deepLink){
            "WALLET" -> {
                onClickTabWallet()
            }
            else -> {}
        }
    }

    override fun onReceiveDeepLink(any: Any?) {
        this.deepLink = any as String
    }

    override fun onClickTabConfig() {
        view.goToConfig()
        view.configureTabConfig()
    }

    override fun onClickTabWallet() {
        if(userSessionInteractor.isLoggedIn()) {
            view.goToWallet()
            view.configureTabWallet()
        }else{
            view.goToPlaceholder(true)
        }
    }

    override fun onClickTabFeed() {
        if(userSessionInteractor.isLoggedIn()) {
            view.goToFeed()
            view.configureTabFeed()
        }else{
            view.goToPlaceholder(false)
        }
    }

    override fun onClickTabHome() {
        view.goToHome()
        view.configureTabHome()
    }

    override fun onClickTabProfile() {
        if(userSessionInteractor.isLoggedIn()) {
            view.goToProfile()
            view.configureTabProfile()
        }else{
            view.goToPlaceholder(false)
        }
    }
}