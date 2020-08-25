package br.com.goin.component.address

import br.com.goin.component.payment.BuildConfig
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import io.reactivex.Observable

class AddressRepository {

    //private val service = RetrofitService(AddressApi::class.java, BuildConfig.BASE_ADDRESS_URL)
    private val service = RetrofitService(AddressApi::class.java, "http://117fa3bb.ngrok.io/")

    private val mapper = AddressMapper()

    fun getUserAdresses(userId: String): Observable<List<AddressModel>> {

        val graphqlQuery = GraphqlBody.Builder(AddressQueries.GET_USER_ADRESSES)
                .`var`("userId", userId)
                .build()

        return service.apiService.getUserAdresses(graphqlQuery).map {
            mapper.map(it.data?.addresses)
        }
    }

    fun getCepAddress(cep: String): Observable<AddressModel> {
        return service.apiService.getCepAddress(cep)
    }

    fun addAddress(addressModel: UserAddress): Observable<GraphQLResponse<UserAddressInput>> {
        val graphqlQuery = GraphqlBody.Builder(AddressQueries.SEND_ADDRESS)
                .`var`("address", addressModel)
                .build()

        return service.apiService.addAddress(graphqlQuery)
    }
}
