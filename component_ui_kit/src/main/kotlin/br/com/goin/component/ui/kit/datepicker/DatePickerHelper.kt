package br.com.goin.component.ui.kit.datepicker

import java.text.SimpleDateFormat
import java.util.*

class DatePickerHelper {

    companion object {
        fun getDate(date: Long): String {
            val instance = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
            instance.time = Date(date)
            val monthFormat = SimpleDateFormat("MMMM")
            val month = monthFormat.format(instance?.time)

            val realMonth = month.substring(0, 3)
            val day = instance.get(Calendar.DAY_OF_MONTH).toString()
            val year = instance.get(Calendar.YEAR).toString()
            val dateString = "$day de $realMonth $year"

            return dateString
        }
    }
}