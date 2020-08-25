package br.com.goin.component.model.category

import io.reactivex.Observable

class CategoriesInteractorImpl : CategoriesInteractor {

    private val repository = CategoriesRepository()

    override fun getCategories(): Observable<List<Category>> {
        return repository.getCategories()
    }
}
