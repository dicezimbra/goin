package br.com.goin.feature.search.category.model

import br.com.goin.component.model.event.Event
import java.io.Serializable

import br.com.goin.component.model.event.api.response.ApiEvent

data class SearchFilterPage( var currentPage: Int = 0,
                             var totalPages: Int? = null,
                             var totalItems: Int? = null,
                             var itens: MutableList<SearchFilterPageItem> = arrayListOf()) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SearchFilterPage

        if (currentPage != other.currentPage) return false
        if (totalPages != other.totalPages) return false
        if (totalItems != other.totalItems) return false
        if (itens != other.itens) return false

        return true
    }

    override fun hashCode(): Int {
        var result = currentPage
        result = 31 * result + (totalPages ?: 0)
        result = 31 * result + (totalItems ?: 0)
        result = 31 * result + itens.hashCode()
        return result
    }
}
