package br.com.goin.feature.home

interface HomeView {
    fun configureOnClickListeners()
    fun goToProfile()
    fun goToFeed()
    fun goToConfig()
    fun goToHome()
    fun configureTabProfile()
    fun configureTabHome()
    fun configureTabFeed()
    fun configureTabConfig()
    fun goToWallet()
    fun configureTabWallet()
    fun goToPlaceholder(isWallet: Boolean)
}