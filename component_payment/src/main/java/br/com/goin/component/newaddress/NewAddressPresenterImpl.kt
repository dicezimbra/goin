package br.com.goin.component.newaddress

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.model.PaymentModel
import br.com.goin.component.address.AddressInteractor
import br.com.goin.component.address.AddressModel
import br.com.goin.component.address.UserAddress
import br.com.goin.component.address.UserAddressInput
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import retrofit2.HttpException
import java.io.Serializable

class NewAddressPresenterImpl(private var view: NewAddressView) : NewAddressPresenter {


    private val addressInteractor = AddressInteractor.instance
    private val disposable = CompositeDisposable()
    private var paymentModel: PaymentModel? = null
    private var addressItems: UserAddress? = null
    override fun onCreate() {
        view.configureOnClickListeners()
        view.configureViewPager()
        view.configureToolbar()
    }


    override fun onReceivePaymentModel(payment: Serializable?) {
        payment?.let {
            paymentModel = it as PaymentModel
            view.onPaymentReceived(paymentModel)
        }
    }

    override fun loadAddress(zipcode: String) {
        addressInteractor.getCepAddress(zipcode)
                .ioThread()
                .doOnSubscribe { view.showLoading() }
                .doOnTerminate { view.hideLoading() }
                .subscribe({
                    view.onLoadAddress(it)
                }, {
                    Log.e("NewAddressPresenter", it.message, it)
                    view.disableButtonNext()
                }).addTo(disposable)
    }

    override fun setAddressItens(addressItems: UserAddress){
        this.addressItems = addressItems
    }

    override fun addAddress() {
        addressInteractor.addAddress(addressItems!!)
                .ioThread()
                .doOnSubscribe { view.showLoading() }
                .doOnTerminate { view.hideLoading() }
                .subscribe({
                    if (it != null) {
                        view.onAddressSuccess(addressItems!!)
                    } else {
                        view.onAddressError()
                    }
                }, {

                    if (it is HttpException) {
                        view.showException(it.message())
                    }
                    Log.e("NewAddressPresenter", it.message, it)

                }).addTo(disposable)
    }


}