package br.com.goin.feature.change.password

import android.util.Log
import br.com.goin.component.session.user.UserSessionInteractor
import br.com.goin.component.rest.api.helpers.ErrorHelper
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.utils.Utils
import br.com.goin.feature.change.password.model.ChangePasswordInteractor

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

private const val OLD_PASSWORD_EQUAL_NEW_PASSWORD = "oldPassword and newPassword are equals"

class ChangePasswordPresenterImpl(val view: ChangePasswordView) : ChangePasswordPresenter {

    enum class PasswordError { NOT_EQUALS, EMPTY, INVALID_PASSWORD }

    private val changePasswordInteractor = ChangePasswordInteractor.instance
    private val userSessionInteractor = UserSessionInteractor.instance
    private var disposable = CompositeDisposable()

    private var email: String? = null

    override fun onCreate() {
        view.configureOnClickListener()
        userSessionInteractor.fetchUser()?.let {
            email = it.email
        }
    }

    override fun onDestroy() {
        disposable.dispose()
    }

    override fun onClickSend(oldPassword: String, password: String, passwordConfirmation: String) {
        when {
            password != passwordConfirmation -> {
                view.isValidPassword(false, PasswordError.NOT_EQUALS)
            }
            oldPassword.isEmpty() || password.isEmpty() || passwordConfirmation.isEmpty() -> {
                view.isValidPassword(false, PasswordError.EMPTY)
            }
            !Utils.isValidPassword(password) -> {
                view.isValidPassword(false, PasswordError.INVALID_PASSWORD)
            }
            else -> {
                changePassword(oldPassword, password, passwordConfirmation)
            }
        }
    }

    private fun changePassword(oldPassword: String, password: String, passwordConfirmation: String) {
        email?.let { email ->
            changePasswordInteractor.changePassword(email, oldPassword, password, passwordConfirmation)
                    .ioThread()
                    .doOnSubscribe { view.showLoading() }
                    .subscribe({
                        view.hideLoading()
                        view.changePasswordSuccess()

                    }, { throwable: Throwable ->
                        view.hideLoading()
                        Log.e("ChangePassword", throwable.message, throwable)

                        val errorMessage = ErrorHelper.getErrorMessage(throwable)
                        errorMessage?.let {
                            if (it == OLD_PASSWORD_EQUAL_NEW_PASSWORD) {
                                view.showAlertOldPasswordEqualNewPassword()
                            }
                        }
                    }).addTo(disposable)
        }
    }

    override fun logOnAnalytics(action: String, label: String?) {
    }
}