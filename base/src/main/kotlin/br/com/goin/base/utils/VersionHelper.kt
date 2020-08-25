package br.com.goin.base.utils

import android.os.Build
import br.com.goin.base.BaseApplication

object VersionHelper{

    fun versionCode(): Long?{
        val context = BaseApplication.instance.context
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            context?.packageManager?.getPackageInfo(context.packageName, 0)?.longVersionCode
        } else {
            context?.packageManager?.getPackageInfo(context.packageName, 0)?.versionCode?.toLong()
        }
    }

    fun versionName(): String?{
        val context = BaseApplication.instance.context
        return context?.packageManager?.getPackageInfo(context.packageName, 0)?.versionName
    }
}