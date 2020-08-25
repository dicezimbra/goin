package br.com.goin.feature.feed.post.checkIn

import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import br.com.goin.base.utils.GpsUtils
import br.com.goin.feature.feed.R

object CheckInHelper {

    fun permissionsMessage(activity: Activity, message: String, callback: () -> Unit) {

        val builder = AlertDialog.Builder(activity)

        builder.setTitle(activity.getString(R.string.permissions_dialog_title))
        builder.setMessage(message)
        builder.setPositiveButton(activity.getString(R.string.yes_dialog)) { dialog, _ ->
            callback()
            dialog.dismiss()
        }

        builder.setNegativeButton(activity.getString(R.string.no_dialog)) { dialog, _ ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    fun showSimpleMessage(activity: Activity, msg: String) {
        showSimpleMessage(activity, msg)
    }




}