package br.com.goin.feature.search.category

import br.com.goin.component.model.event.Event
import br.com.goin.component.model.event.api.response.ApiEvent
import br.com.goin.feature.search.category.model.*
import br.com.goin.feature.search.category.model.api.SearchFilterRequest

interface SearchByCategoryView {
    fun receiveSubCategories(subcategories: ResponseSubcategories)
    fun configureToolbarFilter(isFilterActive: Boolean)
    fun configureSearchbar(cityName: String)
    fun logScreenView(caegory: String)
    fun onFilterChanged(searchFilter: SearchFilter)
    fun configureTabs(searchFilter: SearchFilter, searchFilterTimePeriod: List<SearchFilterTimePeriod>, categoryName: String)
    fun showTabLayout()
    fun hideTabLayout()
    fun goToFilterByCategory(categoryId: String, searchFilterRequest: SearchFilter, searchFilterTimePeriod: SearchFilterTimePeriod?, subCategories: ResponseSubcategories?,categoryName: String?)
    fun configureToolbar(location: String)
    fun changeViewType()
    fun configureSubCategories(searchFilter: SearchFilter)
    fun onClickSubCategories(it: SubcategoriesModel?, hasFilterOn: Boolean)
}