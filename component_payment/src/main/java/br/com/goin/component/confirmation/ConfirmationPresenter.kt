package br.com.goin.component.confirmation

import java.io.Serializable

interface ConfirmationPresenter {
    fun onDestroy()
    fun onReceivePaymentModel(serializableExtra: Serializable?)
    fun onCreate()
    fun finishPurchase()
    fun getCvv()
    fun isSecurityCodeValid(cvv: String)
    fun getCCLasat4Numbers(): String?
    fun loadToolbar()
}