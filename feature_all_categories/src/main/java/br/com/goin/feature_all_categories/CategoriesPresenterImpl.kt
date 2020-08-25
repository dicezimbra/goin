package br.com.goin.feature_all_categories

import br.com.goin.component.session.user.location.UserLocationInteractor
import br.com.goin.feature_all_categories.model.CategoryDetails
import java.io.Serializable

class CategoriesPresenterImpl(val view: CategoriesView) : CategoriesPresenter {

    private var categories: ArrayList<CategoryDetails>? = null
    private val userLocationInteractor = UserLocationInteractor.instance

    override fun onCreate() {
        view.configureToolbar()
        view.configureCurrentLocation(userLocationInteractor.fetch())

        categories?.let {
            view.configureCategories(it)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onReceivedCategories(serializableExtra: Serializable?) {
        categories = serializableExtra as? ArrayList<CategoryDetails>
    }
}