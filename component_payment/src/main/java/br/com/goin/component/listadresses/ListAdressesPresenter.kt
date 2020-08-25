package br.com.goin.component.listadresses

import br.com.goin.base.model.PaymentModel
import br.com.goin.component.address.AddressModel
import br.com.goin.component.address.UserAddress
import java.io.Serializable

interface ListAdressesPresenter {
    fun onCreate()
    fun onDestroy()
    fun goToCreditCardList()
    fun onReceivePaymentModel(serializable: Serializable?)
    fun loadAdresses()
    fun getPaymentModel(): PaymentModel?
    fun loadAddressModel(addressModel: AddressModel)
    fun loadUserAddresss(addressModel: UserAddress)
}