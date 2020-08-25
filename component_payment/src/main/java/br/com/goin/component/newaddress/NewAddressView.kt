package br.com.goin.component.newaddress

import br.com.goin.base.model.PaymentModel
import br.com.goin.component.address.AddressModel
import br.com.goin.component.address.UserAddress

interface NewAddressView {
    fun configureOnClickListeners()
    fun configureViewPager()
    fun configureToolbar()
    fun onLoadAddress(address: AddressModel?)
    fun onAddressSuccess(address: UserAddress)
    fun onAddressError()
    fun onPaymentReceived(paymentModel: PaymentModel?)
    fun showLoading()
    fun hideLoading()
     fun showException(message: String?)
    fun disableButtonNext()
}