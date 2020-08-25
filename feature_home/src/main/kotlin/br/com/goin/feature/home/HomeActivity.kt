package br.com.goin.feature.home

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import br.com.goin.component.navigation.NavigationController
import br.com.goin.feature.home.goin.GoinFragment
import br.com.goin.feature.home.helper.FragmentHelper
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity(), HomeView {

    private val presenter: HomePresenter = HomePresenterImpl(this)
    private val navegationController = NavigationController.instance!!

    companion object {

        private const val DEEP_LINK_PARAM = "DEEP_LINK"

        fun starter(context: Context, deepLink: String) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra(DEEP_LINK_PARAM, deepLink)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        presenter.onReceiveDeepLink(intent.extras[DEEP_LINK_PARAM])
        presenter.onCreate()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        presenter.onReceiveDeepLink(intent?.extras?.get(DEEP_LINK_PARAM))
        presenter.onCreate()
    }

    override fun configureOnClickListeners() {
        tab_config_img.setOnClickListener {
            presenter.onClickTabConfig()
        }

        tab_home.setOnClickListener {
            presenter.onClickTabHome()
        }

        tab_wallet_img.setOnClickListener {
            presenter.onClickTabWallet()
        }

        tab_profile_img.setOnClickListener {
            presenter.onClickTabProfile()
        }

        tab_feed_img.setOnClickListener {
            presenter.onClickTabFeed()
        }
    }

    override fun goToPlaceholder(isWallet: Boolean){
        navegationController.legacyApp().goToPlaceholder(this, isWallet)
    }

    override fun goToProfile() {
        FragmentHelper.replace(
                this, R.id.fragment_container, navegationController.profileModule().getProfile())

    }

    override fun goToWallet() {
        FragmentHelper.replace(
                this, R.id.fragment_container, navegationController.legacyApp().getWallet())
    }

    override fun goToFeed() {
        FragmentHelper.replace(
                this, R.id.fragment_container, navegationController.feedModule().getFeed())
    }

    override fun goToConfig() {
        FragmentHelper.replace(
                this, R.id.fragment_container, navegationController.legacyApp().getConfig())
    }

    override fun goToHome() {
        FragmentHelper.replace(
                this, R.id.fragment_container, GoinFragment.newInstance())
    }

    override fun configureTabProfile() {
        resetConfigureTabs()
        tab_profile_img.alpha = 0.5f
        setButtonBackgroundTint(tab_profile_img, R.color.notTooBlack)
    }

    override fun configureTabHome() {
        resetConfigureTabs()
    }

    override fun configureTabWallet() {
        resetConfigureTabs()
        tab_wallet_img.alpha = 0.5f
        setButtonBackgroundTint(tab_wallet_img, R.color.notTooBlack)
    }

    override fun configureTabFeed() {
        resetConfigureTabs()
        tab_feed_img.alpha = 0.5f
        setButtonBackgroundTint(tab_feed_img, R.color.notTooBlack)
    }

    override fun configureTabConfig() {
        resetConfigureTabs()
        tab_config_img.alpha = 0.5f
        setButtonBackgroundTint(tab_config_img, R.color.notTooBlack)
    }

    private fun resetConfigureTabs() {
        setButtonBackgroundTint(tab_feed_img, android.R.color.white)
        setButtonBackgroundTint(tab_profile_img, android.R.color.white)
        setButtonBackgroundTint(tab_wallet_img, android.R.color.white)
        setButtonBackgroundTint(tab_config_img, android.R.color.white)
        tab_feed_img.alpha = 1.0f
        tab_profile_img.alpha = 1.0f
        tab_wallet_img.alpha = 1.0f
        tab_config_img.alpha = 1.0f
    }

    private fun setButtonBackgroundTint(component: ImageView, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            component.setColorFilter(ContextCompat.getColor(applicationContext, color))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val goinFragment = supportFragmentManager.findFragmentByTag("GoinFragment")
        goinFragment?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}