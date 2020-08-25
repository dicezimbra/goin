package br.goin.features.login.login.preview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.goin.base.helpers.PreferenceHelper
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.navigation.NavigationController
import br.com.goin.component.session.user.UserModel
import br.com.goin.component.ui.kit.dialog.DialogUtils
import br.goin.feature.login.R
import br.goin.features.login.login.LoginActivity
import br.goin.features.login.login.facebook.FacebookLoginHelper
import br.goin.features.login.manager.LoginManager
import br.goin.features.login.signUp.SignUpActivity
import kotlinx.android.synthetic.main.activity_login_preview.*

private const val IDENTITY_PROVIDER = "graph.facebook.com"

class LoginPreviewActivity : AppCompatActivity(), LoginPreviewView {

    private val presenter: LoginPreviewPresenter = LoginPreviewPresenterImpl(this)
    private var fromApp: Boolean? = null
    private var facebookLoginHelper = FacebookLoginHelper()

    companion object {
        fun starter(context: Context, fromApp: Boolean) {
            val intent = Intent(context, LoginPreviewActivity::class.java)
            intent.putExtra("fromApp", fromApp)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_preview)

        fromApp = intent?.extras?.getBoolean("fromApp") ?: false
        presenter.onCreate()
    }

    override fun onResume() {
        super.onResume()
        Analytics.instance.screenView(this, getString(R.string.login_home_screen_name))
    }

    override fun onConfigureClickListeners() {
        btn_sign_up_preview.setOnClickListener { signUp() }
        btn_sign_up_facebook_preview.setOnClickListener { facebookSignUp() }
        btn_sign_up_google.setOnClickListener { googleSignUp() }
        btn_login_preview.setOnClickListener { signIn() }
    }

    override fun facebookSignUp() {
        facebookLoginHelper.login(this) { accessToken ->
            presenter.onFacebookLogged(accessToken, IDENTITY_PROVIDER)
        }
    }

    override fun goToTabsMain() {
        NavigationController.instance?.homeModule()?.goToHome(this)
    }

    override fun saveEmailOnSharedPreferences(userModel: UserModel) {
        PreferenceHelper.write("email", userModel.email)
    }

    override fun googleSignUp() {
    }

    override fun signIn() {
        if (fromApp!!) {
            LoginActivity.starter(this, fromApp!!)
        } else {
            NavigationController.instance?.homeModule()?.goToHome(this)
            finish()
        }
    }

    override fun signUp() {
        SignUpActivity.starter(this)
    }

    override fun showDialogOnError(code: Int) {
        DialogUtils.show(this, code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookLoginHelper.onActivityResult(requestCode, resultCode, data)
        if(requestCode == LoginActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK){
            finish()
            LoginManager.instance.loginComplete()
        }
    }
}