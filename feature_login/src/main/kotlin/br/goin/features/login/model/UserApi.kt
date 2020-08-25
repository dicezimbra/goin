package br.goin.features.login.model

import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import br.com.goin.component.session.user.UserModel
import br.goin.features.login.login.VerifyEmail
import br.goin.features.login.register.NewRegisterUserResponseData
import br.goin.features.login.login.LoginRequest
import br.goin.features.login.login.LoginResponse
import br.goin.features.login.login.QueryReceiver
import br.goin.features.login.password.forgotpassword.model.ForgotPasswordResponse
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApi {

    @POST("login")
    fun login(@Body body: LoginRequest): Observable<LoginResponse>

    @POST("graphql")
    fun register(@Body body: GraphqlBody): Observable<GraphQLResponse<NewRegisterUserResponseData>>

    @POST("graphql")
    fun getFacebookUser(@Body body: GraphqlBody, @Header("token") token: String): Observable<GraphQLResponse<QueryReceiver>>

    @POST("graphql")
    fun getUser(@Body body: GraphqlBody): Observable<GraphQLResponse<QueryReceiver>>

    @POST("graphql")
    fun updateUser(@Body body: GraphqlBody): Single<GraphQLResponse<UserModel>>

    @POST("graphql")
    fun verifyEmail(@Body body: GraphqlBody): Single<GraphQLResponse<VerifyEmail>>

    @POST("graphql")
    fun createTempUser(@Body body: GraphqlBody): Completable

    @POST("graphql")
    fun registerCostumer(@Body body: GraphqlBody): Completable

    @POST("graphql")
    fun forgotPassword(@Body body: GraphqlBody): Single<GraphQLResponse<ForgotPasswordResponse>>

    @POST("graphql")
    fun resetPassword(@Body body: GraphqlBody): Single<GraphQLResponse<ForgotPasswordResponse>>

    @POST("graphql")
    fun changePassword(@Body body: GraphqlBody): Single<GraphQLResponse<ForgotPasswordResponse>>

    @POST("login-social")
    fun loginFacebook(@Header("token") token: String, @Header("identity-provider") identityProvider: String): Observable<UserModel>

}