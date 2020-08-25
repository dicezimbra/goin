package br.com.goin.feature.feed.model

import android.graphics.Bitmap
import br.com.goin.base.extensions.ioThread
import br.com.goin.component.session.user.UserSessionRepository
import br.com.goin.feature.feed.commentary.model.CommentsResponse
import br.com.goin.feature.feed.commentary.model.CreateComment
import br.com.goin.feature.feed.commentary.model.CreateCommentResponse
import br.com.goin.feature.feed.post.CreatePost
import br.com.goin.feature.feed.post.CreatePostResponse
import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.ResponseBody

class PostInteractorImpl : PostInteractor {


    private val repository = PostRepository()
    private val userRepository = UserSessionRepository()

    override fun fetchFeed(userId: String?, lastPostId: String?): Observable<Feed> {
        return when(userId){
            null -> repository.fetchFeed(lastPostId)
            else -> repository.fetchMyFeed(userId, lastPostId)
        }
    }

    override fun like(postId: String): Completable {
        return repository.like(postId)
    }

    override fun dislike(postId: String): Completable {
        return repository.dislike(postId)
    }

    override fun delete(postId: String): Completable {
        return repository.delete(postId)
    }

    override fun report(postId: String): Completable {
        return repository.report(postId)
    }

    override fun createPost(post: CreatePost, mediaImage: Bitmap?, mediaFilePath: String?, id: String?, clubId: String?): Observable<CreatePostResponse> {
        val userId = userRepository.fetchUser()?.id ?: ""

        return when {
            mediaFilePath != null && post.image == true -> {
                post.image = true
                post.video = false
                createPost(post, id, clubId).flatMap { createResponse ->
                    uploadPhoto(mediaFilePath, userId, createResponse.createPost.timeStamp!!).map { createResponse }.ioThread()
                }
            }
            mediaFilePath != null && post.video == true -> {
                post.video = true
                post.image = false

                createPost(post, id, clubId).flatMap { createResponse ->
                    uploadVideo(mediaFilePath, userId, createResponse.createPost.timeStamp!!).map { createResponse }.ioThread()
                }
            }
            else -> {
                post.image = false
                post.video = false
                createPost(post, id, clubId)
            }
        }
    }

    private fun uploadPhoto(picture: String, userId: String, timeStamp: Long): Observable<ResponseBody> {
        return repository.uploadPhoto(picture = picture, userId = userId, timeStamp = timeStamp)
    }

    private fun uploadVideo(video: String, userId: String, timeStamp: Long): Observable<ResponseBody> {
        return repository.uploadVideo(video = video, userId = userId, timeStamp = timeStamp)
    }


    private fun createPost(post: CreatePost, id: String?, clubId: String?): Observable<CreatePostResponse> {
        return if (id != null || clubId != null) {
            repository.createPostEvent(post, id, clubId)
        } else {
            repository.createPost(post)
        }
    }

    override fun loadPosts(postId: String): Observable<CommentsResponse> {
        return repository.loadCommentaries(postId)
    }

    override fun createCommentary(comment: CreateComment): Observable<CreateCommentResponse> {
        return repository.createComment(comment)
    }

    override fun deleteCommentary(postId: String?, commentId: String?): Completable {
        return repository.deleteCommentary(postId, commentId)
    }



}