package br.com.goin.base.helpers

import android.content.Context
import android.preference.PreferenceManager
import br.com.goin.base.BaseApplication
import com.google.gson.GsonBuilder

object PreferenceHelper {
    private const val PREFERENCE_KEY = "goin_preference"

    fun write(key: String, value: String) {
        val context = BaseApplication.instance.context!!
        val preference = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)

        val editor = preference.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun write(key: String, data: Any) {
        val context = BaseApplication.instance.context!!
        val preference = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)

        val gson = GsonBuilder().create()
        val listString = gson.toJson(data)
        preference.edit().putString(key, listString).apply()
    }

    fun read(key: String): Any? {
        val context = BaseApplication.instance.context!!
        val preference = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
        return preference.all[key]
    }

    fun clear() {
        val context = BaseApplication.instance.context!!
        val preference = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
        preference.edit().clear().apply()

        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply()
    }

    fun clear(key: String) {
        val context = BaseApplication.instance.context!!
        val preference = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
        preference.edit().remove(key).apply()
    }

    fun sameOfCache(key: String, data: Any): Boolean {
        val context = BaseApplication.instance.context!!
        val preference = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)

        val gson = GsonBuilder().create()
        val fromServer = gson.toJson(data)
        val fromCache = preference.getString(key, null)
        return fromServer == fromCache
    }
}