package br.com.goin.model.categories

import io.reactivex.Observable
import br.com.goin.feature_all_categories.model.CategoryDetails
class CategoriesInteractorImpl : CategoriesInteractor {

    private val repository = CategoriesRepository()

    override fun getCategories(): Observable<List<CategoryDetails>> {
        return repository.getCategories()
    }
}
