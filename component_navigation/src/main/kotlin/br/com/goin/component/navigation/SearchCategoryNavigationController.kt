package br.com.goin.component.navigation

import android.content.Context
import androidx.fragment.app.Fragment

interface SearchCategoryNavigationController{
    fun goToSearchCategory(context: Context,categoryType: String, categoryName: String, categoryId: String, categoryBanner: String? = null)
}