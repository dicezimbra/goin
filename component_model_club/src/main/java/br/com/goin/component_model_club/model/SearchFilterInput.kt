package br.com.goin.component_model_club.model

import java.io.Serializable

class SearchFilterInput : Serializable {

    var categoryId: String? = null
    var subCategoriesId: String? = null
    var fromDate: Long? = null
    var toDate: Long? = null
    var state: String? = null
    var city: String? = null
    var lowestPrice: String? = null
    var highestPrice: String? = null

    fun to(): String {
        return categoryId + subCategoriesId + fromDate + toDate + state + city + lowestPrice + highestPrice
    }
}
