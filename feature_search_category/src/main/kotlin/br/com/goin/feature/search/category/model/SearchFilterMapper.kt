package br.com.goin.feature.search.category.model

import br.com.goin.component.model.event.api.response.ApiEvent
import br.com.goin.component_model_club.model.Club
import br.com.goin.component_model_club.model.SubcategoriesInfo
import br.com.goin.feature.search.category.model.api.SearchFilterRequest
import br.com.goin.feature.search.category.model.api.SearchFilterResult
import java.util.*

class SearchFilterMapper {

    fun map(filter: SearchFilter, filterTimePeriod: SearchFilterTimePeriod): SearchFilterRequest {
        val request = SearchFilterRequest()
        request.categoryId = filter.categoryId
        request.subCategoriesId= filter.subCategoriesId
        request.fromDate=filterTimePeriod.fromDate
        request.toDate=filterTimePeriod.toDate
        request.state=filter.state
        request.myLocation=filter.myLocation
        request.lowestPrice=filter.lowestPrice
        if(filter.highestPrice != null) {
            request.highestPrice = "${filter.highestPrice}00"
        }
        request.priceRating=filter.priceRating
        request.tags=filter.tags

        return request
    }

    fun map(filter: SearchFilterResult?): SearchFilterPage {
        val itens = mapEvents(filter?.events)
        itens.addAll(mapClub(filter?.clubs))

        val page = SearchFilterPage()
        page.currentPage = filter?.currentPage ?: 1
        page.itens = itens
        page.totalItems = filter?.totalItems ?: 0
        page.totalPages = filter?.totalPages ?: 0

        return page
    }

    private fun mapEvents(events: List<ApiEvent>?): MutableList<SearchFilterPageItem> {
        val items = arrayListOf<SearchFilterPageItem>()
        events?.forEach {
            var calendar: Calendar? = null
            if (it.startDate != null) {
                calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
                calendar.timeZone = TimeZone.getTimeZone("GMT")
                calendar.timeInMillis = it.startDate!!
            }

            items.add(SearchFilterPageItem(name = it.name, type = Type.EVENT,
                    lat = it.latitude?.toDouble(), lng = it.longitude?.toDouble(),
                    id = it.id, image = it.image, subcategory = mapCategoryEvent(it.subcategories), city = it.city,
                    address = it.placeAddress, price = null, state = it.state, startDate = calendar, rating = it.rating, distance = 0F))
        }

        return items
    }

    private fun mapClub(clubs: List<Club>?): MutableList<SearchFilterPageItem> {
        val items = arrayListOf<SearchFilterPageItem>()
        clubs?.forEach {
            items.add(SearchFilterPageItem(name = it.name ?: "", type = Type.CLUB,
                    lat = it.latitude?.toDouble(), lng = it.longitude?.toDouble(),
                    id = it.id ?: "", city = it.city,
                    logo = it.logoImage, image = it.coverImage ?: it.logoImage,
                    subcategory = mapCategories(it.subcategories),
                    address = it.address, price = it.priceRating, state = it.state, startDate = null, rating = it.rating, distance = 0f))
        }

        return items
    }

    private fun mapCategoryEvent(categories: List<br.com.goin.component.model.event.api.response.SubcategoriesInfo>): String {

        var fullCategories = ""
        var categories = categories.distinctBy { it.name }
        if(categories != null && categories.size > 0)

        categories?.let {
            it.forEach { element ->
                fullCategories+= " / ${element.name}"
            }
        }

        return fullCategories
    }

    private fun mapCategories(categories: List<SubcategoriesInfo>): String {
        var fullCategories = ""
        categories?.let {
            it.forEach { element ->
                fullCategories+= " / ${element.name}"
            }
        }

        return fullCategories
    }
}