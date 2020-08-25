package br.com.goin.component.ui.kit.dialog

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import br.com.goin.base.helpers.KeyboardHelper
import br.com.goin.component.ui.kit.R
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.item_success_dialog.view.*

object DialogUtils {

    private val MESSAGE_ARG = "MESSAGE_ARG"


    fun showAlertWithCallBack(context: Context, title: String, message: String, positiveButton: String,
                              positiveListener: (Any, Any) -> Unit) {

        val builder = androidx.appcompat.app.AlertDialog.Builder(context)

        builder.setMessage(message)
                .setTitle(title)

        builder.setPositiveButton(positiveButton, positiveListener)
        val dialog = builder.create()
        dialog.show()
    }


    fun showDecisionDialog(activity: Activity, msg: String,
                           acceptMessage: String, rejectMessage: String,
                           listener: DecisionListener): AlertDialog {

        return AlertDialog.Builder(activity)
                .setMessage(msg)
                .setPositiveButton(acceptMessage) { _, _ -> listener.onAccept() }
                .setNegativeButton(rejectMessage) { _, _ -> listener.onReject() }
                .show()
    }

    fun showDialogOptions(activity: Activity, msg: String,
                          acceptMessage: String, rejectMessage: String,
                          callback: () -> Unit) {

        val builder = androidx.appcompat.app.AlertDialog.Builder(activity)

        builder.setMessage(msg)
        builder.setPositiveButton(acceptMessage) { _, _ -> callback() }
        builder.setNegativeButton(rejectMessage) { dialog, _ -> dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }

    interface DecisionListener {
        fun onAccept()
        fun onReject()

    }

    fun handleError(activity: Activity?) {
        if (activity != null) {
            DialogUtils.show(activity, activity.getString(R.string.unkown_error), object : DialogUtils.DialogCallback {

                override fun onClickOk() {}

                override fun onClickCancel() {

                }
            })
        }
    }

    @JvmOverloads
    fun show(activity: Activity?, messageId: Int?, callback: DialogCallback? = null) {

        try {
            val message: String = activity!!.getString(messageId as Int)

            val dialogFragment = AlertDialogFragmentWithDefault()
            dialogFragment.setCallback(callback)//Passa o callback  pro escopo do AlertDialogFragmentWithDefault por setter
            val args = Bundle()
            args.putString(MESSAGE_ARG, message)
            dialogFragment.arguments = args

            try {
                if (activity != null)
                    dialogFragment.show(activity.fragmentManager, activity.resources.getString(R.string.alert_warning))
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
            }

        } catch (e: Exception) {

        }
    }


    @JvmOverloads
    fun showSuccess(activity: Activity?, description: Int?, drawable: Int, title: Int,  callback: (() -> Unit)?) {

        try {

            val dialogFragment = AlertDialogFragment()
            dialogFragment.setCallBack(callback)
            dialogFragment.setDescription(activity?.getString(description!!))
            dialogFragment.setDrawable(drawable)
            dialogFragment.setTitle(title)

           // val args = Bundle()
            //args.putString(MESSAGE_ARG, message)
            //dialogFragment.arguments = args

            try {
                if (activity != null)
                    dialogFragment.show(activity.fragmentManager, activity.resources.getString(R.string.alert_warning))
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
            }

        } catch (e: Exception) {

        }
    }

    @JvmOverloads
    fun showError(activity: Activity?, description: String?, drawable: Int, title: Int, callback: (() -> Unit)?) : AlertDialogFragment{
        val dialogFragment = AlertDialogFragment()
        try {


            dialogFragment.setCallBack(callback)
            dialogFragment.setDescription(description)
            dialogFragment.setDrawable(drawable)
            dialogFragment.setTitle(title)
            dialogFragment.isCancelable = false
            try {
                if (activity != null)
                    dialogFragment.show(activity.fragmentManager, activity.resources.getString(R.string.alert_warning))
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
            }

        } catch (e: Exception) {

        }

        return dialogFragment
    }



    @JvmOverloads
    fun showInput(activity: Activity?, description: String?, drawable: Int, title: Int, callback: ((String) -> Unit)?): AlertInputDialogFragment {
        val dialogFragment = AlertInputDialogFragment()
        try {


            dialogFragment.setCallback(callback)
            dialogFragment.setDescription(description)
            dialogFragment.setDrawable(drawable)
            dialogFragment.setTitle(title)

            // val args = Bundle()
            //args.putString(MESSAGE_ARG, message)
            //dialogFragment.arguments = args

            try {
                if (activity != null)
                    dialogFragment.show(activity.fragmentManager, activity.resources.getString(R.string.alert_warning))
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
            }


        } catch (e: Exception) {

        }
        return dialogFragment
    }

    @JvmOverloads
    fun show(activity: Activity?, messageId: String?, callback: DialogCallback? = null) {

        try {
            val message: String = messageId ?: ""

            val dialogFragment = AlertDialogFragmentWithDefault()
            dialogFragment.setCallback(callback)//Passa o callback  pro escopo do AlertDialogFragmentWithDefault por setter
            val args = Bundle()
            args.putString(MESSAGE_ARG, message)
            dialogFragment.arguments = args

            try {
                if (activity != null)
                    dialogFragment.show(activity.fragmentManager, activity.resources.getString(R.string.alert_warning))
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
            }

        } catch (e: Exception) {

        }
    }

    fun createProgressDialog(mContext: Context): ProgressDialog {
        val dialog = ProgressDialog(mContext)
        try {
            dialog.show()
        } catch (e: WindowManager.BadTokenException) {

        }

        dialog.setCancelable(false)
        if (dialog.window!!.windowManager != null)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.item_progress_dialog)
        dialog.hide()
        return dialog
    }

    interface DialogCallback {
        fun onClickOk()
        fun onClickCancel()
    }

    class AlertDialogFragmentWithDefault : DialogFragment() {
        private var callback: DialogCallback? = null

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val builder = AlertDialog.Builder(activity)
            builder.setMessage(arguments.getString(MESSAGE_ARG))
            builder.setPositiveButton(activity.getString(R.string.alert_ok)) { dialog, _ ->
                KeyboardHelper.hideKeyboard(view, activity)
                dialog.dismiss()
                if (callback != null)
                    callback!!.onClickOk()
            }
            return builder.create()
        }

        override fun onCancel(dialog: DialogInterface) {
            KeyboardHelper.hideKeyboard(view, activity)
            if (callback != null)
                callback!!.onClickCancel()
        }

        fun setCallback(callback: DialogCallback?) {
            this.callback = callback
        }
    }

    class AlertDialogFragment : DialogFragment() {

        private var callBackUnit:  (() -> Unit)? = null
        private var drawable: Int? = null
        private var title: Int? = null
        private var description: String? = null
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

            val factory = LayoutInflater.from(activity)
            val content = factory.inflate(R.layout.item_success_dialog, null)

            val builder = AlertDialog.Builder(activity)

            val dialog_icon = content.dialog_icon
            val dialog_title = content.dialog_title
            val dialog_description = content.dialog_description
            val dialog_action = content.dialog_action

            dialog_action.setOnClickListener{
                callBackUnit?.invoke()
            }

            drawable?.let {   dialog_icon.setImageResource(it) }
            title?.let {   dialog_title.text = activity.getString(it) }
            description?.let {   dialog_description.text =it}

            builder.setView(content)
            return builder.create()
        }

        fun setCallBack(callback:   (() -> Unit)? ) {
            this.callBackUnit = callback
        }

        fun setDrawable(drawable: Int?) {
            this.drawable = drawable
        }

        fun setTitle(title: Int?) {
            this.title = title
        }

        fun setDescription(description: String?) {
            this.description = description
        }
    }


    class AlertInputDialogFragment : DialogFragment() {
        private var callback: ((String) -> Unit)? = null
        private var drawable: Int? = null
        private var title: Int? = null
        private var description: String? = null
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

            val factory = LayoutInflater.from(activity)
            val content = factory.inflate(R.layout.item_input_dialog, null)

            val builder = AlertDialog.Builder(activity)

            val dialog_input_icon = content.findViewById<ImageView>(R.id.dialog_input_icon)
            val dialog_input_title = content.findViewById<TextView>(R.id.dialog_input_title)
            val dialog_input_description = content.findViewById<TextView>(R.id.dialog_input_description)
            val dialog_input_action = content.findViewById<Button>(R.id.dialog_input_action)
            val dialog_input_input = content.findViewById<EditText>(R.id.dialog_input_input)

            dialog_input_action.setOnClickListener{
                callback?.invoke(dialog_input_input.text.toString())
            }

            drawable?.let {   dialog_input_icon.setImageResource(it) }
            title?.let {   dialog_input_title.text = activity.getString(it) }
            description?.let {   dialog_input_description.text = it }

            builder.setView(content)
            return builder.create()
        }

        fun setCallback(callback: ((String) -> Unit)?) {
            this.callback = callback
        }

        fun setDrawable(drawable: Int?) {
            this.drawable = drawable
        }

        fun setTitle(title: Int?) {
            this.title = title
        }

        fun setDescription(description: String?) {
            this.description = description
        }

    }
}
