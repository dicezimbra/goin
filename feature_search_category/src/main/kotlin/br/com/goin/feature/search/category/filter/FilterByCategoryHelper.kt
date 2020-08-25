package br.com.goin.feature.search.category.filter

import android.app.DatePickerDialog
import android.content.Context
import br.com.goin.base.utils.Constants
import br.com.goin.component.ui.kit.datepicker.DatePickerHelper
import br.com.goin.component.ui.kit.dialog.DialogUtils
import br.com.goin.feature.search.category.R
import kotlinx.android.synthetic.main.activity_filter_by_category.*
import java.util.*

object FilterByCategoryHelper{


    fun openCalendar(context: Context,
                     minDate: Long? = Calendar.getInstance().timeInMillis,
                     callback: (Long, String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year1, month1, dayOfMonth ->
            val calendarNow = Calendar.getInstance()
            calendarNow.timeZone = TimeZone.getTimeZone("GMT")
            calendarNow.set(Calendar.AM_PM, Calendar.AM)
            calendarNow.set(Calendar.YEAR, year1)
            calendarNow.set(Calendar.MONTH, month1)
            calendarNow.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            calendarNow.set(Calendar.HOUR, 6)
            calendarNow.set(Calendar.MINUTE, 0)
            calendarNow.set(Calendar.SECOND, 0)
            calendarNow.set(Calendar.MILLISECOND, 0)

            val calendarStartDateLong = calendarNow.timeInMillis
            val calendarDate = DatePickerHelper.getDate(calendarStartDateLong)
            callback(calendarStartDateLong, calendarDate)
        }

        val datePickerDialog: DatePickerDialog
        datePickerDialog = DatePickerDialog(context, dateSetListener, year, month, day)
        datePickerDialog.datePicker.minDate = minDate ?:  Calendar.getInstance().timeInMillis

        datePickerDialog.show()
    }
}