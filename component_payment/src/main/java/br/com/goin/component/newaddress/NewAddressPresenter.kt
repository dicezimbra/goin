package br.com.goin.component.newaddress

import br.com.goin.base.model.PaymentModel
import br.com.goin.component.address.UserAddress
import java.io.Serializable

interface NewAddressPresenter {
    fun onCreate()
    fun loadAddress(zipcode: String)
    fun addAddress()

    fun setAddressItens(addressItems: UserAddress)
    fun onReceivePaymentModel(payment: Serializable?)
}