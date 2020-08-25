package br.com.goin.component.push

import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import br.com.goin.component.session.user.UserModel
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface UserPushApi {

    @POST("graphql")
    fun updateUser(@Body body: GraphqlBody): Single<GraphQLResponse<UserModel>>

}
