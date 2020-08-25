package br.com.goin.feature.search.location.searchLocation

import br.com.goin.feature.search.location.searchLocation.model.CityLocation

interface SearchLocationPresenter {
    fun onCreate()
    fun onSearchChanged(query: String)
    fun onLoadCurrentLocation(cityLocation: CityLocation)
    fun onDestroy()
    fun onLocationClicked(cityLocation: CityLocation)
}