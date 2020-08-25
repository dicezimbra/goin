package br.com.goin.base.helpers

import android.app.Activity
import android.app.AlertDialog
import br.com.goin.base.R

object DialogPermissionHelper {

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
}