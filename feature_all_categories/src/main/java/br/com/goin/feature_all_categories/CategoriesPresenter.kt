package br.com.goin.feature_all_categories

import br.com.goin.component.session.user.location.UserLocation
import java.io.Serializable

interface CategoriesPresenter {
    fun onCreate()
    fun onReceivedCategories(serializableExtra: Serializable?)
}