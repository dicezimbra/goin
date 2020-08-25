package br.com.goin.component.push

import br.com.goin.base.BuildConfig
import br.com.goin.base.helpers.PreferenceHelper
import br.com.goin.component.push.UserPushApi
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import br.com.goin.component.session.user.UserModel
import br.com.goin.component.session.user.UserQuery
import io.reactivex.Single

private const val PUSH_TOKEN_PREFERENCE_KEY = "PUSH_TOKEN"
class UserPushRepository {

    companion object {
        var service = RetrofitService(UserPushApi::class.java, BuildConfig.BASE_URL)
    }


    fun updateUser(userModel: UserModel): Single<UserModel> {
        userModel.clean()
        val graphqlQuery = GraphqlBody.Builder(UserQuery.UPDATE_USER)
                .`var`("input", userModel)
                .build()

        return service.apiService.updateUser(graphqlQuery).map { it.data }
    }



    fun savePushToken(token: String) {
        PreferenceHelper.write(PUSH_TOKEN_PREFERENCE_KEY, token)
    }

    fun fetchPushToken(): String? {
        return PreferenceHelper.read(PUSH_TOKEN_PREFERENCE_KEY) as? String
    }
}