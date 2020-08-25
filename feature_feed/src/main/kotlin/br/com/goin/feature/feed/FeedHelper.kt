package br.com.goin.feature.feed

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.core.content.ContextCompat
import br.com.goin.base.BaseApplication
import br.com.goin.base.utils.Utils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object FeedHelper {

    fun showDeleteDialog(context: Context, callback: () -> Unit) {
        AlertDialog.Builder(context).setMessage(R.string.confirm_delete_message)
                .setPositiveButton(R.string.okay) { _, _ ->
                    callback()
                }.setNeutralButton(R.string.cancel) { dialogInterface, _ -> dialogInterface.dismiss() }
                .create()
                .show()
    }

    fun showReportDialog(context: Context, callback: () -> Unit) {
        AlertDialog.Builder(context).setMessage(R.string.confirm_report_message)
                .setPositiveButton(R.string.okay) { _, _ ->
                    callback()
                }.setNeutralButton(R.string.cancel) { dialogInterface, _ -> dialogInterface.dismiss() }
                .create()
                .show()
    }

    fun showSuccessReportMessage(context: Context, title: String, msg: String, btnTitleListener: String) {
        AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(btnTitleListener) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
    }

    fun getPostWithTags(description: String?, onClickListener: ((userId: String) -> Unit)?): SpannableStringBuilder {

        val builder = SpannableStringBuilder()
        if (description == null) return builder
        var currentIndex = 0
        val tagIndicator = "@{" + Utils.TAG_HASH
        val bold = StyleSpan(Typeface.BOLD)


        while (currentIndex <= description.length - 1) {
            val tagStartIndex = description.indexOf(tagIndicator, currentIndex)
            val tagEndIndex = description.indexOf("}", tagStartIndex)
            if (tagStartIndex == -1 || tagEndIndex == -1) {
                return builder.append(description.substring(currentIndex))
            }
            val normalText = description.substring(currentIndex, tagStartIndex)
            val tagInformation = description.substring(tagStartIndex + tagIndicator.length + 1, tagEndIndex).split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val tag = tagInformation[0]
            val userId = tagInformation[1]
            if (tagInformation.size == 1) {
                return builder.append(description.substring(currentIndex))
            }
            val auxSpan = SpannableString(normalText + tag)
            currentIndex = tagEndIndex + 1
            auxSpan.setSpan(bold, normalText.length, auxSpan.length, 0)
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    onClickListener?.invoke(userId)
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                    ds.color = ContextCompat.getColor(BaseApplication.instance.context!!, R.color.grapefruit)
                }
            }
            auxSpan.setSpan(clickableSpan, normalText.length, auxSpan.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            builder.append(auxSpan)
        }
        return builder
    }

    @SuppressLint("SimpleDateFormat")
    fun stringToTimestampAmerican(dateString: String): Long? {
        val formatter = SimpleDateFormat("MM/dd/yyyy")
        try {
            val date = formatter.parse(dateString)
            return date.time
        } catch (e: ParseException) {
            return null
        }

    }

    fun stringToTimestampBrazilian(timestamp: Long?): String {
        val date = Date(timestamp!!)
        @SuppressLint("SimpleDateFormat")
        val timeSDF = SimpleDateFormat("HH:mm") // the format of your time
        @SuppressLint("SimpleDateFormat")
        val dateSDF = SimpleDateFormat("dd/MM/yyyy") // the format of your date

        timeSDF.timeZone = TimeZone.getTimeZone("GMT-2")

        return dateSDF.format(date) + " às " + timeSDF.format(date)
    }

    @Throws(ParseException::class)
    fun stringToTimestamp(dateString: String): Long {
        val formatter = SimpleDateFormat(Utils.dateFormat)
        val date = formatter.parse(dateString)
        return date.time
    }

    fun getTimeDifText(timestamp: Long?, context: Context): String {
        val now = Calendar.getInstance().time
        val seconds = now.time / 1000L - timestamp!! / 1000L
        val minutes = seconds / 60L

        if (seconds < 0) {
            return context.resources.getQuantityString(R.plurals.seconds_ago, seconds.toInt(), 0)
        }

        if (seconds < 60) {
            return context.resources.getQuantityString(R.plurals.seconds_ago, seconds.toInt(), seconds)
        }
        if (minutes in 1..59) {
            return context.resources.getQuantityString(R.plurals.minutes_ago, minutes.toInt(), minutes)
        }
        val hours = minutes / 60L
        if (hours >= 1 && hours < 24) {
            return context.resources.getQuantityString(R.plurals.hours_ago, hours.toInt(), hours)
        }
        val days = hours / 24L
        return if (days >= 1 && days < 365) {
            context.resources.getQuantityString(R.plurals.days_ago, days.toInt(), days)
        } else context.getString(R.string.long_time_ago)
    }


}
