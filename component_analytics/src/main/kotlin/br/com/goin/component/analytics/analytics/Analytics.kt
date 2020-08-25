package br.com.goin.component.analytics.analytics

import android.app.Activity

interface Analytics {
    companion object {
        val instance: Analytics by lazy { AnalyticsImpl() }
    }

    fun screenView(activity: Activity, screenName: String)
    fun logSwipeEvent(category: String, value: String)
    fun logClickEvent(category: String, value: String = "")

    fun logSwipeEventRes(category: Int, value: Int)
    fun logClickEventRes(category: Int, value: Int)
    fun screenViewRes(activity: Activity, screenName: Int)
}