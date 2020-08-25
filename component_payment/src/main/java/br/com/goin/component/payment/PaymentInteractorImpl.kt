package br.com.goin.component.payment

import br.com.goin.base.model.PaymentModel
import io.reactivex.Observable
import okhttp3.ResponseBody

class PaymentInteractorImpl: PaymentInteractor {

    private val repository = PaymentRepository()

    override fun fetchAllCrediCards(): Observable<List<CreditCardModel>> {
        return repository.fetchCrediCards().map { it }
    }

    override fun saveCreditCard(creditCard: CreditCardModel): Observable<String>{
         return repository.saveCreditCard(creditCard)
    }

    override fun finishPurchase(paymentModel: PaymentModel?): Observable<ResponseBody> {
        return repository.finishPurchase(paymentModel)
    }
}