package br.com.goin.component.confirmation

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.model.PaymentModel
import br.com.goin.component.payment.CreditCardHelper
import br.com.goin.component.payment.PaymentInteractor
import br.com.goin.component.push.UserPushEndpointInteractor
import br.com.goin.component.session.user.UserSessionInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.io.Serializable

class ConfirmationPresenterImpl (val view: ConfirmationView): ConfirmationPresenter{

    private val disposable = CompositeDisposable()
    private var paymentModel: PaymentModel? = null
    private val paymentInteractor = PaymentInteractor.instance
    private val pushInteractor = UserPushEndpointInteractor.instance
    private val userInteractor = UserSessionInteractor.instance

    private var cvv: String? = null
    override fun onDestroy() {
        disposable.dispose()
    }

    override fun onReceivePaymentModel(serializableExtra: Serializable?) {
        serializableExtra?.let {
            paymentModel = it as PaymentModel
        }
    }

    override fun onCreate() {
        view.loadPaymentInfos(paymentModel!!)
        view.configureClick()
    }

    override fun finishPurchase() {
        paymentModel?.pushEndpoint = pushInteractor.fetchPushToken()
        paymentModel?.creditcard?.cvv = cvv?.toLong()
        paymentModel?.userId = userInteractor.fetchUser()?.id

        paymentInteractor.finishPurchase(paymentModel)
               // .useCache("ADDRESS", ResponseBody::class.java)
                .ioThread()
                .doOnSubscribe { view.showLoading() }
                .doOnTerminate { view.hideLoading() }
                .subscribe({
                    Log.e("GOIN","$it")
                    view.showSuccess(it)
                }, { throwable: Throwable ->
                    view.handleError(throwable)

                }).addTo(disposable)
    }

    override fun getCvv() {
        view.getCVV()
    }


    override fun isSecurityCodeValid(cvv: String) {
        val creditCardType = CreditCardHelper.findCardType("${paymentModel?.creditcard?.number}")

        if(cvv.length == CreditCardHelper.securityCodeValid(creditCardType)){
            this.cvv = cvv
            view.cvvValid()
        }

    }

    override fun getCCLasat4Numbers(): String? {
        var creditCard = "${paymentModel?.creditcard?.number}"
        var creditCardLenght = creditCard.length

        return creditCard.substring(creditCardLenght - 4, creditCardLenght)
    }

    override fun loadToolbar() {
        view.loadToolbar(paymentModel)
    }
}