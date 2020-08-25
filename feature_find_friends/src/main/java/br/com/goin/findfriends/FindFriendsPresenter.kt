package br.com.goin.findfriends

interface FindFriendsPresenter {
    fun onCreate()
    fun followUser(userId: String?)
    fun unfollowUser(userId: String?)
    fun onDestroy()
    fun logOnAnalytics(screenName: String, category: String, action: String, label: String? = null)
    fun onSearchFriend(searchInput: String)
}
