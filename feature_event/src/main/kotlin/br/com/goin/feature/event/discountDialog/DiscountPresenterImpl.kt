package br.com.goin.feature.event.discountDialog

import android.util.Log
import br.com.goin.component.model.event.Event
import br.com.goin.base.extensions.ioThread
import br.com.goin.component.model.event.EventInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class DiscountPresenterImpl(val view: DiscountView) : DiscountPresenter {

    private var event: Event? = null

    private val disposables = CompositeDisposable()
    private val interactor = EventInteractor.instance

    override fun onCreate() {
        view.configureClickListener()
    }

    override fun onDestroy() {
        disposables.dispose()
    }

    override fun onReceivedEvent(event: Event) {
        this.event = event
    }

    override fun onClickNo() {
        event?.let {
            view.goToIngressoRapidoSite(it)
        }
    }

    override fun onConfirmDiscountCode(code: String) {
        if (code.isEmpty()) {
            view.showToastFillFields()
        } else {
            validateDiscountCode(code)
        }
    }

    private fun validateDiscountCode(code: String) {
        event?.let { event ->
            interactor.validateDiscountcode(event.id, code)
                    .ioThread()
                    .doOnSubscribe { view.showLoading() }
                    .doOnTerminate { view.hideLoading() }
                    .subscribe({
                        if (it.valid) {
                            view.goToTickets(event, code)
                        } else {
                            view.showToastDiscountCodeInvalid()
                            view.clearDiscountCodeField()
                        }
                    }, {
                        view.showToastDiscountCodeInvalid()
                        view.clearDiscountCodeField()

                        Log.w("DiscountPresenter", it)
                    }).addTo(disposables)
        }
    }
}