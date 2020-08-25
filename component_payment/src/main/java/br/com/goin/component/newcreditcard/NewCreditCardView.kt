package br.com.goin.component.newcreditcard

import br.com.goin.base.model.PaymentModel
import br.com.goin.component.payment.CreditCardModel

interface NewCreditCardView {
    fun configureToolbar()
    fun configureViewPager(model: CreditCardModel)
    fun configureOnClickListeners()
    fun onPaymentReceived(paymentModel: PaymentModel?)
    fun configureAnimations()
    fun showLoading()
    fun hideLoading()
    fun handleError(throwable: Throwable)
    fun onSuccess(it: CreditCardModel)
}