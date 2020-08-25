package br.com.goin.feature.feed

import br.com.goin.feature.feed.model.Post


interface FeedView {
    fun configureEmptyFeed()
    fun showLoading()
    fun hideLoading()
    fun configurePosts(posts: List<Post>)
    fun configureUserAvatar(profilePicture: String)
    fun configureOnClickListener()
    fun configureRecyclerview(userId: String, isProfile: Boolean)
    fun updatePosts()
    fun handleError(throwable: Throwable)
    fun onSucceededTask()
    fun onRunningTask()
    fun showSuccessMessage()
    fun logScreenName()
}