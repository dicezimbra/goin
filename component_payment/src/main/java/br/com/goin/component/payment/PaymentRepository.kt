package br.com.goin.component.payment

import br.com.goin.base.helpers.PreferenceHelper
import br.com.goin.base.model.PaymentModel
import br.com.goin.component.address.AddressApi
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import okhttp3.ResponseBody

private const val USER_CREDIT_CARD_KEY = "USER_CREDIT_CARD_KEY"

class PaymentRepository {

    private val service = RetrofitService(PaymentApi::class.java, BuildConfig.BASE_PAYMENT_URL)

    fun saveCreditCard(creditCard: CreditCardModel) :Observable<String>{
       return fetchCrediCards().map {
            it?.add(creditCard)
            val gson = Gson()
            val json = gson.toJson(it)
            PreferenceHelper.write(USER_CREDIT_CARD_KEY, json)
           json
        }

    }

    fun fetchCrediCards(): Observable<ArrayList<CreditCardModel>> {

        return Observable.create<ArrayList<CreditCardModel>> {

            try {
                val json = PreferenceHelper.read(USER_CREDIT_CARD_KEY) as? String

                if (json == null) {
                    it.onNext(ArrayList<CreditCardModel>())
                } else {
                    val gson = GsonBuilder().create()
                    val creditCardModelListType = object : TypeToken<ArrayList<CreditCardModel>>() {}.type
                    it.onNext(gson.fromJson(json, creditCardModelListType))
                }
            } catch (e: Throwable) {
                it.onError(e)
            }

            it.onComplete()
        }
    }

    fun finishPurchase(paymentModel: PaymentModel?): Observable<ResponseBody> {
        return service.apiService.finishPurchase(paymentModel!!).map { it }
    }
}