package br.goin.features.login.password.forgotpassword

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.component.session.user.UserSessionInteractor
import br.com.goin.features.password.forgotpassword.ForgotPasswordPresenter
import br.goin.features.login.helper.ValidationHelper
import br.goin.features.login.password.forgotpassword.model.ForgotPasswordInteractor
import io.reactivex.disposables.Disposable

private const val EMAIL_NOT_FOUND_ERROR = "Username/client id combination not found."
private const val INTERNAL_SERVER_ERROR = "Internal server error"
private const val INTERNAL_SERVER_ERROR_CODE = "502"

class ForgotPasswordPresenterImpl(val view: ForgotPasswordView) : ForgotPasswordPresenter {

    private val interactor = ForgotPasswordInteractor.instance
    private var disposable: Disposable? = null

    override fun onCreate() {
        view.configureOnClickListener()
        view.configureTextChanges()
    }

    override fun onDestroy() {
        disposable?.dispose()
    }


    override fun verifyIfEmaildValid(email: String) {
        if (ValidationHelper.isValidEmail(email)) {
            view.emailIsValid()
        } else {
            view.emailNotValid()
        }
    }

    override fun forgotPassword(email: String) {
        disposable = interactor.forgotPassword(email)
                .ioThread()
                .doOnSubscribe { view.showLoading() }
                .subscribe({
                    view.forgotPasswordSuccess()
                    view.hideLoading()
                }, { throwable: Throwable ->
                    view.showErrorEmailNotFound()
                    view.hideLoading()
                    Log.e("NewPassword", throwable.message, throwable)
                })
    }
}