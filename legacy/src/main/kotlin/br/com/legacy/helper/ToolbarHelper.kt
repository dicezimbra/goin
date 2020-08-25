package br.com.legacy.helper

import android.content.Context
import android.view.View

object ToolbarHelper {

    fun setStatusbarPadding(view: View, context: Context) {
        view.setPadding(0, getStatusbarHeight(context), 0, 0)
    }

    private fun getStatusbarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}