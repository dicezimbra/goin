package br.goin.features.login.password.forgotpassword.model

import br.com.goin.base.BuildConfig
import br.com.goin.base.GraphqlQuery
import br.com.goin.base.OutsmartResponse
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import io.reactivex.Single

class ForgotPasswordRepository {

    companion object {
        var service = RetrofitService(ForgotPasswordApi::class.java, BuildConfig.BASE_URL)
    }

    fun forgotPassword(email: String): Single<GraphQLResponse<ForgotPasswordResponse>> {
        val graphqlQuery = GraphqlBody.Builder(PasswordQueries.FORGOT_PASSWORD)
                .`var`("email", email)
                .build()
        return service.apiService.forgotPassword(graphqlQuery)
    }

    fun resetPassword(verificationCode: String, newPassword: String, email: String, passwordConfirmation: String):
            Single<GraphQLResponse<ForgotPasswordResponse>> {
        val graphqlQuery = GraphqlBody.Builder(PasswordQueries.RESET_PASSWORD)
                .`var`("email", email)
                .`var`("verificationCode", verificationCode)
                .`var`("newPassword", newPassword)
                .`var`("passwordConfirmation", passwordConfirmation)
                .build()
        return service.apiService.resetPassword(graphqlQuery)
    }

    fun changePassword(email: String, oldPassword: String, password: String, passwordConfirmation: String):
            Single<GraphQLResponse<ForgotPasswordResponse>> {
        val graphqlQuery = GraphqlBody.Builder(PasswordQueries.CHANGE_PASSWORD)
                .`var`("email", email)
                .`var`("oldPassword", oldPassword)
                .`var`("newPassword", password)
                .`var`("passwordConfirmation", passwordConfirmation)
                .build()
        return service.apiService.changePassword(graphqlQuery)
    }
}