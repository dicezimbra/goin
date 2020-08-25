package br.com.goin.feature.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.goin.component.analytics.analytics.Analytics

import br.com.goin.component.session.user.UserModel
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.ArrayList

class SplashScreenActivity : AppCompatActivity(), SplashView {

    private val presenter: SplashPresenter = SplashPresenterImpl(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()
        Analytics.instance.screenView(this, getString(R.string.splash_screen_name))
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun forceUpdate() {
        val message = resources.getString(R.string.message_force_udpate)

        AlertDialog.Builder(this)
                .setTitle(resources.getString(R.string.title_update))
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(resources.getString(R.string.update_app)) { _, _ -> GooglePlayHelper.openGooglePlay(this) }
                .show()
    }

    override fun optionalUpdate() {
        val message = resources.getString(R.string.message_optional_udpate)

        AlertDialog.Builder(this)
                .setTitle(resources.getString(R.string.title_update))
                .setMessage(message)
                .setCancelable(true)
                .setOnCancelListener { presenter.goToNextActivity() }
                .setPositiveButton(resources.getString(R.string.update_app)) { _, _ -> GooglePlayHelper.openGooglePlay(this) }
                .setNegativeButton(resources.getString(R.string.update_not_now)) { _, _ -> presenter.goToNextActivity() }
                .show()
    }

    override fun goToNextActivity(userModel: UserModel?){
        val type = this.intent.getStringExtra("type")

        var uri: String? = null
        val data = this.intent.data
        if (data != null && data.isHierarchical) {
            uri = this.intent.dataString
        }

        val hasNotification = type != null
        val hasUrlParams = uri != null

        if (hasUrlParams) {
            val destination: String?
            val args = ArrayList<String>()
            destination = NavigationManager.extractUriArgs(this, uri!!, args)
            val isSelected = NavigationManager.selectDestination(this, destination, args)

            if(!isSelected){
                DeepLinkManager.selectedDestination(this, intent)
            }

        } else if (hasNotification && userModel != null) {
            val isSelected = NavigationManager.selectDestination(this, type, NavigationManager.extractNotificationArgs(this, type))

            if(!isSelected){
                DeepLinkManager.selectedDestination(this, intent)
            }
        } else {
            DeepLinkManager.selectedDestination(this, intent)
        }
    }

    public override fun onNewIntent(intent: Intent) {
        this.intent = intent
    }

    override fun showLoading(){
        progress_bar.visibility = View.VISIBLE
    }

    override fun hideLoading(){
        progress_bar.visibility = View.GONE
    }
}
