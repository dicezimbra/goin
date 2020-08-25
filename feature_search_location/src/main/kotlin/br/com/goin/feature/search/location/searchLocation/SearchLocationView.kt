package br.com.goin.feature.search.location.searchLocation

import br.com.goin.feature.search.location.searchLocation.model.CityLocation

interface SearchLocationView {
    fun configureRecyclerView()
    fun configureSearchField()
    fun configureCities(cityLocations: List<CityLocation>)
    fun hideLoading()
    fun showLoading(query: String?)
    fun showEmptyResult(query: String)
    fun askLocationPermission()
    fun finish()
    fun configureLocationChange()
    fun sendIntentResult(it: String)
    fun handleError(throwable: Throwable, query: String)
}