package br.com.goin.component.model.category

import io.reactivex.Observable

interface CategoriesInteractor {

    companion object {
        val instance: CategoriesInteractor = CategoriesInteractorImpl()
    }

    fun getCategories(): Observable<List<Category>>
}
