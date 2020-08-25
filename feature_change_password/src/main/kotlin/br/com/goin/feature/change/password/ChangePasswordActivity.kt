package br.com.goin.feature.change.password

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import br.com.goin.base.utils.Utils

import br.com.goin.component.analytics.analytics.Analytics

import kotlinx.android.synthetic.main.activity_change_password_new.*

class ChangePasswordActivity : AppCompatActivity(), ChangePasswordView {

    val presenter: ChangePasswordPresenter = ChangePasswordPresenterImpl(this)

    companion object {
        fun starter(context: Context) {
            val intent = Intent(context, ChangePasswordActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password_new)
        presenter.onCreate()

        toolbar.title = getString(R.string.change_password_title)
        toolbar.setOnBackButtonClicked { finish() }
    }

    override fun onResume() {
        super.onResume()
        Analytics.instance.screenView(this, getString(R.string.change_password_screen_name))
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun configureOnClickListener() {
        send_button.setOnClickListener {
            presenter.logOnAnalytics(getString(R.string.analytics_change_password_change_action))

            val oldPassword = old_password.text.toString()
            val password = new_password_pass.text.toString()
            val passwordConfirmation = new_password_confirm.text.toString()
            presenter.onClickSend(oldPassword, password, passwordConfirmation)
        }
    }

    override fun changePasswordSuccess() {
        Utils.showToastShort(this, getString(R.string.change_password_success))
        finish()
    }

    override fun changePasswordError(error: String?) {
        Utils.showAlertWithCallBack(this, "", error!!, getString(R.string.alert_ok)) { _, _ -> }
    }

    override fun showAlertOldPasswordEqualNewPassword() {
        Utils.showAlertWithCallBack(this, "", getString(R.string.new_password_same_old), getString(R.string.alert_ok)) { _, _ -> }
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
        form_group.visibility = View.GONE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
        form_group.visibility = View.VISIBLE
    }

    override fun isValidPassword(isValid: Boolean, typeMessage: ChangePasswordPresenterImpl.PasswordError) {
        text_input_old_password.isErrorEnabled = true
        text_input_confirm.isErrorEnabled = true
        text_input_new_password_pass.isErrorEnabled = true

        when {
            !isValid && typeMessage == ChangePasswordPresenterImpl.PasswordError.NOT_EQUALS -> {
                text_input_new_password_pass.error = getString(R.string.different_passwords)
                text_input_confirm.error = getString(R.string.different_passwords)
            }
            !isValid && typeMessage == ChangePasswordPresenterImpl.PasswordError.EMPTY -> {
                old_password.error = getString(R.string.fill_all_fields_password)
                text_input_new_password_pass.error = getString(R.string.fill_all_fields_password)
                text_input_confirm.error = getString(R.string.fill_all_fields_password)
            }
            !isValid && typeMessage == ChangePasswordPresenterImpl.PasswordError.INVALID_PASSWORD -> {
                text_input_new_password_pass.error = getString(R.string.invalid_password_error)
                text_input_confirm.error = getString(R.string.invalid_password_error)
            }
        }
    }
}