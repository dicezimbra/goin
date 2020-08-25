package br.com.goin.feature.profile

interface ProfilePresenter {
    fun onDestroy()
    fun onCreate()
    fun loadProfile()
    fun logOnAnalytics(action: String, label: String? = null)
    fun onReceiveProfileId(profileId: String?)
    fun onInviteClick()
    fun logScreenName()
}