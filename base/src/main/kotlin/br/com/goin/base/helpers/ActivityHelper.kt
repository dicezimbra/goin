package br.com.goin.base.helpers

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object ActivityHelper {

    fun hasPermissions(context: Context?, permissions: Array<String>): Boolean {
        for (permission: String in permissions) {
            if (ActivityCompat.checkSelfPermission(context!!, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
}