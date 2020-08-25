package br.com.goin.feature.home

interface HomePresenter {
    fun onCreate()
    fun onClickTabConfig()
    fun onClickTabHome()
    fun onClickTabWallet()
    fun onClickTabFeed()
    fun onClickTabProfile()
    fun onReceiveDeepLink(any: Any?)
}