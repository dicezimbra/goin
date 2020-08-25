package br.com.goin.feature_theater

interface TheaterDetailPresenter {

    fun onCreate(clubId: String)
    fun onDestroy()
    fun onClickShare()
    fun onInviteClicked()
    fun onMapClicked()
    fun onUberClicked()
}