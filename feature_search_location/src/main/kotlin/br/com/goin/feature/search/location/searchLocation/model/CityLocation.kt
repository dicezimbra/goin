package br.com.goin.feature.search.location.searchLocation.model

import androidx.annotation.DrawableRes
import br.com.goin.feature.search.location.R

data class CityLocation(val lat: Double? = null, val lng: Double? = null,
                        val title: String,
                        val toolbarTitle: String? = null,
                        val description: String? = null,
                        val city: String? = null,
                        val state: String? = null,
                        val uf: String? = null,
                        val highlight: Boolean = false,
                        @DrawableRes val iconRes: Int = R.drawable.ic_location_on_gray_24dp){

    fun hasLatLng() = lat != null && lng != null
}