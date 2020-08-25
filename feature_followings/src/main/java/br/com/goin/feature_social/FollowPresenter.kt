package br.com.goin.feature_social


interface FollowPresenter {
    fun onCreate()
    fun loadUserModels(userId: String?, relation: FollowRelation)
    fun followUser(userId: String?)
    fun unfollowUser(userId: String?)
    fun onDestroy()
    fun logOnAnalytics(screenName: String, category: String, action: String, label: String? = null)
}