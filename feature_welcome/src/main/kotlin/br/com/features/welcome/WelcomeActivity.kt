package br.com.features.welcome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.goin.base.helpers.PreferenceHelper
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.navigation.NavigationController
import com.example.feature_welcome.R
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity(), WelcomeView {

    private val presenter: WelcomePresenter = WelcomePresenterImpl(this)
    private val navigationController = NavigationController.instance
    private var adapterWelcome: WelcomeAdapter? = null

    companion object {
        fun starter(context: Context) {
            context.startActivity(Intent(context, WelcomeActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        presenter.onCreate()
    }

    override fun configureAdapter() {
        adapterWelcome = WelcomeAdapter(this)
        adapterWelcome?.signUpClickListener = {
            presenter.startLogIn()
        }
        adapterWelcome?.notNowClickListener = {
            presenter.startHome()
        }

        view_page_container_welcome.adapter = adapterWelcome
        dots.setupWithViewPager(view_page_container_welcome)
    }

    override fun onResume() {
        super.onResume()
        Analytics.instance.screenView(this, getString(R.string.onboarding_screen_view))
    }

    override fun goToHome() {
        navigationController?.homeModule()?.goToHome(this)
        PreferenceHelper.write("firstTime", false)
        finish()
    }

    override fun goToLogIn() {
        navigationController?.loginModule()?.goToLogin(this) {}
        PreferenceHelper.write("firstTime", false)
        finish()
    }

    override fun onBackPressedButton() {
        finish()
    }
}