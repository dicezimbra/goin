package br.com.goin.component.newcreditcard

import java.io.Serializable

interface NewCreditCardPresenter {
    fun onCreate()
    fun onReceivePaymentModel(serializable: Serializable?)
    fun saveCreditCard()
    fun onDestroy()
}