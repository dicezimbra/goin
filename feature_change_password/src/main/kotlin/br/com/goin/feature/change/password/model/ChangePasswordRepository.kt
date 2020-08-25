package br.com.goin.feature.change.password.model

import br.com.goin.base.BuildConfig
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import io.reactivex.Completable

class ChangePasswordRepository {

    companion object {
        var service = RetrofitService(ChangePasswordApi::class.java, BuildConfig.BASE_URL)
    }

    fun changePassword(
            email: String,
            oldPassword: String,
            password: String,
            passwordConfirmation: String
    ): Completable {

        val graphqlQuery = GraphqlBody.Builder(ChangePasswordQueries.CHANGE_PASSWORD)
                .`var`("email", email)
                .`var`("oldPassword", oldPassword)
                .`var`("newPassword", password)
                .`var`("passwordConfirmation", passwordConfirmation)
                .build()

        return service.apiService.changePassword(graphqlQuery)
    }
}
