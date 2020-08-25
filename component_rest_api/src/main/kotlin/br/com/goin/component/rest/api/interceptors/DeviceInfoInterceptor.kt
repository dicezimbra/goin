package br.com.goin.component.rest.api.interceptors

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.text.TextUtils

import java.io.IOException
import java.util.UUID

import br.com.goin.base.BaseApplication
import br.com.goin.base.BuildConfig
import br.com.goin.base.utils.VersionHelper
import okhttp3.Interceptor
import okhttp3.Response

class DeviceInfoInterceptor : Interceptor {

    @SuppressLint("HardwareIds")
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {

        val request = chain.request()
        val builder = request.newBuilder()

        val context = BaseApplication.instance.context!!
        val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        val applicationId = id(context)
        val osVersion = Build.VERSION.RELEASE
        val appVersion = "" + VersionHelper.versionCode()
        val deviceModel = deviceName

        builder.addHeader("device-id", deviceId)
        builder.addHeader("application-id", applicationId)
        builder.addHeader("os", "android")
        builder.addHeader("os-version", osVersion)
        builder.addHeader("app-version", appVersion)

        deviceModel?.let {
            builder.addHeader("device-model", deviceModel)
        }

        return chain.proceed(builder.build())
    }

    fun id(context: Context): String {
        var uniqueID: String? = null
        if (uniqueID == null) {
            val sharedPrefs = context.getSharedPreferences(
                    "uniqueId", Context.MODE_PRIVATE)
            uniqueID = sharedPrefs.getString("uniqueId", null)
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString()
                val editor = sharedPrefs.edit()
                editor.putString("uniqueId", uniqueID)
                editor.commit()
            }
        }
        return uniqueID
    }

    companion object {

        val deviceName: String?
            get() {
                val manufacturer = Build.MANUFACTURER
                val model = Build.MODEL
                return if (model.startsWith(manufacturer)) {
                    capitalize(model)
                } else capitalize(manufacturer) + " " + model
            }

        private fun capitalize(str: String): String? {
            if (TextUtils.isEmpty(str)) {
                return str
            }
            val arr = str.toCharArray()
            var capitalizeNext = true

            val phrase = StringBuilder()
            for (c in arr) {
                if (capitalizeNext && Character.isLetter(c)) {
                    phrase.append(Character.toUpperCase(c))
                    capitalizeNext = false
                    continue
                } else if (Character.isWhitespace(c)) {
                    capitalizeNext = true
                }
                phrase.append(c)
            }

            return phrase.toString()
        }
    }
}
