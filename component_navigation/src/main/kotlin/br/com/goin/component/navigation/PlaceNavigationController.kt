package br.com.goin.component.navigation

import android.content.Context

interface PlaceNavigationController{
    fun goToPlace(context: Context, placeId: String, categoryName: String)
    fun goToPlaceRatings(context: Context, placeId: String)
}