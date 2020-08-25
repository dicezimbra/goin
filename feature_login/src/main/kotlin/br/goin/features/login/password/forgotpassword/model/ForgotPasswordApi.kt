package br.goin.features.login.password.forgotpassword.model

import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface ForgotPasswordApi {

    @POST("graphql")
    fun forgotPassword(@Body body: GraphqlBody): Single<GraphQLResponse<ForgotPasswordResponse>>

    @POST("graphql")
    fun resetPassword(@Body body: GraphqlBody): Single<GraphQLResponse<ForgotPasswordResponse>>

    @POST("graphql")
    fun changePassword(@Body body: GraphqlBody): Single<GraphQLResponse<ForgotPasswordResponse>>

}