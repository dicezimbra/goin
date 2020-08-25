package br.com.goin.feature.feed.model

import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import br.com.goin.feature.feed.commentary.model.CommentsResponse
import br.com.goin.feature.feed.commentary.model.CreateComment
import br.com.goin.feature.feed.commentary.model.CreateCommentResponse
import br.com.goin.feature.feed.post.CreatePostResponse

import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface PostApi {

    @POST("graphql")
    fun fetchPosts(@Body body: GraphqlBody): Observable<GraphQLResponse<FeedResponse>>

    @POST("graphql")
    fun delete(@Body body: GraphqlBody): Completable

    @POST("graphql")
    fun report(@Body body: GraphqlBody): Completable

    @POST("graphql")
    fun createPosts(@Body body: GraphqlBody): Observable<GraphQLResponse<CreatePostResponse>>

    @POST("graphql")
    fun loadComments(@Body body: GraphqlBody): Observable<GraphQLResponse<CommentsResponse>>

    @POST("graphql")
    fun createCommentary(@Body body: GraphqlBody): Observable<GraphQLResponse<CreateCommentResponse>>

    @POST("graphql")
    fun deleteCommentary(@Body body: GraphqlBody): Completable
}