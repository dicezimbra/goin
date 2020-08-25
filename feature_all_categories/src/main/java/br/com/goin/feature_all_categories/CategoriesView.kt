package br.com.goin.feature_all_categories

import br.com.goin.component.session.user.location.UserLocation
import br.com.goin.feature_all_categories.model.CategoryDetails

interface CategoriesView {
    fun configureCategories(categories: ArrayList<CategoryDetails>)
    fun configureToolbar()
    fun configureCurrentLocation(userLocation: UserLocation)
}