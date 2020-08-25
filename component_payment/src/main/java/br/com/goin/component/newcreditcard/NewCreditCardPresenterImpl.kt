package br.com.goin.component.newcreditcard

import br.com.goin.base.extensions.ioThread
import br.com.goin.base.model.PaymentModel
import br.com.goin.component.payment.CreditCardModel
import br.com.goin.component.payment.PaymentInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.io.Serializable

class NewCreditCardPresenterImpl(val view: NewCreditCardView): NewCreditCardPresenter {

    private val model = CreditCardModel()
    private var paymentModel: PaymentModel? = null
    private val paymentInteractor = PaymentInteractor.instance
    private val disposable = CompositeDisposable()

    override fun onCreate(){
        view.configureToolbar()
        view.configureViewPager(model)
        view.configureOnClickListeners()
        view.configureAnimations()
    }

    override fun onReceivePaymentModel(serializable: Serializable?) {
        serializable?.let {
            paymentModel = it as PaymentModel?
        }

        view.onPaymentReceived(paymentModel)
    }

    override fun saveCreditCard() {
        paymentInteractor.saveCreditCard(model)
                .ioThread()
                .doOnSubscribe { view.showLoading() }
                .doOnTerminate { view.hideLoading() }
                .subscribe({
                    view.onSuccess(model)

                }, { throwable: Throwable ->
                    view.handleError(throwable)

                }).addTo(disposable)
    }

    override fun onDestroy() {
        disposable.dispose()
    }

}