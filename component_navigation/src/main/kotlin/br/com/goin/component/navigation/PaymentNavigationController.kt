package br.com.goin.component.navigation

import android.app.Activity
import android.content.Context
import br.com.goin.base.model.PaymentModel

interface PaymentNavigationController{
    fun goToSelectAddressScreen(context: Context, model : PaymentModel)
    fun goToNewAddressScreen(context: Activity, model: PaymentModel?)
    fun goToCreditCardList(context: Context, paymentInfo: PaymentModel)
    fun goToNewCreditCradScreen(context: Activity, paymentInfo: PaymentModel)
    fun goToConfirmationScreen(context: Context, paymentModel: PaymentModel)
}