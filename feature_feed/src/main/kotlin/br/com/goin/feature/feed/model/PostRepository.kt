package br.com.goin.feature.feed.model

import android.util.Log
import br.com.goin.base.BaseApplication
import br.com.goin.base.BuildConfig
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import br.com.goin.component.rest.api.upload.UploadService
import br.com.goin.feature.feed.commentary.model.CommentsResponse
import br.com.goin.feature.feed.commentary.model.CreateComment
import br.com.goin.feature.feed.commentary.model.CreateCommentResponse
import br.com.goin.feature.feed.post.CreatePost
import br.com.goin.feature.feed.post.CreatePostResponse
import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.ResponseBody
import java.io.File


private var TEMP_KEY = "Posts/"

class PostRepository {

    private val service = RetrofitService(PostApi::class.java, BuildConfig.BASE_URL)
    private val uploadService = UploadService()

    fun fetchMyFeed(userId: String?, lastPostId: String?): Observable<Feed> {
        val graphqlQuery = GraphqlBody.Builder(PostQueries.GET_MY_FEED)
                .`var`("lastPostId", lastPostId)
                .`var`("userId", userId)
                .build()

        return service.apiService.fetchPosts(graphqlQuery).map { Feed(post = it.data?.posts, morePostsToGet = false) }
    }

    fun fetchFeed(lastPostId: String?): Observable<Feed> {
        val graphqlQuery = GraphqlBody.Builder(PostQueries.GET_FEED)
                .`var`("lastPostId", lastPostId)
                .build()

        return service.apiService.fetchPosts(graphqlQuery).map { it.data?.feed ?: Feed() }
    }

    fun delete(postId: String): Completable {
        val graphqlQuery = GraphqlBody.Builder(PostQueries.DELETE_POST)
                .`var`("postId", postId)
                .build()

        return service.apiService.delete(graphqlQuery)
    }

    fun report(postId: String): Completable {
        val graphqlQuery = GraphqlBody.Builder(PostQueries.REPORT_POST)
                .`var`("postId", postId)
                .build()

        return service.apiService.report(graphqlQuery)
    }

    fun like(postId: String): Completable {
        val graphqlQuery = GraphqlBody.Builder(PostQueries.LIKE_POST)
                .`var`("postId", postId)
                .build()

        return service.apiService.report(graphqlQuery)

    }

    fun dislike(postId: String): Completable {
        val graphqlQuery = GraphqlBody.Builder(PostQueries.DISLIKE_POST)
                .`var`("postId", postId)
                .build()

        return service.apiService.report(graphqlQuery)

    }


  /*  fun uploadPhoto(picture: String, userId: String, timeStamp: Long?): Observable<String> {
        return Observable.create<String> {
            try {
                val fileFinal = File(picture)
                val credentials = BasicAWSCredentials(AMAZON_S3_KEY, AMAZON_S3_SECRET)
                val s3Client = AmazonS3Client(credentials)

                val utility = TransferUtility(s3Client, BaseApplication.instance.context)
                val observer = utility.upload(BuildConfig.BUCKET_NAME, "$TEMP_KEY$userId/$timeStamp.jpeg", fileFinal)
                observer.setTransferListener(object : TransferListener {
                    override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                    }

                    override fun onStateChanged(id: Int, state: TransferState?) {
                        if (state?.compareTo(TransferState.COMPLETED) == 0) {
                            it.onNext(BuildConfig.BUCKET_PATH + TEMP_KEY)
                            it.onComplete()
                        }
                    }

                    override fun onError(id: Int, ex: Exception?) {
                        ex?.let { exception ->
                            it.onError(exception)
                        }
                    }
                })
            } catch (ex: Exception) {
                Log.e("UserRepositoryUploadEx", ex.localizedMessage)
            }
        }
    }
*/
    fun createPost(post: CreatePost): Observable<CreatePostResponse> {
        val graphqlQuery = GraphqlBody.Builder(PostQueries.CREATE_POST)
                .`var`("input", post)
                .build()

        return service.apiService.createPosts(graphqlQuery).map { it.data }
    }

    fun createPostEvent(post: CreatePost, id: String?, clubId: String?): Observable<CreatePostResponse> {
        val graphqlQuery = GraphqlBody.Builder(PostQueries.CREATE_POST_WITH_EVENT).`var`("input", post)
        if(id != null){
            graphqlQuery.`var`("eventId", id)
        }

        if(clubId != null){
            graphqlQuery.`var`("clubId", clubId)
        }

        return service.apiService.createPosts(graphqlQuery.build()).map { it.data }
    }

    fun loadCommentaries(postId: String): Observable<CommentsResponse> {
        val graphqlQuery = GraphqlBody.Builder(PostQueries.GET_COMMENTS)
                .`var`("postId", postId)
                .build()

        return service.apiService.loadComments(graphqlQuery).map { it.data }
    }

    fun createComment(comment: CreateComment): Observable<CreateCommentResponse> {
        val graphqlQuery = GraphqlBody.Builder(PostQueries.CREATE_COMMENT)
                .`var`("input", comment)
                .build()

        return service.apiService.createCommentary(graphqlQuery).map { it.data }
    }

    fun deleteCommentary(postId: String?, commentId: String?): Completable {
        val graphqlQuery = GraphqlBody.Builder(PostQueries.DELETE_COMMENT)
                .`var`("postId", postId)
                .`var`("commentId", commentId)
                .build()

        return service.apiService.deleteCommentary(graphqlQuery)
    }

    fun uploadPhoto(picture: String, userId: String, timeStamp: Long): Observable<ResponseBody> {
       return uploadService.uploadImage(File(picture), "$TEMP_KEY$userId/$timeStamp.jpeg")
    }

    fun uploadVideo(video: String, userId: String, timeStamp: Long): Observable<ResponseBody> {
        return uploadService.uploadVideo(File(video), "$TEMP_KEY$userId/$timeStamp.mp4")
    }

}