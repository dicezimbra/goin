package br.goin.features.login.signUp

import android.app.Activity
import android.text.TextUtils
import android.util.Patterns
import java.util.regex.Pattern

object SignUpHelper {

    private const val PREF_NAME = "BasePref"

    fun setEmailCredentials(activity: Activity, userEmail: String) {
        val sharedPref = activity.getSharedPreferences(PREF_NAME, 0)
        val editor = sharedPref.edit()
        editor.putString("email", userEmail)
        editor.apply()
    }

    fun getEmailCredentials(activity: Activity): String? {
        val sharedPref = activity.getSharedPreferences(PREF_NAME, 0)
        return sharedPref.getString("email", "")
    }

    fun isValidEmail(email: CharSequence): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    fun setPassCredentials(activity: Activity, pass: String) {
        val sharedPref = activity.getSharedPreferences(PREF_NAME, 0)
        val editor = sharedPref.edit()
        editor.putString("pass", pass)
        editor.apply()
    }

    fun getPassCredentials(activity: Activity): String? {
        val sharedPref = activity.getSharedPreferences(PREF_NAME, 0)
        return sharedPref.getString("pass", "")
    }

}
