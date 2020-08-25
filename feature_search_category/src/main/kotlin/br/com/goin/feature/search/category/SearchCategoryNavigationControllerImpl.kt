package br.com.goin.feature.search.category

import android.content.Context
import br.com.goin.component.navigation.SearchCategoryNavigationController

class SearchCategoryNavigationControllerImpl: SearchCategoryNavigationController {

    override fun goToSearchCategory(context: Context, categoryType: String , categoryName: String, categoryId: String, categoryBanner: String?){
        SearchByCategoryActivity.start(context, categoryType, categoryId, categoryName, categoryBanner)
    }
}