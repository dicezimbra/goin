package br.com.goin.feature.search.category.tab

import br.com.goin.feature.search.category.model.SearchFilter
import java.io.Serializable

interface SearchByCategoryTabPresenter {
    fun onCreate()
    fun onReceiveFilterRequest(serializable: Serializable?)
    fun onDestroy()
    fun onReceiveSearchFilterTimePeriodRequest(serializable: Serializable?)
    fun refreshFilter(searchFilter: SearchFilter)
    fun loadMore(page: Int)
}