package br.com.goin.component.payment

import br.com.goin.base.model.PaymentModel
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PaymentApi {


    @GET("creditcard")
    fun fetchAllCrediCards(@Query("userid") userid: String): Observable<List<CreditCardModel>>

    @POST("order/create-reserve")
    fun finishPurchase(@Body model: PaymentModel): Observable<ResponseBody>

}