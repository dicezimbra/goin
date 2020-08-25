package br.goin.features.login.password.forgotpassword.model

import br.com.goin.base.OutsmartResponse
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import io.reactivex.Single

interface ForgotPasswordInteractor {

    companion object {
        val instance: ForgotPasswordInteractor by lazy { ForgotPasswordImpl() }
    }


    fun changePassword(email: String, oldPassword: String, password: String, passwordConfirmation: String): Single<GraphQLResponse<ForgotPasswordResponse>>
    fun forgotPassword(email: String): Single<GraphQLResponse<ForgotPasswordResponse>>
    fun resetPassword(code: String, newPassword: String, email: String, passwordConfirmation: String): Single<GraphQLResponse<ForgotPasswordResponse>>

}