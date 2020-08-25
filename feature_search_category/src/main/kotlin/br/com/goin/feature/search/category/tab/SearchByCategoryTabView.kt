package br.com.goin.feature.search.category.tab

import br.com.goin.feature.search.category.model.SearchFilter
import br.com.goin.feature.search.category.model.SearchFilterPageItem

interface SearchByCategoryTabView {
    fun showLoading()
    fun hideLoading()
    fun handleError(throwable: Throwable)
    fun receiveItens(itens: MutableList<SearchFilterPageItem>, location: Pair<Double, Double>?)
    fun onFilterChanged(searchFilter: SearchFilter)
    fun resetEndlessRecyclerview()
    fun onLoadedMore(itens: List<SearchFilterPageItem>)
    fun hideWithoutNearbyResult()
    fun showWithoutNearbyResult()
    fun onViewTypeChange(type: SearchByCategoryTabAdapter.VIEW_MODE)
}