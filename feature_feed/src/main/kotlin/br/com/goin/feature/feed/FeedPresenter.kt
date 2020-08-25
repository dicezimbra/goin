package br.com.goin.feature.feed

import androidx.work.WorkStatus
import br.com.goin.feature.feed.model.Post

interface FeedPresenter {
    fun onCreate()
    fun onDestroy()
    fun reportPost(postId: String?)
    fun deletePost(postId: String?)
    fun onLikeClicked(post: Post)
    fun loadMorePosts()
    fun onCommentCreated(postId: String?, comment: String?, commentAuthor: String?)
    fun onBackgroundTasks(it: List<WorkStatus>)
    fun loadFeedByTask()
    fun swipFetchFeed()
    fun onReceivedProfileId(profileId: String?)
    fun logScreenName()
}