package br.com.goin.feature.search.category.model.api

import java.io.Serializable
import java.util.*

data class SearchFilterRequest(var categoryId: String? = null,
                               var subCategoriesId: String? = null,
                               var fromDate: Long? = null,
                               var toDate: Long? = null,
                               var state: String? = null,
                               var myLocation: Array<Double>? = null,
                               var lowestPrice: String? = null,
                               var highestPrice: String? = null,
                               var tags: MutableList<String>? = null,
                               var priceRating: MutableList<Int>? = null) : Serializable {

    fun clone(): SearchFilterRequest {
        val filter = SearchFilterRequest()
        filter.categoryId = this.categoryId
        filter.subCategoriesId = this.subCategoriesId
        filter.fromDate = this.fromDate
        filter.toDate = this.toDate
        filter.state = this.state
        filter.myLocation = this.myLocation
        filter.lowestPrice = this.lowestPrice
        filter.highestPrice = this.highestPrice
        return filter
    }

    fun clearDates(): SearchFilterRequest {
        this.toDate = null
        this.fromDate = null
        return this
    }

    fun onlyTomorrow(): SearchFilterRequest {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_WEEK_IN_MONTH, 1)
        this.toDate = calendar.time.time
        this.fromDate = calendar.time.time
        return this
    }

    fun onlyToday(): SearchFilterRequest {
        val date = Date().time
        this.toDate = date
        this.fromDate = date
        return this
    }

    fun onlyThisWeek(): SearchFilterRequest {
        val calendarStart = Calendar.getInstance()
        val calendarEnd = Calendar.getInstance()
        calendarStart.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        calendarEnd.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
        this.fromDate = calendarStart.time.time
        this.toDate = calendarEnd.time.time
        return this
    }

    fun onlyThisMonth(): SearchFilterRequest {
        val calendarStart = Calendar.getInstance()
        val calendarEnd = Calendar.getInstance()
        calendarStart.set(Calendar.DAY_OF_MONTH, 0)
        calendarEnd.set(Calendar.DAY_OF_MONTH, calendarEnd.getActualMaximum(Calendar.DAY_OF_MONTH))
        this.fromDate = calendarStart.time.time
        this.toDate = calendarEnd.time.time
        return this
    }

    fun onlyNextMonth(): SearchFilterRequest {
        val calendarStart = Calendar.getInstance()
        val calendarEnd = Calendar.getInstance()

        calendarStart.add(Calendar.MONTH, 1)
        calendarEnd.add(Calendar.MONTH, 1)

        calendarStart.set(Calendar.DAY_OF_MONTH, 0)
        calendarEnd.set(Calendar.DAY_OF_MONTH, calendarEnd.getActualMaximum(Calendar.DAY_OF_MONTH))
        this.fromDate = calendarStart.time.time
        this.toDate = calendarEnd.time.time
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SearchFilterRequest

        if (categoryId != other.categoryId) return false
        if (subCategoriesId != other.subCategoriesId) return false
        if (fromDate != other.fromDate) return false
        if (toDate != other.toDate) return false
        if (state != other.state) return false
        if (myLocation != null) {
            if (other.myLocation == null) return false
            if (myLocation?.hashCode() == other.myLocation?.hashCode()) return false
        } else if (other.myLocation != null) return false
        if (lowestPrice != other.lowestPrice) return false
        if (highestPrice != other.highestPrice) return false

        return true
    }

    override fun hashCode(): Int {
        var result = categoryId?.hashCode() ?: 0
        result = 31 * result + (subCategoriesId?.hashCode() ?: 0)
        result = 31 * result + (fromDate?.hashCode() ?: 0)
        result = 31 * result + (toDate?.hashCode() ?: 0)
        result = 31 * result + (state?.hashCode() ?: 0)
        result = 31 * result + (myLocation?.contentHashCode() ?: 0)
        result = 31 * result + (lowestPrice?.hashCode() ?: 0)
        result = 31 * result + (highestPrice?.hashCode() ?: 0)
        return result
    }
}
