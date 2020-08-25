package br.com.goin.feature.event

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import br.com.goin.component.model.event.api.response.ApiVoucher
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.dialog_event_voucher.view.*

object EventDialogHelper {

    fun showVoucherEvent(activity: AppCompatActivity, apiVoucher: ApiVoucher, clamConfirm: (ApiVoucher) -> Unit): AlertDialog {
        val alertBuilder = AlertDialog.Builder(activity).create()
        val dialogEventVoucher = activity.layoutInflater.inflate(R.layout.dialog_event_voucher, null)
        alertBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        Glide.with(activity)
                .load(apiVoucher.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(dialogEventVoucher.imageViewDialogVoucher)

        val title = "${apiVoucher.title} ${apiVoucher.subtitle}"
        val subtitle = apiVoucher.subtitle ?: ""
        val titleSpannable = SpannableString("${apiVoucher.title} ${apiVoucher.subtitle}")

        val redColor = ColorStateList(arrayOf(intArrayOf()), intArrayOf(ContextCompat.getColor(activity, R.color.gray_rating)))
        val highlightSpan = TextAppearanceSpan(null, Typeface.NORMAL, -1, redColor, null)
        titleSpannable.setSpan(highlightSpan, title.indexOf(subtitle), title.indexOf(subtitle) + subtitle.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        dialogEventVoucher.textViewTitle.text = titleSpannable
        dialogEventVoucher.textViewRegulation.text = apiVoucher.regulation
        dialogEventVoucher.textViewDate.text = apiVoucher.dateText
        dialogEventVoucher.textViewTour.text = apiVoucher.placeName
        dialogEventVoucher.textViewLabelVoucher.text = apiVoucher.detailTitle
        dialogEventVoucher.imageViewDialogClose.setOnClickListener { alertBuilder.dismiss() }
        dialogEventVoucher.confirmVoucher.setOnClickListener {
            clamConfirm(apiVoucher)
        }

        alertBuilder.setView(dialogEventVoucher)
        alertBuilder.show()
        return alertBuilder
    }
}