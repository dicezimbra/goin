package br.com.goin.component.listadresses

import br.com.goin.base.model.PaymentModel
import br.com.goin.component.address.AddressModel

interface ListAdressesView {

    fun configureView()
    fun loadAddress(it: List<AddressModel>)
    fun addNewAddress()
    fun showLoading()
    fun hideLoading()
    fun handleError(t: Throwable?)
    fun goToCreditCardList()
    fun configureToolbar()
    fun loadAddressesInfo()
}