package br.com.goin.component.listadresses

import br.com.goin.base.extensions.ioThread
import br.com.goin.base.extensions.useCache
import br.com.goin.base.model.PaymentModel
import br.com.goin.component.address.AddressInteractor
import br.com.goin.component.address.AddressModel
import br.com.goin.component.address.UserAddress
import br.com.goin.component.session.user.UserSessionInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.io.Serializable

class ListAdressesPresenterImpl(val view: ListAdressesView) : ListAdressesPresenter {

    private val disposable = CompositeDisposable()

    private var paymentModel: PaymentModel? = null
    private val addressInteractor = AddressInteractor.instance
    private val userSession = UserSessionInteractor.instance

    override fun onCreate() {
        // userId?.let {
        view.configureToolbar()
        loadAdresses()
        view.configureView()
    }

    override fun getPaymentModel(): PaymentModel? {
        return paymentModel
    }

    override fun loadAdresses(){
        val userId = userSession.fetchUser()?.id
        userId?.let { userId ->
           addressInteractor.getUserAdresses(userId)
                   .useCache("ADDRESS$userId", AddressModel::class.java)
                   .ioThread()
                   .doOnSubscribe { view.showLoading() }
                   .doOnTerminate { view.hideLoading() }
                   .subscribe({
                       if (it.size == 0) {
                           view.addNewAddress()
                       } else {
                           view.configureView()
                           view.loadAddress(it)
                       }

                   }, { throwable: Throwable ->
                       view.handleError(throwable)

                   }).addTo(disposable)
           //  }
       }
    }

    override fun onReceivePaymentModel(serializable: Serializable?) {
        serializable?.let {
            paymentModel = it as PaymentModel?
            view.loadAddressesInfo()
        }
    }

    override fun onDestroy() {
        disposable.dispose()
    }

    override fun goToCreditCardList() {
        view.goToCreditCardList()
    }

    override fun loadAddressModel(addressModel: AddressModel){
        paymentModel?.let {
            it.address.zipcode = addressModel.cep
            it.address.street = addressModel.logradouro
            it.address.number = addressModel.number
            it.address.complement = addressModel.complemento
            it.address.district = addressModel.bairro
            it.address.city = addressModel.localidade
            it.address.state = addressModel.uf
            it.address.label = addressModel.label
        }
    }

    override fun loadUserAddresss(addressModel: UserAddress) {
        paymentModel?.let {
            it.address.zipcode = addressModel.zipcode
            it.address.street = addressModel.street
            it.address.number = addressModel.number
            it.address.complement = addressModel.complement
            it.address.label = addressModel.alias
            it.address.district = addressModel.district
            it.address.city = addressModel.city
            it.address.state = addressModel.state
        }
    }
}