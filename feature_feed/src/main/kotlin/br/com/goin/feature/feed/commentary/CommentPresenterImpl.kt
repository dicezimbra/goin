package br.com.goin.feature.feed.commentary

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.component.session.user.UserSessionInteractor
import br.com.goin.feature.feed.commentary.model.CreateComment
import br.com.goin.feature.feed.commentary.model.CreateCommentResponse
import br.com.goin.feature.feed.model.Post
import br.com.goin.feature.feed.model.PostInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.io.Serializable

class CommentPresenterImpl(val view: CommentView) : CommentPresenter {

    private val interactor = PostInteractor.instance
    private val userInteractor = UserSessionInteractor.instance
    private val disposable = CompositeDisposable()
    private var reloadPostId: String? = null

    override fun onCreate(postId: String?, post: Serializable?) {
        val userId = userInteractor.fetchUser()?.id
        postId?.let {
            loadCommentaries(it)
            reloadPostId = it
        }
        loadUser()
        view.configureToolbar()
        view.onClickListeners(postId, userId)

        post?.let {
            val intent = it as Post
            view.intentPost(intent)
        }
    }


    private fun loadUser() {
        val user = userInteractor.fetchUser()
        user?.let {
            view.setUserInfo(it)
        }
    }

    override fun sendComment(comment: CreateComment) {
        interactor.createCommentary(comment)
                .ioThread()
                .subscribe({
                    it.createComment
                    loadCommentaries(reloadPostId!!)
                }, { throwable ->
                    Log.e("CommentPresenter", throwable.localizedMessage, throwable)
                }).addTo(disposable)

    }

    private fun loadCommentaries(postId: String) {
        interactor.loadPosts(postId)
                .ioThread()
                .doOnNext {
                    view.showLoading()
                }
                .doOnError {
                    view.emptyComments(it.message)
                }
                .subscribe({
                    view.hideLoading()
                    if ( it.comments.size >= 1) {
                        view.onLoadComments(it.comments)
                    }else{
                        view.showMessageNoComments()
                    }
                }, { throwable ->
                    Log.e("CommentPresenter", throwable.message, throwable)
                }).addTo(disposable)
    }

    override fun onDeleteComment(postId: String?, commentId: String?) {
        interactor.deleteCommentary(postId, commentId)
                .ioThread()
                .subscribe({
                    loadCommentaries(reloadPostId!!)
                }, { throwable ->
                    Log.e("CommentPresenter", throwable.localizedMessage, throwable)
                }).addTo(disposable)
    }

}