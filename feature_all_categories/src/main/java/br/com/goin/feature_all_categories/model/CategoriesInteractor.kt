package br.com.goin.model.categories

import io.reactivex.Observable
import br.com.goin.feature_all_categories.model.CategoryDetails
interface CategoriesInteractor {

    companion object {
        val instance: CategoriesInteractor = CategoriesInteractorImpl()
    }

    fun getCategories(): Observable<List<CategoryDetails>>
}
