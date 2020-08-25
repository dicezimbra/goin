package br.com.goin.features.password.newpassword

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.goin.features.login.model.UserInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.io.Serializable

class NewPasswordPresenterImpl(val view: NewPasswordView) : NewPasswordPresenter {

    private val userInteractor = UserInteractor.instance
    private var disposable = CompositeDisposable()
    private var email: String? = null

    override fun onCreate() {
        view.configureOnClickListeners()
        view.configureMatchPasswords()
    }

    override fun onReceiveEmailExtras(extra: Serializable) {
        extra.let {
            email = it as String
            view.getEmail(email!!)
        }
    }

    override fun validatePassword(email: String, pin: String, pass: String, confirmPass: String) {
        if (pass == confirmPass && pin.isNotEmpty() && email.isNotEmpty()) {
            userInteractor.resetPassword(pin, pass, email, confirmPass)
                    .ioThread()
                    .doOnSubscribe { view.showLoading() }
                    .subscribe({
                        view.resetPasswordSuccess()
                        view.sendToSignUp()
                        view.hideLoading()
                    }, { throwable: Throwable ->
                        view.hideLoading()
                        Log.e("NewPasswordPresenter", throwable.message, throwable)

                    }).addTo(disposable)
        }

    }

    override fun onValidateCapitalLetter(newPass: String, pin: String) {
        val hasUppercase = newPass != newPass.toLowerCase()
        val textLength = newPass.length >= 8

        if (!hasUppercase && !newPass.isEmpty() && (!textLength || textLength)) {
            view.showErrorMessageCapitalize()
        } else if (newPass.isEmpty()) {
            view.hideMessageCapitalize()
        } else if (!hasUppercase && newPass.startsWith(newPass.toUpperCase()) && !newPass.isEmpty()) {
            view.hideMessageCapitalize()
        } else if (hasUppercase && !newPass.isEmpty() && textLength) {
            view.showCorretPasswordMessageCapitalize()
        }
    }

    override fun onValidatePasswordEquals(confirm: String, newpass: String) {
        if (confirm != newpass || confirm.isEmpty()) {
            view.showErrorPasswordsDontMatch()
        } else {
            view.hideErrorPasswordsDontMatch()
        }
    }

}
