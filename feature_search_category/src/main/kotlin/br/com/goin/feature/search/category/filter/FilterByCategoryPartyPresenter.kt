package br.com.goin.feature.search.category.filter

import br.com.goin.feature.search.category.model.ResponseSubcategories
import br.com.goin.feature.search.category.model.SubcategoriesModel

interface FilterByCategoryPartyPresenter {

    fun onCreate()
    fun receiveSubCategories(subcategories: ResponseSubcategories)
    fun onDestroy()
    fun receiveSelectedSubCategories(subCategory: Any?)
}