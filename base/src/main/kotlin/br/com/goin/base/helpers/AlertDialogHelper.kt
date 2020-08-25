package br.com.goin.base.helpers

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment

import br.com.goin.base.R

object AlertDialogHelper {
    private val MESSAGE_ARG = "MESSAGE_ARG"

    @JvmOverloads
    fun show(activity: AppCompatActivity?, messageId: Any?, callback: DialogCallback? = null) {

        try {
            val message: String = if (messageId != null && messageId.javaClass == Int::class.java) {
                activity!!.getString(messageId as Int)
            } else {
                messageId as String
            }

            val dialogFragment = AlertDialogFragmentWithDefault()
            dialogFragment.setCallback(callback)
            val args = Bundle()
            args.putString(MESSAGE_ARG, message)
            dialogFragment.arguments = args

            try {
                if (activity != null)
                    dialogFragment.show(activity.supportFragmentManager, activity.resources.getString(R.string.alert_warning))
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
            }

        } catch (e: Exception) {

        }

    }

    interface DialogCallback {
        fun onClickOk()
        fun onClickCancel()
    }

    class AlertDialogFragmentWithDefault : DialogFragment() {
        private var callback: DialogCallback? = null

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder = AlertDialog.Builder(activity)
            builder.setMessage(arguments?.getString(MESSAGE_ARG))
            builder.setPositiveButton(activity?.getString(R.string.alert_ok)) { dialog, _ ->
                activity?.let {
                    KeyboardHelper.hideKeyboard(view, it)
                }

                dialog.dismiss()
                if (callback != null)
                    callback!!.onClickOk()
            }
            return builder.create()
        }

        override fun onCancel(dialog: DialogInterface) {
            activity?.let {
                KeyboardHelper.hideKeyboard(view, it)
            }

            if (callback != null)
                callback!!.onClickCancel()
        }

        fun setCallback(callback: DialogCallback?) {
            this.callback = callback
        }
    }
}
