package br.com.goin.features.password.newpassword

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

object NewPasswordHelper {

    fun hideKeyboardOnTouch(view: View, activity: Activity) {
        if (!view.isClickable) {
            view.setOnTouchListener { _, _ ->
                hideKeyboard(view, activity)
                false
            }
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                hideKeyboardOnTouch(innerView, activity)
            }
        }
    }

    fun hideKeyboard(view: View?, activity: Activity) {
        if (view != null) {
            val `in` = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            `in`.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

}