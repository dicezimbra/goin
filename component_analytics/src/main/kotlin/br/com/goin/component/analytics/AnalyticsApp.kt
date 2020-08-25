package br.com.goin.component.analytics

import android.annotation.SuppressLint
import android.content.Context
import com.adjust.sdk.Adjust
import com.adjust.sdk.AdjustConfig
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsApp{

    companion object {
        @SuppressLint("StaticFieldLeak")
        var instance: AnalyticsApp = AnalyticsApp()
    }

    var context: Context? = null

    fun onCreate(context: Context? = null){
        this.context = context
        context?.let {
            configureAdjust(context)
            configureFirebaseUserProperties(context)
        }
    }

    private fun configureFirebaseUserProperties(context: Context){
        FirebaseAnalytics.getInstance(context).setUserProperty("flavor", BuildConfig.FLAVOR)
        FirebaseAnalytics.getInstance(context).setUserProperty("isDebug", BuildConfig.DEBUG.toString())
    }

    private fun configureAdjust(context: Context) {
        val appToken = BuildConfig.ADJUST_TOKEN
        val environment = AdjustConfig.ENVIRONMENT_PRODUCTION
        val config = AdjustConfig(context, appToken, environment)
        Adjust.onCreate(config)
    }
}