package br.com.goin.feature.event.action

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.component.model.event.EventInteractor
import br.com.goin.component.model.event.Event
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class ActionComponentPresenterImpl(val view: ActionComponentView) : ActionComponentPresenter {

    private var event: Event? = null
    private val interactor = EventInteractor.instance
    private val disposables = CompositeDisposable()

    override fun onReceiveEventModel(event: Event) {
        this.event = event
    }

    override fun onCreate() {
        view.configureActions()
        view.configureOnClickListeners()

        event?.let {
            view.configureCheckin(it)
        }
    }

    override fun onDestroy() {
        disposables.dispose()
    }

    override fun reloadEventModel(event: Event) {
        this.event?.let {
            if (it != event) {
                view.configureCheckin(event)
            }
        }
    }

    override fun onCheckinClicked() {
        event?.let {
            if (it.hasEventStarted()) {
                view.showCheckingDialog(it)
            } else {
                view.showCheckingDisabledDialog(it)
            }
        }
    }

    override fun onInviteClicked() {
        event?.let {
            view.goToInvite(it.id)
        }
    }

    private fun confirmTokenValid() {
        event?.let { event ->
            interactor.confirmTokenValid("INGRESSE")
                    .ioThread()
                    .subscribe({ isConfirmed ->
                        if (isConfirmed) {
                            view.openTickets(event)
                        } else {
                            view.loginInIngresse(event)
                        }
                    }, {
                        Log.w("EventPresenter", it)
                    }).addTo(disposables)
        }
    }
}