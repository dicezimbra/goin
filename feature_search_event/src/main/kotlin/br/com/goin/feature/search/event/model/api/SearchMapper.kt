package br.com.goin.feature.search.event.model.api

import br.com.goin.feature.search.event.model.CategorySearch

class SearchMapper {

    fun mapper(categories: List<CategorySearch>): SearchResponseByCategory {

        val response = SearchResponseByCategory(categories.toMutableList())
        return response
    }
}