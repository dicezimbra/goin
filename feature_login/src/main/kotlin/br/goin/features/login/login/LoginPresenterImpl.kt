package br.goin.features.login.login

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.component.push.UserPushEndpointInteractor
import br.com.goin.component.rest.api.helpers.ErrorHelper
import br.goin.feature.login.R
import br.goin.features.login.helper.ValidationHelper
import br.goin.features.login.model.UserInteractor

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import retrofit2.HttpException

private const val USER_NOT_FOUND = "UserNotFoundException"
private const val USER_NOT_AUTHORIZED = "NotAuthorizedException"

class LoginPresenterImpl(val view: LoginView) : LoginPresenter {

    private val userInteractor = UserInteractor.instance
    private val userPushEndpointInteractor = UserPushEndpointInteractor.instance
    private var disposables = CompositeDisposable()

    override fun onCreate() {
        view.setupViews()
        view.validateEmail()
        view.validatePassword()
    }

    override fun onStart() {
        view.setEmailFromSharedPreferences()
    }

    override fun onDestroy() {
        disposables.dispose()
    }

    override fun onFacebookLogged(accessToken: String, identityProvider: String) {
        userInteractor.loginWithFacebook(accessToken, identityProvider)
                .ioThread()
                .doOnSubscribe { view.showLoading() }
                .doOnTerminate { view.hideLoading() }
                .subscribe({
                    view.saveEmailOnSharedPreferences()
                    view.goToTabsMain()
                }, {
                    when (it) {
                        is HttpException ->
                            view.showDialogOnError(it.code())
                    }
                    Log.w("LoginPresenter", it.message)
                })
                .addTo(disposables)
    }

    override fun verifyIfIsValid(s: String?) {
        s?.let {
            if (!LoginActivityHelper.isValidEmail(it)) {
                view.notValidEmail()
            } else {
                view.validEmail()
            }
        }
    }

    override fun verifyPassword(pass: String) {
        if (pass.isNotEmpty()) {
            view.validPassField()
        } else {
            view.invalidPassField()
        }
    }

    override fun onSignIn(email: String?, password: String?, emailValid: Boolean) {
        email?.let {
            if (ValidationHelper.isValidEmail(it)) {
                if (password.equals("")) {
                    view.passwordNotValid()
                } else {
                    signIn(email, password)
                }
            }
        }
    }

    private fun signIn(email: String?, password: String?) {
        userInteractor.login(email ?: "", password ?: "")
                .ioThread()
                .doOnSubscribe { view.showLoading() }
                .doOnTerminate { view.hideLoading() }
                .subscribe({
                    setupFirebaseToken()
                    view.saveEmailOnSharedPreferences()
                    view.goToTabsMain()
                }, { t: Throwable ->
                    val error: String? = ErrorHelper.getErrorName(t)
                    when (error) {
                        USER_NOT_FOUND -> {
                            view.showDialogOnError(R.string.invalid_user)
                        }
                        USER_NOT_AUTHORIZED -> {
                            view.showDialogOnError(R.string.invalid_user)
                        }
                        else -> {
                            view.showDialogOnError(R.string.unkown_error)
                        }
                    }

                    Log.e("LoginPresenter", t.localizedMessage)
                }).addTo(disposables)
    }

    private fun setupFirebaseToken() {
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("FirebaseInstanceId", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    val token = task.result?.token
                    userPushEndpointInteractor.updateMessagingToken(token = token)
                            .subscribe({}, { throwable: Throwable ->
                                Log.e("LoginPresenter", throwable.message, throwable)
                            }).addTo(disposables)
                })
    }
}