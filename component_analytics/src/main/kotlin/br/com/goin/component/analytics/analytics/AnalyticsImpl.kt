package br.com.goin.component.analytics.analytics

import android.app.Activity
import android.os.Bundle
import br.com.goin.component.analytics.AnalyticsApp
import com.google.firebase.analytics.FirebaseAnalytics

private const val CATEGORY = "CATEGORY"
private const val VALUE = "VALUE"

class AnalyticsImpl : Analytics {

    private val firebaseAnalytics: FirebaseAnalytics by lazy { FirebaseAnalytics.getInstance(AnalyticsApp.instance.context!!) }

    override fun logSwipeEvent(category: String, value: String) {
        logCustomEvent("user_swipe", category, value)
    }

    override fun logClickEvent(category: String, value: String) {
        logCustomEvent("user_click", category, value)
    }

    override fun screenView(activity: Activity, screenName: String) {
        firebaseAnalytics.setCurrentScreen(activity, screenName, null)
    }



    override fun logSwipeEventRes(category: Int, value: Int) {
        logCustomEvent("user_swipe", getString(category), getString(value))
    }

    override fun logClickEventRes(category: Int, value: Int) {
        logCustomEvent("user_click", getString(category), getString(value))
    }

    override fun screenViewRes(activity: Activity, screenName: Int) {
        firebaseAnalytics.setCurrentScreen(activity, getString(screenName), null)
    }

    private fun logCustomEvent(eventName: String, category: String, value: String) {
        val params = Bundle().apply {
            putString(CATEGORY, category)
            putString(VALUE, value)
        }
        firebaseAnalytics.logEvent(eventName,params)
    }

    private fun getString(value: Int): String{
        return AnalyticsApp.instance.context!!.getString(value)
    }

}