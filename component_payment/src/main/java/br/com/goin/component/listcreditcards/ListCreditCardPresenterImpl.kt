package br.com.goin.component.listcreditcards

import br.com.goin.base.extensions.ioThread
import br.com.goin.base.model.PaymentModel
import br.com.goin.component.payment.PaymentInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.io.Serializable

class ListCreditCardPresenterImpl(var view: ListCreditCardView) : ListCreditCardPresenter {
    private val disposable = CompositeDisposable()
    private val paymentInteractor = PaymentInteractor.instance
    private var paymentModel: PaymentModel? = null
    override fun getPaymentModel(): PaymentModel {
        paymentModel?.let {
            return it
        }
    }


    override fun onCreate() {

        paymentInteractor.fetchAllCrediCards()
                //.useCache("CREDITCARD", CreditCardModel::class.java)
                .ioThread()
                .doOnSubscribe { view.showLoading() }
                .doOnTerminate { view.hideLoading() }
                .subscribe({
                    if (it.isEmpty()) {
                        view.addNewCreditCard()
                    } else {
                        view.configureView()
                        view.loadCreditCards(it)
                    }

                }, { throwable: Throwable ->
                    view.handleError(throwable)

                }).addTo(disposable)
    }


    override fun onDestroy() {
        disposable.dispose()
    }

    override fun onReceivePaymentModel(serializable: Serializable?) {
        serializable?.let {
            paymentModel = it as PaymentModel?
        }

        view.onPaymentReceived(paymentModel)
    }
}