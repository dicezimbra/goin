package br.com.goin.feature.search.event

import android.content.Context
import br.com.goin.component.navigation.SearchNavigationController

class SearchNavigationControllerImpl: SearchNavigationController {

    override fun goToSearch(context: Context, categoryId: String?) {
        SearchActivity.starter(context, categoryId)
    }
}