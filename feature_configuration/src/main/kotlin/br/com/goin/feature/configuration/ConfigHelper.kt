package br.com.goin.feature.configuration

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.feature_configuration.R

object ConfigHelper {

    fun showDialogLogout(context: Context, callback: () -> Unit) {
        val builder = AlertDialog.Builder(context)

        builder.setMessage(context.getString(R.string.really_want_to_leave))
                .setTitle(context.getString(R.string.logout))

        builder.setPositiveButton(context.getString(R.string.logout)) { _, _ -> callback() }
        builder.setNegativeButton(context.getString(R.string.no)) { dialog, _ -> dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }
}