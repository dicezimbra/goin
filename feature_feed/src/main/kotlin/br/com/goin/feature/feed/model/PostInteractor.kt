package br.com.goin.feature.feed.model

import android.graphics.Bitmap
import br.com.goin.feature.feed.commentary.model.CommentsResponse
import br.com.goin.feature.feed.commentary.model.CreateComment
import br.com.goin.feature.feed.commentary.model.CreateCommentResponse
import br.com.goin.feature.feed.post.CreatePost
import br.com.goin.feature.feed.post.CreatePostResponse
import io.reactivex.Completable
import io.reactivex.Observable

interface PostInteractor {

    companion object {
        val instance: PostInteractor by lazy { PostInteractorImpl() }
    }

    fun fetchFeed(userId: String? = null, lastPostId: String?): Observable<Feed>
    fun delete(postId: String): Completable
    fun report(postId: String): Completable
    fun like(postId: String): Completable
    fun dislike(postId: String): Completable
    fun createPost(post: CreatePost, mediaImage: Bitmap?, mediaFilePath: String?, id: String?, clubId: String?): Observable<CreatePostResponse>
    fun loadPosts(postId: String): Observable<CommentsResponse>
    fun createCommentary(comment: CreateComment): Observable<CreateCommentResponse>
    fun deleteCommentary(postId: String?, commentId: String?): Completable
}