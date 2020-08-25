package br.com.goin.feature.search.event

import br.com.goin.feature.search.event.model.api.SearchByNameResponse
import br.com.goin.feature.search.event.model.api.SearchResponse
import br.com.goin.feature.search.event.model.api.SearchResponseByCategory
import java.io.Serializable

interface SearchPresenter {

    fun onCreate()
    fun onReceiveData(data: SearchResponseByCategory?)
    fun onSearch(querySearch: String)
    fun onReceivedCategory(categoryId: Serializable?)
    fun onDestroy()
}