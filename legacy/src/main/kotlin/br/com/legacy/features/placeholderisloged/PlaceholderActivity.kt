package br.com.legacy.features.placeholderisloged

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.R
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.navigation.NavigationController

import kotlinx.android.synthetic.main.fragment_placeholder_feed.*

class PlaceholderActivity : AppCompatActivity() {

    companion object {

        const val IS_WALLET = "IS_WALLET"

        fun starter(context: AppCompatActivity) {
            val intent = Intent(context, PlaceholderActivity::class.java)
            context.startActivity(intent)
            context.overridePendingTransition(R.anim.slide_in_bottom_to_top, R.anim.slide_out_top_to_bottom)
        }

        fun starterForWallet(context: AppCompatActivity) {
            val intent = Intent(context, PlaceholderActivity::class.java)
            intent.putExtra(IS_WALLET, true)
            context.startActivity(intent)
            context.overridePendingTransition(R.anim.slide_in_bottom_to_top, R.anim.slide_out_top_to_bottom)
        }
    }

    private val navegationController = NavigationController.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(intent.getBooleanExtra(IS_WALLET, false)) {
            setContentView(R.layout.fragment_placeholder_feed)
        }else{
            setContentView(R.layout.fragment_placeholder_wallet)
        }

        buttonGoToStart.setOnClickListener {
            navegationController?.loginModule()?.goToLogin(this) {
                finish()
            }
        }

        textViewNotNow.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if(intent.getBooleanExtra(IS_WALLET, false)) {
            Analytics.instance.screenView(this, getString(R.string.onboarding_social_screen_view))
        }else{
            Analytics.instance.screenView(this, getString(R.string.onboarding_organizer_screen_view))
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_bottom_to_top, R.anim.slide_out_top_to_bottom)
    }
}