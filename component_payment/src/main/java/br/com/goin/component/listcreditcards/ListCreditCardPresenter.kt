package br.com.goin.component.listcreditcards

import br.com.goin.base.model.PaymentModel
import br.com.goin.component.payment.CreditCardModel
import java.io.Serializable

interface ListCreditCardPresenter {
    fun onCreate()
    fun onDestroy()
    fun onReceivePaymentModel(serializable: Serializable?)
    fun getPaymentModel(): PaymentModel
}