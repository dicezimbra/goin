package br.com.goin.component.navigation

import android.content.Context

interface SearchNavigationController{
    fun goToSearch(context: Context, categoryId: String?)
}