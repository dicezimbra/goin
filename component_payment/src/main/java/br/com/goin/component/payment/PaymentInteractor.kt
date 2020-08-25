package br.com.goin.component.payment

import br.com.goin.base.model.PaymentModel
import io.reactivex.Observable
import okhttp3.ResponseBody

interface PaymentInteractor {

    companion object {
        val instance: PaymentInteractor by lazy { PaymentInteractorImpl() }
    }

    fun fetchAllCrediCards(): Observable<List<CreditCardModel>>
    fun saveCreditCard(creditCard: CreditCardModel): Observable<String>
    fun finishPurchase(paymentModel: PaymentModel?): Observable<ResponseBody>
}