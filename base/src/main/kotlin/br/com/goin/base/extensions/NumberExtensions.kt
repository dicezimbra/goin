package br.com.goin.base.extensions

import android.content.res.Resources
import android.text.TextUtils
import android.util.Patterns
import java.text.DecimalFormat

fun Float.dpToPx(): Float {
    return (this * Resources.getSystem().displayMetrics.density)
}

fun Int.dpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}

fun Int.pxToDp(): Int {
    return (this / Resources.getSystem().displayMetrics.density).toInt()
}

fun Int.formatNumberBr(): String {

    val number = this.toFloat()

    val decimalFormat = DecimalFormat()
    decimalFormat.applyPattern("R$ #,##0.00")
    return decimalFormat.format(number)
}

fun Float.formatNumberBr(): String {

    val decimalFormat = DecimalFormat()
    decimalFormat.applyPattern("R$ #,##0.00")
    return decimalFormat.format(this)
}

fun Int.formatFloatTwoDigits(): String {

    val priceRound = Math.round(this.toFloat()).toFloat()
    val finalPrice = String.format("%.2f", priceRound)
    return finalPrice
}

fun Int.addTwoDigits(): String {
    return "${this}00"
}