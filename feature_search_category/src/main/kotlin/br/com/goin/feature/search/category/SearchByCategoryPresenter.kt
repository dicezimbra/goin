package br.com.goin.feature.search.category

import br.com.goin.feature.search.category.model.SearchFilter
import br.com.goin.feature.search.category.model.SearchFilterTimePeriod
import br.com.goin.feature.search.category.model.SubcategoriesModel
import br.com.goin.feature.search.category.model.api.SearchFilterRequest

interface SearchByCategoryPresenter {
    fun onDestroy()
    fun onCreate()
    fun onReceiveCategory(categoryType: String,
                          categoryId: String,
                          categoyName: String?,
                          categoryBanner: String?)
    fun logScreenName()
    fun onRefresh(searchFilterRequest: SearchFilter?, searchFilterTimePeriod: SearchFilterTimePeriod?)
    fun onClickFilter()
    fun onClickSubcategory(subcategoriesModel: SubcategoriesModel?)
}