package br.com.goin.feature.change.password.model

import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import io.reactivex.Completable
import io.reactivex.Single

interface ChangePasswordInteractor {

    companion object {
        val instance: ChangePasswordInteractor by lazy { ChangePasswordInteractorImpl() }
    }

    fun changePassword(email: String, oldPassword: String, password: String, passwordConfirmation: String): Completable
}