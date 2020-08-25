package br.com.goin.feature.search.category.filter

import br.com.goin.feature.search.category.filter.model.ChipTagResponse
import br.com.goin.feature.search.category.model.ResponseSubcategories

interface FilterByCategoryPartyView {

    fun configureView()
    fun configureExtras()
    fun configureToolbar()
    fun configureRangeBar()
    fun receiveChipTags(chiptagsList: ChipTagResponse)
    fun receiveSubCategories(subcategories: ResponseSubcategories, selectedSubCategory: Int?)
    fun configureFilter(isActivated: Boolean)
}