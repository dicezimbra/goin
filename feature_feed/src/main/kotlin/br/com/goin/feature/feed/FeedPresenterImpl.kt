package br.com.goin.feature.feed

import android.util.Log
import androidx.work.State
import androidx.work.WorkStatus
import br.com.goin.base.extensions.clearCache
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.extensions.useCache
import br.com.goin.component.session.user.UserSessionInteractor
import br.com.goin.feature.feed.model.Feed
import br.com.goin.feature.feed.model.Post
import br.com.goin.feature.feed.model.PostInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class FeedPresenterImpl(val view: FeedView) : FeedPresenter {

    private val postInteractor = PostInteractor.instance
    private val userSessionInteractor = UserSessionInteractor.instance

    private val disposable = CompositeDisposable()

    private var profileId: String? = null
    private var feed: Feed? = null
    private var posts: MutableList<Post> = arrayListOf()

    override fun onCreate() {
        val user = userSessionInteractor.fetchUser()

        user?.let {
            view.configureUserAvatar(it.profilePicture)
        }

        view.configureOnClickListener()
        view.configureRecyclerview(user?.id ?: "", profileId != null)
        fetchFeed(null)
    }

    override fun onReceivedProfileId(profileId: String?) {
        this.profileId = profileId
    }

    override fun loadFeedByTask() {
        val lastPostId = null
        clearCache("FEED$lastPostId$profileId")
        fetchFeed(lastPostId)
    }

    override fun logScreenName() {
        if(profileId == null){
            view.logScreenName()
        }
    }

    override fun swipFetchFeed() {
        fetchFeed(null)
    }

    override fun onDestroy() {
        disposable.dispose()
    }

    override fun onCommentCreated(postId: String?, comment: String?, commentAuthor: String?) {
        posts.findLast { it.postId == postId }?.apply {
            this.comment = comment
            this.commentAuthor = commentAuthor
            this.commentsCount?.plus(1)
        }

        view.updatePosts()
    }

    override fun loadMorePosts() {
        feed?.let {
            if (it.morePostsToGet) {
                fetchFeed(it.lastPostId)
            }
        }
    }

    private fun fetchFeed(lastPostId: String?) {
        postInteractor.fetchFeed(profileId, lastPostId)
                .useCache("FEED$lastPostId$profileId", Feed::class.java)
                .ioThread()
                .doOnSubscribe { if (feed == null) view.showLoading() }
                .subscribe({ feed ->
                    if (feed.post?.isEmpty() == true) {
                        view.configureEmptyFeed()
                        view.hideLoading()
                    } else {
                        if(lastPostId == null) this.posts.clear()
                        feed.post?.let { this.posts.addAll(it) }

                        view.configurePosts(this.posts)
                    }

                    view.hideLoading()
                }, { t: Throwable ->
                    view.handleError(t)
                    Log.e("FeedPresenter", t.message, t)
                }).addTo(disposable)
    }

    override fun onLikeClicked(post: Post) {
        if (post.likedByMe) {
            postInteractor.like(post.postId)
                    .ioThread()
                    .subscribe({}, { t: Throwable -> Log.e("FeedPresenter", t.message, t) })
                    .addTo(disposable)
        } else {
            postInteractor.dislike(post.postId)
                    .ioThread()
                    .subscribe({}, { t: Throwable -> Log.e("FeedPresenter", t.message, t) })
                    .addTo(disposable)
        }
    }

    override fun deletePost(postId: String?) {
        postId?.let {
            postInteractor.delete(it)
                    .ioThread()
                    .subscribe({}, { t: Throwable ->
                        Log.e("FeedPresenter", t.message, t)
                    }).addTo(disposable)
        }
    }

    override fun reportPost(postId: String?) {
        postId?.let {
            postInteractor.report(it)
                    .ioThread()
                    .subscribe({
                        view.showSuccessMessage()
                    }, { t: Throwable ->
                        Log.e("FeedPresenter", t.message, t)
                    }).addTo(disposable)
        }
    }


    override fun onBackgroundTasks(it: List<WorkStatus>) {
        it.let { status ->
            if (status.isNotEmpty()) {
                val hasSucceeded = status.none {
                    it.state != State.SUCCEEDED
                }
                if (hasSucceeded) {
                    view.onSucceededTask()
                } else {
                    view.onRunningTask()
                }
                Log.w("FeedWorkManager", status.toString())
            }
        }
    }

}