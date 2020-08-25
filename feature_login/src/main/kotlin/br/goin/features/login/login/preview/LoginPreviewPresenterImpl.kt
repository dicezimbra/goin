package br.goin.features.login.login.preview

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.goin.features.login.model.UserInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import retrofit2.HttpException

class LoginPreviewPresenterImpl(val view: LoginPreviewView) : LoginPreviewPresenter {

    private val userInteractor = UserInteractor.instance
    private val disposables = CompositeDisposable()

    override fun onCreate() {
        view.onConfigureClickListeners()
    }

    override fun onFacebookLogged(accessToken: String, identityProvider: String) {

        userInteractor.loginWithFacebook(accessToken, identityProvider)
                .ioThread()
                .doOnSubscribe {  }
                .doOnTerminate { }
                .subscribe({
                    view.saveEmailOnSharedPreferences(it)
                    view.goToTabsMain()
                }, {
                    when (it) {
                        is HttpException ->
                            view.showDialogOnError(it.code())
                    }
                    Log.w("LoginPreviewPresenter", it.message)
                })
                .addTo(disposables)
    }
}