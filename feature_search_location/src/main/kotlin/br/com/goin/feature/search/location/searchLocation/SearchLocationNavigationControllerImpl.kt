package br.com.goin.feature.search.location.searchLocation

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.goin.component.navigation.SearchLocationNavigationController

class SearchLocationNavigationControllerImpl: SearchLocationNavigationController {

    override fun goToSearch(fragment: Fragment){
        SearchLocationActivity.starter(fragment)
    }

    override fun goToSearch(activity: AppCompatActivity){
        SearchLocationActivity.starter(activity)
    }
}
