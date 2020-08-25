package br.com.goin.component.listcreditcards

import br.com.goin.base.model.PaymentModel
import br.com.goin.component.payment.CreditCardModel

interface ListCreditCardView {

    fun configureView()
    fun loadCreditCards(it: List<CreditCardModel>)
    fun addNewCreditCard()
    fun showLoading()
    fun hideLoading()
    fun handleError(t: Throwable?)
    fun onPaymentReceived(paymentModel: PaymentModel?)
}