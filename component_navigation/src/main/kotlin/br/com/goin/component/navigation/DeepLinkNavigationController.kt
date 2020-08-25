package br.com.goin.component.navigation

import android.app.Activity
import android.content.Context

interface DeepLinkNavigationController {
    fun goToBannerAction(context: Activity, actionValue: String, bannerType: String)
}
