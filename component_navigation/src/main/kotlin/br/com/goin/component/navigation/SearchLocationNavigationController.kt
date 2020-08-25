package br.com.goin.component.navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

interface SearchLocationNavigationController{
    fun goToSearch(fragment: Fragment)
    fun goToSearch(activity: AppCompatActivity)
}
