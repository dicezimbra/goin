package br.com.goin.feature.profile.myinterests

interface MyInterestPresenter {
    fun onCreate()
    fun onReceivedProfileId(profileId: String?)
}