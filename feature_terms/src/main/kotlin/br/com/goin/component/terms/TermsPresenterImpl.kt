package br.com.goin.component.terms

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.component.terms.model.TermsInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.*

class TermsPresenterImpl(val view: TermsView) : TermsPresenter {
    private val interactor = TermsInteractor.instance
    private var disposables = CompositeDisposable()

    override fun onCreate() {
        view.setupToolbar()
        view.setupBackButton()
    }

    override fun fetchTerms(termsType: TermsType) {
        interactor
                .fetchTerms(mapLanguage(), termsType.name)
                .ioThread()
                .doOnSubscribe { view.showLoading() }
                .doAfterTerminate { view.hideLoading() }
                .subscribe({
                    if(it.isNotEmpty()) {
                        view.configureExpandableLayout(it)
                    } else {
                        view.displayDialogOnError()
                    }
                }, {
                    view.displayDialogOnError()
                    Log.d("TermsPresenter", it.message)
                }).addTo(disposables)
    }

    override fun onDestroy() {
        disposables.dispose()
    }

    private fun mapLanguage(): String? {
        return when(Locale.getDefault().language) {
            Locale("pt").language -> "PTBR"
            Locale("en").language -> "ENUS"
            else -> null
        }
    }
}