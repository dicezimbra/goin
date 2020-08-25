package br.com.goin.base.helpers

import android.app.Activity
import android.content.Context

import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardHelper {

    fun hideKeyboard(view: View?, activity: Activity) {
        if (view != null) {
            val `in` = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            `in`.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}
