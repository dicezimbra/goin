package br.com.goin.feature.profile.model

import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import br.com.goin.component.session.user.UserModel
import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface ProfileApi {

    @POST("graphql")
    fun updateUserProfile(@Body body: GraphqlBody): Observable<ResponseBody>

    @POST("graphql")
    fun findById(@Body body: GraphqlBody): Observable<GraphQLResponse<ProfileResponse>>


    @POST("graphql")
    fun follow(@Body body: GraphqlBody): Completable
}