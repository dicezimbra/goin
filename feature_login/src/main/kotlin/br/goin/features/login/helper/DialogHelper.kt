package br.goin.features.login.helper

import android.app.Activity
import android.app.AlertDialog

object DialogHelper {

    fun showOkDialog(activity: Activity, msg: String, callback: () -> Unit): AlertDialog? {

        return AlertDialog.Builder(activity)
                .setMessage(msg)
                .setPositiveButton("OK") { dialog, _ ->
                    callback()
                    dialog.dismiss()
                }
                .show()
    }

}