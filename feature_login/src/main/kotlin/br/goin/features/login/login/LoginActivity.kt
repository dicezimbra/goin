package br.goin.features.login.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.com.goin.base.extensions.isValidEmail
import br.com.goin.base.helpers.KeyboardHelper
import br.com.goin.base.utils.Utils
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.navigation.NavigationController
import br.com.goin.component.ui.kit.dialog.DialogUtils
import br.goin.feature.login.R
import br.goin.features.login.signUp.choosePicture.ChoosePictureActivity
import br.goin.features.login.password.forgotpassword.ForgotPasswordActivity
import br.goin.features.login.login.facebook.FacebookLoginHelper
import br.goin.features.login.manager.LoginManager
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit

private const val IDENTITY_PROVIDER = "graph.facebook.com"

class LoginActivity : AppCompatActivity(), LoginView {

    private val presenter: LoginPresenter = LoginPresenterImpl(this)
    private var disposables = CompositeDisposable()
    private val facebookHelper = FacebookLoginHelper()
    private var fromApp: Boolean? = null

    companion object {
        const val REQUEST_CODE = 1005

        fun starter(context: Activity, fromApp: Boolean) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra("fromApp", fromApp)
            context.startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar_login)
        supportActionBar?.title = ""

        fromApp = intent?.extras?.getBoolean("fromApp") ?: false
        presenter.onCreate()
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()

        Analytics.instance.screenView(this, getString(R.string.login_screen_view))
        input_e.setText(LoginActivityHelper.getEmailCredentials(this))
    }

    override fun onResume() {
        super.onResume()
        input_e.setText(LoginActivityHelper.getEmailCredentials(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
        presenter.onDestroy()
    }

    override fun setupViews() {
        login_after_background.setOnClickListener { KeyboardHelper.hideKeyboard(it, this@LoginActivity) }
        input_password.setOnFocusChangeListener { _, focused -> onFocusChanged(focused) }
        login_btn_Login.setOnClickListener { signIn() }
        forgot_password_button.setOnClickListener { goToForgotPassword() }
        toolbar_login.setOnBackButtonClicked { onBackPressed() }
        login_btn_facebook.setOnClickListener { loginWithFacebook() }
        input_layout_password.typeface = Typeface.createFromAsset(this.assets, "fonts/Quicksand-Medium.ttf")
    }

    private fun onFocusChanged(focused: Boolean) {
        if (focused) {
            scrollView?.scrollTo(0, login_btn_Login.bottom)
        }
    }

    override fun showDialogOnError(invalid_user: Int) {
        form_group.visibility = View.VISIBLE
        progress_bar.visibility = View.GONE
        DialogUtils.show(this, invalid_user)

    }

    override fun validateEmail() {
        input_e.textChanges()
                .skipInitialValue()
                .doOnNext { }
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe { email ->
                    if (email.isNotEmpty()) {
                        runOnUiThread { presenter.verifyIfIsValid(email.toString()) }
                    }
                }.addTo(disposables)
    }

    override fun validEmail() {
        input_layout_email.error = null
    }

    override fun validatePassword() {
        input_password.textChanges()
                .skipInitialValue()
                .doOnNext { }
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe { password ->
                    runOnUiThread { presenter.verifyPassword(password.toString()) }
                }.addTo(disposables)
    }

    override fun validPassField() {
        login_btn_Login.alpha = 1.0f
        login_btn_Login.isEnabled = true
    }

    override fun invalidPassField() {
        login_btn_Login.alpha = 0.4f
        login_btn_Login.isEnabled = false
    }


    override fun notValidEmail() {
        login_btn_Login.alpha = 0.4f
        input_layout_email.error = getString(R.string.enter_a_valid_email)
        login_btn_Login.isEnabled = false
        input_layout_email.typeface = Typeface.createFromAsset(this.assets, "fonts/Quicksand-Medium.ttf")
    }

    override fun passwordNotValid() {
        input_layout_password.error = getString(R.string.enter_a_valid_password)
    }

    private fun signIn() {
        KeyboardHelper.hideKeyboard(login_after_background, this@LoginActivity)
        presenter.onSignIn(input_e?.text.toString(), input_password?.text.toString(), input_e.text.toString().isValidEmail())
    }

    private fun loginWithFacebook() {
        facebookHelper.login(this) { accessToken ->
            presenter.onFacebookLogged(accessToken, IDENTITY_PROVIDER)
        }
    }

    override fun hideLoading() {
        input_layout_password.error = null
        form_group.visibility = View.VISIBLE
        progress_bar.visibility = View.GONE
    }

    override fun showLoading() {
        form_group.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE
    }

    override fun saveEmailOnSharedPreferences() {
        LoginActivityHelper.setEmailCredentials(this, input_e?.text.toString())
    }

    override fun setEmailFromSharedPreferences() {
        val isFieldEmpty = input_e?.text?.isEmpty() ?: false
        if (!isFieldEmpty) {
            input_e?.setText(LoginActivityHelper.getEmailCredentials(this))
        }
    }

    override fun savePasswordOnSharedPreferences(passwordEncrypted: String) {
        LoginActivityHelper.setPassCredentials(this, passwordEncrypted)
    }

    override fun getPasswordFromSharedPreferences(): String? {
        return LoginActivityHelper.getPassCredentials(this)
    }

    override fun goToForgotPassword() {
        forgot_password_button?.startAnimation(Utils.setClickEffect())
        ForgotPasswordActivity.starter(this, fromApp!!)
    }

    override fun goToChooseProfilePicture() {
        if (fromApp!!) {
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            ChoosePictureActivity.starter(this)
        }
    }

    override fun goToTabsMain() {
        if (fromApp!!) {
            setResult(Activity.RESULT_OK)
            finish()
        } else {
            NavigationController.instance?.homeModule()?.goToHome(this)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookHelper.onActivityResult(requestCode, resultCode, data)
    }
}
