package br.goin.features.login.helper

import android.text.TextUtils
import android.util.Patterns

object ValidationHelper {

    fun isValidEmail(email: CharSequence): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}