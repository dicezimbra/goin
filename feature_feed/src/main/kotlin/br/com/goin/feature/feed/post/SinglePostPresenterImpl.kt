package br.com.goin.feature.feed.post

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.component.session.user.UserSessionInteractor
import br.com.goin.feature.feed.model.Post
import br.com.goin.feature.feed.model.PostInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.io.Serializable

class SinglePostPresenterImpl(val view: SinglePostView): SinglePostPresenter{

    private var post: Post? = null
    private val postInteractor = PostInteractor.instance
    private val userSessionInteractor = UserSessionInteractor.instance
    private val disposable = CompositeDisposable()

    override fun onCreate(){
        post?.let {
            view.configurePost(it)
            view.configureSharedPost(it)

            val userId = userSessionInteractor.fetchUser()?.id ?: ""
            view.configureOnClick(it, userId)
        }
    }

    override fun onDestroy(){
        disposable.dispose()
    }

    override fun onReceivePost(serializable: Serializable){
        this.post = serializable as? Post
    }

    override fun onLikeClicked() {
        post?.let { post ->
            if(post.likedByMe){
                postInteractor.like(post.postId)
                        .ioThread()
                        .subscribe({}, {t: Throwable ->
                            Log.e("FeedPresenter", t.message, t)
                        }).addTo(disposable)
            }else{
                postInteractor.dislike(post.postId)
                        .ioThread()
                        .subscribe({}, {t: Throwable ->
                            Log.e("FeedPresenter", t.message, t)
                        }).addTo(disposable)
            }
        }
    }

    override fun deletePost(){
        post?.postId?.let {
            postInteractor.delete(it)
                    .ioThread()
                    .subscribe({}, {t: Throwable ->
                        Log.e("FeedPresenter", t.message, t)
                    }).addTo(disposable)
        }
    }

    override fun reportPost(){
        post?.postId?.let {
            postInteractor.report(it)
                    .ioThread()
                    .subscribe({}, {t: Throwable ->
                        Log.e("FeedPresenter", t.message, t)
                    }).addTo(disposable)
        }
    }
}