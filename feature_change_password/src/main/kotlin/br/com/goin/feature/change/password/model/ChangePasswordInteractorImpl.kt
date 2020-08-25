package br.com.goin.feature.change.password.model

import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import io.reactivex.Completable
import io.reactivex.Single

class ChangePasswordInteractorImpl : ChangePasswordInteractor {

    private val repository = ChangePasswordRepository()

    override fun changePassword(
            email: String,
            oldPassword: String,
            password: String,
            passwordConfirmation: String
    ): Completable {
        return repository.changePassword(email, oldPassword, password, passwordConfirmation)
    }
}
