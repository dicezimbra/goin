package br.com.goin.component.address

import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AddressApi {



    @GET("address/{cep}")
    fun getCepAddress(@Path("cep") cep: String): Observable<AddressModel>

    @POST("graphql")
    fun addAddress(@Body graphql: GraphqlBody): Observable<GraphQLResponse<UserAddressInput>>


    @POST("graphql")
    fun getUserAdresses(@Body body: GraphqlBody): Observable<GraphQLResponse<UserAddressList>>
}