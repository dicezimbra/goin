package br.goin.features.login.password.forgotpassword.model

import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import io.reactivex.Single

class ForgotPasswordImpl : ForgotPasswordInteractor {

    private val repository = ForgotPasswordRepository()

    override fun changePassword(email: String, oldPassword: String, password: String, passwordConfirmation: String):
            Single<GraphQLResponse<ForgotPasswordResponse>> {
        return repository.changePassword(email, oldPassword, password, passwordConfirmation)
    }

    override fun forgotPassword(email: String) = repository.forgotPassword(email)

    override fun resetPassword(code: String, newPassword: String, email: String, passwordConfirmation: String):
            Single<GraphQLResponse<ForgotPasswordResponse>> {
        return repository.resetPassword(code, newPassword, email, passwordConfirmation)
    }

}



