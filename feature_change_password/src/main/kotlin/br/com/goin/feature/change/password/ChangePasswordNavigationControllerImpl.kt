package br.com.goin.feature.change.password

import android.app.Activity
import br.com.goin.component.navigation.ChangePasswordNavigationController
import br.com.goin.component.navigation.ConfigurationNavigationController

class ChangePasswordNavigationControllerImpl : ChangePasswordNavigationController {

    override fun goToChangePassword(activity: Activity){
        ChangePasswordActivity.starter(activity)
    }
}