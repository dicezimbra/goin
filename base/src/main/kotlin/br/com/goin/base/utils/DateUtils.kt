package br.com.goin.base.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

class DateUtils : android.text.format.DateUtils() {

    companion object {
        fun formatEventDate(startDate: Calendar?, endDate: Calendar?): String {

            val starday = startDate?.get(Calendar.DAY_OF_MONTH)
            val starthour = startDate?.get(Calendar.HOUR_OF_DAY)
            val startminute = startDate?.get(Calendar.MINUTE)

            val monthFormat = SimpleDateFormat("MMMM")
            val startmonth = monthFormat.format(startDate?.time)


            if (endDate != null) {


                val endMonth = monthFormat.format(endDate.time)


                val endhour = endDate.get(Calendar.HOUR_OF_DAY)
                val endminute = endDate.get(Calendar.MINUTE)
                val endDay = endDate.get(Calendar.DAY_OF_MONTH)

                if (startmonth != endMonth) {
                    val startTime = String.format("%02dh%02d", starthour, startminute)
                    val endTime = String.format("%02dh%02d", endhour, endminute)
                    return starday.toString() + " de " + startmonth + " as " + startTime + " ate " + endDay + " de " + endMonth + " as " + endTime
                } else if (starday != endDay && startmonth != endMonth) {
                    val startTime = String.format("%02dh%02d", starthour, startminute)
                    val endTime = String.format("%02dh%02d", endhour, endminute)
                    return starday.toString() + " de " + startmonth + " as " + startTime + " ate " + endDay + " de " + endMonth + " as " + endTime
                } else if (endhour != starthour && starday != endDay) {
                    val startTime = String.format("%02dh%02d", starthour, startminute)
                    val endTime = String.format("%02dh%02d", endhour, endminute)
                    return starday.toString() + " a " + endDay + " de " + startmonth + " das " + startTime + " as " + endTime
                } else if (starday == endDay && endhour != starthour) {
                    val startTime = String.format("%02dh%02d", starthour, startminute)
                    val endTime = String.format("%02dh%02d", endhour, endminute)
                    return starday.toString() + " de " + startmonth + " das " + startTime + " as " + endTime
                } else if (starday == endDay && endhour == starthour) {
                    val startTime = String.format("%02dh%02d", starthour, startminute)
                    return starday.toString() + " de " + startmonth + " as " + startTime
                }

            }


            val startTime = String.format("%02d:%02d", starthour, startminute)

            return starday.toString() + ", " + starday + " de " + startmonth + " as " + startTime
        }

        fun getDay(startDate: Calendar?): String {
            return "" + startDate?.get(Calendar.DAY_OF_MONTH)
        }

        @SuppressLint("SimpleDateFormat")
        fun getMonth(startDate: Calendar?): String? {
            val monthFormat = SimpleDateFormat("MMMM")
            val month = monthFormat.format(startDate?.time)

            return month.substring(0, 3).toUpperCase()
        }

        @SuppressLint("SimpleDateFormat")
        fun getFullMonth(startDate: Calendar?): String? {
            val monthFormat = SimpleDateFormat("MMMM")
            val month = monthFormat.format(startDate?.time)

            return month.toUpperCase()
        }

        /**
         * Convert Calendar to "dd/MM/yyyy - hh:mm" date String format
         * Eg.: 01/01/2018 - 14:00
         */
        fun convertCalendarToDateAndTimeString(calendar: Calendar?): String {

            val date = SimpleDateFormat("dd/MM/yyyy - hh:mm",
                    Locale.getDefault())
            return if (calendar == null) {
                ""
            } else date.format(calendar.time)
        }




        fun convertCalendarToDateAndTimeString2(calendar: Calendar): String {
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            return day.toString() + "/" + (month + 1) + "/" + year + " - " + String.format("%02d:%02d", hour, minute)
        }

        /**
         * Convert Calendar to "dd/MM/yyyy" date String format
         * Eg.: 01/01/2018
         */
        fun convertCalendarToDateString(calendar: Calendar?): String {

            val date = SimpleDateFormat("dd/MM/yyyy",
                    Locale.getDefault())

            return if (calendar == null) {
                ""
            } else date.format(calendar.time)

        }

        fun convertCalendarToDateString(date: Long?): String {

            date?.let {
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
                calendar.timeInMillis = it


                return convertCalendarToDateString(calendar)
            }
        }

        /**
         * Convert Calendar to "HH:mm" date String format
         * Eg.: 20:00
         */
        fun convertCalendarToHourString(calendar: Calendar?): String {

            val date = SimpleDateFormat("HH:mm",
                    Locale.getDefault())

            return if (calendar == null) {
                ""
            } else date.format(calendar.time)

        }
    }





}
