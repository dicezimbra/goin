package br.com.goin.feature.event

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import br.com.goin.base.R

object BrowserHelper{

    fun openBrowser(context: Context, url: String) {
        var finalUrl = url
        if (!finalUrl.startsWith("http://") && !finalUrl.startsWith("https://")) {
            finalUrl = "http://$url"
        }

        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, Uri.parse(finalUrl))
    }
}