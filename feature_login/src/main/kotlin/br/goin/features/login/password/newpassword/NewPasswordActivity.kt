package br.goin.features.login.password.newpassword

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.features.password.newpassword.NewPasswordHelper
import br.com.goin.features.password.newpassword.NewPasswordPresenter
import br.com.goin.features.password.newpassword.NewPasswordPresenterImpl
import br.com.goin.features.password.newpassword.NewPasswordView
import br.goin.feature.login.R
import br.goin.features.login.login.LoginActivity
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.new_password_activity.*
import java.util.concurrent.TimeUnit

class NewPasswordActivity : AppCompatActivity(), NewPasswordView {

    private val presenter: NewPasswordPresenter = NewPasswordPresenterImpl(this)
    private val disposables = CompositeDisposable()
    private var emailExtras: String? = null

    companion object {
        const val EMAIL = "EMAIL"

        fun starter(context: Context, email: String) {
            val intent = Intent(context, NewPasswordActivity::class.java)
            intent.putExtra(EMAIL, email)
            context.startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_password_activity)

        presenter.onReceiveEmailExtras(intent.getSerializableExtra(EMAIL))
        presenter.onCreate()
    }

    override fun onResume() {
        super.onResume()
        Analytics.instance.screenView(this, getString(R.string.forgot_password_screen_name_2))
    }

    override fun getEmail(email: String) {
        emailExtras = email
    }

    override fun configureOnClickListeners() {
        toolbar_new_password.setNavigationOnClickListener { onBackPressed() }
        btn_save_new_password.setOnClickListener {
            presenter.validatePassword(emailExtras!!, pin_edit_text.text.toString(), input_layout_new_password.text.toString(), input_layout_confirm_new_password.text.toString())
        }
        textInputLayoutPass.typeface = Typeface.createFromAsset(this.assets, "fonts/Quicksand-Medium.ttf")
        textInputLayoutConfirm.typeface = Typeface.createFromAsset(this.assets, "fonts/Quicksand-Medium.ttf")

        pin_edit_text.setOnEditorActionListener { v, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    NewPasswordHelper.hideKeyboard(v, this@NewPasswordActivity)
                    true
                }
                else -> false
            }
        }
    }

    override fun sendToSignUp() {
        val handle = Handler()
        handle.postDelayed({ callIntent() }, 2000)
    }

    override fun callIntent() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun showLoading() {
        group_constraints_new_password.visibility = View.GONE
        progress_bar_new_password.visibility = View.VISIBLE
        your_pass_error.visibility = View.GONE
    }

    override fun hideLoading() {
        progress_bar_new_password.visibility = View.GONE
        your_pass_error.visibility = View.GONE
    }

    override fun resetPasswordSuccess() {
        your_pass_error.visibility = View.GONE
        pass_changed_successfully.visibility = View.VISIBLE
        group_constraints_new_password.visibility = View.GONE
        progress_bar_new_password.visibility = View.GONE
    }


    override fun hideMessageCapitalize() {
        your_pass_error.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    override fun configureMatchPasswords() {
        input_layout_new_password.textChanges()
                .skipInitialValue()
                .doOnNext { }
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe { newPassword ->
                    runOnUiThread { presenter.onValidateCapitalLetter(newPassword.toString(), pin_edit_text.text.toString()) }
                }.addTo(disposables)

        input_layout_confirm_new_password.textChanges()
                .skipInitialValue()
                .doOnNext { }
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe { confirmNewPasssword ->
                    runOnUiThread { presenter.onValidatePasswordEquals(confirmNewPasssword.toString(), input_layout_new_password.text.toString()) }
                }.addTo(disposables)

    }

    override fun showErrorPasswordsDontMatch() {
        input_layout_confirm_new_password.error = getString(R.string.password_dont_match)
        your_pass_error.visibility = View.VISIBLE
        btn_save_new_password.alpha = 0.4f
        btn_save_new_password.isEnabled = false
    }

    override fun hideErrorPasswordsDontMatch() {

        pin_edit_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (start == 5 && input_layout_new_password.text!!.isNotEmpty() && input_layout_confirm_new_password.text!!.isNotEmpty()) {

                    input_layout_confirm_new_password.error = null
                    your_pass_error.visibility = View.VISIBLE
                    btn_save_new_password.alpha = 1.0f
                    btn_save_new_password.isEnabled = true
                }
            }
        })
    }


    override fun showErrorMessageCapitalize() {

        val builder = SpannableStringBuilder()
        val span1 = SpannableString("" + resources.getString(R.string.your_pass) + "")
        val span2 = SpannableString(" " + resources.getString(R.string.capital_letter) + " ")
        val span3 = SpannableString("" + resources.getString(R.string.and_at_least) + "")
        val span4 = SpannableString(" " + resources.getString(R.string.eight_chars) + " ")

        val spanYourPass = SpannableString(span1)
        if (your_pass_error.length() > 0) {
            spanYourPass.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.new_gray)), 0, span1.length, 0)
            builder.append(spanYourPass)
        }

        val spanCapitalLetter = SpannableString(span2)
        if (your_pass_error.length() > 0) {
            spanCapitalLetter.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.red)), 0, span2.length, 0)
            spanCapitalLetter.setSpan(StyleSpan(Typeface.BOLD), 0, span2.length, 0)
            builder.append(spanCapitalLetter)
        }

        val spanAtLeast = SpannableString(span3)
        if (your_pass_error.length() > 0) {
            spanAtLeast.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.new_gray)), 0, span3.length, 0)
            builder.append(spanAtLeast)
        }

        val spanEightChars = SpannableString(span4)
        if (your_pass_error.length() > 0) {
            spanEightChars.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.red)), 0, span4.length, 0)
            spanEightChars.setSpan(StyleSpan(Typeface.BOLD), 0, span4.length, 0)
            builder.append(spanEightChars)
        }

        your_pass_error.setText(builder, TextView.BufferType.SPANNABLE)
        your_pass_error.visibility = View.VISIBLE
        btn_save_new_password.alpha = 0.4f
        btn_save_new_password.isEnabled = false

    }

    override fun showCorretPasswordMessageCapitalize() {
        val builder = SpannableStringBuilder()
        val span1 = SpannableString("" + resources.getString(R.string.your_pass) + "")
        val span2 = SpannableString(" " + resources.getString(R.string.capital_letter) + " ")
        val span3 = SpannableString("" + resources.getString(R.string.and_at_least) + "")
        val span4 = SpannableString(" " + resources.getString(R.string.eight_chars) + " ")

        val spanYourPass = SpannableString(span1)
        if (your_pass_error.length() > 0) {
            spanYourPass.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.new_gray)), 0, span1.length, 0)
            builder.append(spanYourPass)
        }

        val spanCapitalLetter = SpannableString(span2)
        if (your_pass_error.length() > 0) {
            spanCapitalLetter.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.light_green)), 0, span2.length, 0)
            spanCapitalLetter.setSpan(StyleSpan(Typeface.BOLD), 0, span2.length, 0)
            builder.append(spanCapitalLetter)
        }

        val spanAtLeast = SpannableString(span3)
        if (your_pass_error.length() > 0) {
            spanAtLeast.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.new_gray)), 0, span3.length, 0)
            builder.append(spanAtLeast)
        }

        val spanEightChars = SpannableString(span4)
        if (your_pass_error.length() > 0) {
            spanEightChars.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, R.color.light_green)), 0, span4.length, 0)
            spanEightChars.setSpan(StyleSpan(Typeface.BOLD), 0, span4.length, 0)
            builder.append(spanEightChars)
        }

        your_pass_error.setText(builder, TextView.BufferType.SPANNABLE)

    }

}