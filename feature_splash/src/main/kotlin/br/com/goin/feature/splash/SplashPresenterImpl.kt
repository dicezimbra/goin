package br.com.goin.feature.splash

import android.util.Log
import br.com.goin.component.session.user.UserSessionInteractor
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.utils.VersionHelper
import br.com.goin.feature.splash.model.UpdateInteractor
import br.com.goin.feature.splash.model.VerifyUpdateStauts
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class SplashPresenterImpl(val view: SplashView): SplashPresenter{

    private val interactor = UpdateInteractor.instance
    private val userSessionInteractor = UserSessionInteractor.instance
    private val disposables = CompositeDisposable()

    override fun onStart() {
        checkUpdate()
    }

    override fun onDestroy(){
        disposables.dispose()
    }

    override fun goToNextActivity(){
        view.goToNextActivity(userSessionInteractor.fetchUser())
    }

    private fun checkUpdate(){
        interactor.hasUpdates(version = VersionHelper.versionCode().toString())
                .ioThread()
                .doOnSubscribe { view.showLoading() }
                .doOnTerminate { view.hideLoading() }
                .subscribe({
                    when {
                        VerifyUpdateStauts.force == it.status -> view.forceUpdate()
                        VerifyUpdateStauts.optional == it.status -> view.optionalUpdate()
                        else -> goToNextActivity()
                    }
                }, {t ->
                    goToNextActivity()
                    Log.e("SplashPresenter", t.message, t)
                }).addTo(disposables)
    }


}