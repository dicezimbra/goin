package br.com.legacy.helper

import android.app.Activity
import android.content.pm.PackageManager
import br.com.legacy.utils.GpsUtils

object PermissionsHelper {

    fun onRequestLocationResult(activity: Activity, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
            GpsUtils.getCurrentLocation(activity, { print(it) }, { print(it) })
        }
    }

}