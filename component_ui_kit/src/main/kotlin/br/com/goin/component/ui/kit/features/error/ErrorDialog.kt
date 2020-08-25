package br.com.goin.component.ui.kit.features.error

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import androidx.core.content.ContextCompat
import br.com.goin.component.ui.kit.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_error.view.*
import kotlinx.android.synthetic.main.view_error_dialog.*

class ErrorDialog(context: Context) : Dialog(context) {

    var onClickRetry: () -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.view_error_dialog)

        retry_button_error_dialog.setOnClickListener {
            dismiss()
            onClickRetry()
        }

        configureInternetError()
        configureUnknownError()
        configureMessageError("")
    }

    fun configureInternetError() {
        imageErrorDialog.background = ContextCompat.getDrawable(context, R.drawable.image_internet_error)
        messageErrorDialog.text = context.getString(R.string.error_not_internet)
    }

    fun configureMessageError(id: String) {
        imageErrorDialog.background = ContextCompat.getDrawable(context, R.drawable.image_internet_error)
        messageErrorDialog?.text = id
    }

    fun configureUnknownError() {
        imageErrorDialog.background = ContextCompat.getDrawable(context, R.drawable.image_cloud_error)
        messageErrorDialog?.text = context.getString(R.string.unknow_error)
    }
}