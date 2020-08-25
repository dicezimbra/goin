package br.goin.features.login.signUp

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import br.com.goin.base.extensions.isValidEmail
import br.com.goin.base.helpers.KeyboardHelper
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.navigation.TermsNavigationController
import br.com.goin.component.ui.kit.dialog.DialogUtils
import br.com.goin.features.signUp.SignUpView
import br.goin.feature.login.R
import br.goin.features.login.signUp.choosePicture.ChoosePictureActivity
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*

private const val TERMS_OF_USE = "https://s3.amazonaws.com/goin-public/termos_de_uso.pdf"
private const val HALF_PRICE_POLICY = "https://goin.com.br/Politica_Meia_Entrada.pdf"
private const val PRIVACY_POLICY = "https://s3.amazonaws.com/goin-public/politica_de_privacidade.pdf"


class SignUpActivity : AppCompatActivity(), SignUpView {

    private val presenter: SignUpPresenter = SignUpPresenterImpl(this)
    private var fromApp: Boolean = false
    private var loginManager = br.goin.features.login.manager.LoginManager.instance

    enum class SexTypes {
        Female, Male, Other
    }

    companion object {
        fun starter(context: Context) {
            context.startActivity(Intent(context, SignUpActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        intent?.let {
            fromApp = it.getBooleanExtra("fromApp", false)
        }
        presenter.onCreate()
    }

    override fun onResume() {
        super.onResume()
        Analytics.instance.screenView(this, getString(R.string.register_screen_name))
    }

    override fun setupTypeface() {
        btn_sign_up.typeface = Typeface.createFromAsset(this.assets, "fonts/Quicksand-Medium.ttf")
        input_layout_password.typeface = Typeface.createFromAsset(this.assets, "fonts/Quicksand-Medium.ttf")
        input_layout_confirm_password.typeface = Typeface.createFromAsset(this.assets, "fonts/Quicksand-Medium.ttf")
        input_edit_text_password.typeface = Typeface.createFromAsset(this.assets, "fonts/Quicksand-Medium.ttf")
        input_edit_text_confirm_password.typeface = Typeface.createFromAsset(this.assets, "fonts/Quicksand-Medium.ttf")
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun setupClickListeners() {
        signup_after_background_frame.setOnClickListener { KeyboardHelper.hideKeyboard(it, this@SignUpActivity) }
        btn_sign_up.setOnClickListener { doSignup() }
        toolbar_sign_up.setNavigationOnClickListener { finishActivity() }
    }

    override fun setupTextTerms() {
        val termsOfUseSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                goToActivityTerms(hashMapOf("terms_of_use" to getString(R.string.terms_of_use_lorem), "terms" to TERMS_OF_USE))
            }
        }

        val privacyPolicySpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                goToActivityTerms(hashMapOf("privacy_policy" to getString(R.string.privacy_policy), "privacy" to PRIVACY_POLICY))
            }
        }

        val styledString = SpannableString(getString(R.string.privacy_terms))

        if (Locale.getDefault().language == "pt") {
            styledString.setSpan(termsOfUseSpan, 48, 66, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            styledString.setSpan(privacyPolicySpan, 71, 90, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        } else {
            styledString.setSpan(termsOfUseSpan, 58, 78, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            styledString.setSpan(privacyPolicySpan, 87, 101, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        }

        text_terms_and_conditions?.run {
            movementMethod = LinkMovementMethod.getInstance()
            text = styledString
        }
    }

    private fun goToActivityTerms(hashMap: HashMap<String, String>) {
        Intent(this, TermsNavigationController::class.java).apply {
            for (element in hashMap) {
                this.putExtra(element.key, element.value)
            }
            startActivity(this)
        }
        overridePendingTransition(R.anim.slide_in_anim, R.anim.slide_out_anim)
    }

    private fun doSignup() {
        KeyboardHelper.hideKeyboard(signup_after_background_frame, this)

        val email = input_edit_text_email.text.toString()
        val password = input_edit_text_password.text.toString()
        val passwordConfirmation = input_edit_text_confirm_password.text.toString()
        val name = input_edit_text_name.text.toString()

        presenter.onSignUp(email, password,
                passwordConfirmation, name,
                email.isValidEmail(), check_box_sign_up.isChecked)
    }

    override fun hideLoading() {
        form_group.visibility = View.VISIBLE
        progress_bar.visibility = View.GONE
    }

    override fun showLoading() {
        form_group.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE
    }

    override fun saveEmailOnSharedPreferences() {
        SignUpHelper.setEmailCredentials(this, input_edit_text_email.text.toString())
    }

    override fun goToChooseProfilePicture() {
        if (!fromApp) {
            ChoosePictureActivity.starter(this)
        }
    }

    override fun finishActivity() {
        presenter.onDestroy()

        if (fromApp) {
            setResult(RESULT_OK, this.intent)
            loginManager.loginComplete()
        }

        finish()
    }

    override fun showDialogOnError(@StringRes error: Int) {
        DialogUtils.show(this@SignUpActivity, resources.getString(error))
    }
}