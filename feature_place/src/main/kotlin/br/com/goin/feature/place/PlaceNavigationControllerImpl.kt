package br.com.goin.feature.place

import android.content.Context
import br.com.goin.component.navigation.PlaceNavigationController
import br.com.goin.feature.rating.RatingActivity

class PlaceNavigationControllerImpl: PlaceNavigationController {
    override fun goToPlaceRatings(context: Context, placeId: String) {
        RatingActivity.starter(context, placeId)
    }

    override fun goToPlace(context: Context, placeId: String, categoryName: String){
        PlaceActivity.starter(context, placeId, categoryName)
    }
}