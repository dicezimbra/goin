package br.com.goin.feature.search.category.model

import br.com.goin.feature.search.category.model.api.SearchFilterRequest
import java.io.Serializable
import java.util.*

data class SearchFilter(
        var categoryId: String? = null,
        var subCategoriesId: String? = null,
        var state: String? = null,
        var myLocation: Array<Double>? = null,
        var lowestPrice: String? = null,
        var tags: MutableList<String>? = arrayListOf(),
        var priceRating: MutableList<Int>? = null,
        var highestPrice: String? = null): Serializable{

    fun hasFilterEnabled(): Boolean {
        return (!priceRating.isNullOrEmpty() && (priceRating?.get(0) != 1 || priceRating?.get(1) != 5 ))
                || !subCategoriesId.isNullOrEmpty()
                || !state.isNullOrEmpty()
                || (!lowestPrice.isNullOrEmpty() && lowestPrice != "0")
                || !tags.isNullOrEmpty()
                || (!highestPrice.isNullOrEmpty() && highestPrice != "1500")
    }
}

data class SearchFilterTimePeriod( var fromDate: Long? = null,
                                   var isCustomDate: Boolean = false,
                                   var toDate: Long? = null): Serializable{

    fun clearDates(): SearchFilterTimePeriod {
        toDate = null
        fromDate = null
        return this
    }

    fun onlyTomorrow(): SearchFilterTimePeriod {
        val calendar = Calendar.getInstance()
        clearCalendar(calendar)
        calendar.add(Calendar.DATE, 1)

        toDate = calendar.time.time
        fromDate = calendar.time.time
        return this
    }

    fun onlyToday(): SearchFilterTimePeriod {
        val calendar = Calendar.getInstance()
        clearCalendar(calendar)
        toDate = calendar.time.time
        fromDate = calendar.time.time
        return this
    }

    fun onlyThisWeek(): SearchFilterTimePeriod {
        val calendarStart = Calendar.getInstance()
        val calendarEnd = Calendar.getInstance()
        clearCalendar(calendarStart)
        clearCalendar(calendarEnd)

        calendarStart.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendarEnd.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)

        fromDate = calendarStart.time.time
        toDate = calendarEnd.time.time
        return this
    }

    fun onlyThisWeekend(): SearchFilterTimePeriod {
        val calendarStart = Calendar.getInstance()
        val calendarEnd = Calendar.getInstance()
        clearCalendar(calendarStart)
        clearCalendar(calendarEnd)
        calendarStart.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
        calendarEnd.add(Calendar.WEEK_OF_MONTH, 1)
        calendarEnd.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        fromDate = calendarStart.time.time
        toDate = calendarEnd.time.time
        return this
    }

    fun onlyThisMonth(): SearchFilterTimePeriod {
        val calendarStart = Calendar.getInstance()
        val calendarEnd = Calendar.getInstance()
        clearCalendar(calendarStart)
        clearCalendar(calendarEnd)

        calendarStart.set(Calendar.DAY_OF_MONTH, 1)
        calendarEnd.set(Calendar.DAY_OF_MONTH, calendarEnd.getActualMaximum(Calendar.DAY_OF_MONTH))
        fromDate = calendarStart.time.time
        toDate = calendarEnd.time.time
        return this
    }

    fun onlyNextMonth(): SearchFilterTimePeriod {
        val calendarStart = Calendar.getInstance()
        val calendarEnd = Calendar.getInstance()
        clearCalendar(calendarStart)
        clearCalendar(calendarEnd)

        calendarStart.add(Calendar.MONTH, 1)
        calendarEnd.add(Calendar.MONTH, 1)

        calendarStart.set(Calendar.DAY_OF_MONTH, 1)
        calendarEnd.set(Calendar.DAY_OF_MONTH, calendarEnd.getActualMaximum(Calendar.DAY_OF_MONTH))

        fromDate = calendarStart.time.time
        toDate = calendarEnd.time.time
        return this
    }

    private fun clearCalendar(calendar: Calendar) {
        calendar.timeZone = TimeZone.getTimeZone("GMT")
        calendar.set(Calendar.HOUR, 1)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
    }
}

