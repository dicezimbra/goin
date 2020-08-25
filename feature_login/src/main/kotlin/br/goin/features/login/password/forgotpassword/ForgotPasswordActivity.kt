package br.goin.features.login.password.forgotpassword

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.com.goin.base.utils.Utils
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.features.password.forgotpassword.ForgotPasswordPresenter
import br.goin.feature.login.R
import br.goin.features.login.password.newpassword.NewPasswordActivity
import br.goin.features.login.helper.DialogHelper
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_forgot_password_new.*
import java.util.concurrent.TimeUnit

class ForgotPasswordActivity : AppCompatActivity(), ForgotPasswordView {

    var presenter: ForgotPasswordPresenter = ForgotPasswordPresenterImpl(this)
    private var disposables = CompositeDisposable()

    companion object {
        fun starter(context: Context, fromApp: Boolean) {
            val intent = Intent(context, ForgotPasswordActivity::class.java)
            intent.putExtra("fromApp", fromApp)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password_new)

        presenter.onCreate()
    }

    override fun onResume() {
        super.onResume()
        Analytics.instance.screenView(this, getString(R.string.forgot_password_screen_name))
    }

    override fun configureOnClickListener() {
        toolbar_forgot_password.setNavigationOnClickListener { onBackPressed() }
        send_button.setOnClickListener {
            val email = login_email_field.text.toString()
            presenter.forgotPassword(email)
        }
        send_button.typeface = Typeface.createFromAsset(this.assets, "fonts/Quicksand-Medium.ttf")
    }

    override fun forgotPasswordSuccess() {
        DialogHelper.showOkDialog(this, getString(R.string.forgot_password_send_code_success)) {
            sendToPin()
        }
    }

    override fun sendToPin() {
        NewPasswordActivity.starter(this, login_email_field.text.toString())
    }

    override fun showErrorEmailNotFound() {
        Utils.showAlertWithCallBack(this, "", getString(R.string.email_not_found), getString(R.string.alert_ok)) { _, _ -> }
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
        form_group.visibility = View.GONE
    }

    override fun hideLoading() {
        form_group.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    override fun emailNotValid() {
        edit_view.error = getString(R.string.invalidEmail)
        edit_view.typeface = Typeface.createFromAsset(this.assets, "fonts/Quicksand-Medium.ttf")
        send_button.isEnabled = false
        send_button.alpha = 0.4f
    }

    override fun emailIsValid() {
        edit_view.error = null
        send_button.isEnabled = true
        send_button.alpha = 1.0f
    }

    override fun configureTextChanges() {

        login_email_field.textChanges()
                .skipInitialValue()
                .doOnNext { }
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe { email ->
                    runOnUiThread { presenter.verifyIfEmaildValid(email.toString()) }
                }.addTo(disposables)
    }

}