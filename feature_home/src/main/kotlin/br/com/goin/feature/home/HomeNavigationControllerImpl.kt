package br.com.goin.feature.home

import android.content.Context
import androidx.fragment.app.Fragment
import br.com.goin.component.navigation.HomeNavigationController
import br.com.goin.feature.home.goin.GoinFragment

class HomeNavigationControllerImpl : HomeNavigationController {

    override fun goToHome(context: Context, deeplink: String) {
        HomeActivity.starter(context, deeplink)
    }

    override fun getGoin(): Fragment{
        return GoinFragment.newInstance()
    }
}