package br.com.goin.feature.splash

import android.content.Context
import android.content.Intent
import android.net.Uri

object GooglePlayHelper{
    fun openGooglePlay(context: Context) {
        val appPackageName = context.packageName
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
        } catch (anfe: android.content.ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }
    }
}