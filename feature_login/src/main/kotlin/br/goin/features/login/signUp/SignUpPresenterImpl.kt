package br.goin.features.login.signUp

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.utils.Utils.isValidPassword
import br.com.goin.component.analytics.adjust.TagManager
import br.com.goin.component.analytics.adjust.TagManagerImpl
import br.com.goin.component.rest.api.helpers.ErrorHelper
import br.com.goin.features.signUp.SignUpView
import br.goin.feature.login.R
import br.goin.features.login.model.UserInteractor
import io.reactivex.disposables.Disposable

private const val EMAIL_EXIST_ERROR = "An account with the given email already exists."

class SignUpPresenterImpl(val view: SignUpView) : SignUpPresenter {
    private var disposable: Disposable? = null
    private val userInteractor: UserInteractor = UserInteractor.instance

    override fun onCreate() {
        view.hideLoading()
        view.setupTypeface()
        view.setupClickListeners()
    }

    override fun onSignUp(email: String?, password: String?,
                          passwordConfirmation: String?, name: String?,
                          emailValid: Boolean, consentToUseTerms: Boolean) {
        if (!emailValid) {
            view.showDialogOnError(R.string.invalid_email)
            return
        } else if (name.isNullOrBlank()) {
            view.showDialogOnError(R.string.please_insert_name)
            return
        } else if (password.isNullOrEmpty()) {
            view.showDialogOnError(R.string.please_insert_password)
            return
        } else if (passwordConfirmation.isNullOrEmpty()) {
            view.showDialogOnError(R.string.please_confirm_password)
            return
        } else if (password.trim { it <= ' ' } != passwordConfirmation.trim { it <= ' ' }) {
            view.showDialogOnError(R.string.password_dont_match)
            return
        } else if (!isValidPassword(passwordConfirmation ?: "")) {
            view.showDialogOnError(R.string.invalid_password)
            return
        } else if (!consentToUseTerms) {
            view.showDialogOnError(R.string.invalid_checked)
            return
        } else {
            signUp(email!!, password, name)
        }
    }

    private fun signUp(email: String, password: String, name: String) {
        val emailValid = email.toLowerCase().trim { it <= ' ' }
        val nameValid = name.trim { it <= ' ' }

        val passwordValid = password.trim { it <= ' ' }

        val inviteCode = BranchIOHelper.getInvitedByCode()

        disposable = userInteractor.register(email = emailValid, password = passwordValid, name = nameValid, inviteCode = inviteCode)
                .ioThread()
                .doOnSubscribe { view.showLoading() }
                .doOnTerminate { view.hideLoading() }
                .subscribe({
                    tagAdjust()

                    view.saveEmailOnSharedPreferences()
                    view.goToChooseProfilePicture()
                    view.finishActivity()
                }, {
                    ErrorHelper.getErrorMessage(it)?.let { message ->
                        when (message) {
                            EMAIL_EXIST_ERROR -> view.showDialogOnError(R.string.email_exists)
                            else -> view.showDialogOnError(R.string.unkown_error)
                        }
                    }
                    Log.e("SignUpPresenterImpl", it.message)
                })
    }

    private fun tagAdjust() {
        val tagManager = TagManagerImpl()
        tagManager.trackAdjustEvent(TagManager.TOKEN_REGISTER)
    }

    override fun onDestroy() {
        disposable?.dispose()
    }


}