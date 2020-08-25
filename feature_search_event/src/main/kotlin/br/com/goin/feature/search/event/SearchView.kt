package br.com.goin.feature.search.event

import br.com.goin.feature.search.event.model.api.SearchEvent

interface SearchView {

    fun initUI()
    fun hideLoading()
    fun showLoading(msg: String?)
    fun configureEventsEmpty()
    fun onReceiveSuccess(hash: LinkedHashMap<String, MutableList<SearchEvent>>)
    fun hideTabLayout()
    fun showTabLayout()
    fun handleError(throwable: Throwable, query: String)
}