package br.com.goin.component.confirmation

import br.com.goin.base.model.PaymentModel
import okhttp3.ResponseBody

interface ConfirmationView {
    fun loadPaymentInfos(paymentModel: PaymentModel)
    fun configureClick()
    fun showLoading()
    fun hideLoading()
    fun handleError(t: Throwable?)
    fun showSuccess(it: ResponseBody?)
    fun getCVV()
    fun cvvValid()
    fun loadToolbar(paymentModel: PaymentModel?)
}