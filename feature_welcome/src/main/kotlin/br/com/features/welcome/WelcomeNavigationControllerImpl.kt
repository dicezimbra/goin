package br.com.features.welcome

import android.content.Context
import br.com.goin.component.navigation.WelcomeNavigationController

class WelcomeNavigationControllerImpl: WelcomeNavigationController {

    override fun goToWelcome(context: Context) {
        WelcomeActivity.starter(context)
    }
}

