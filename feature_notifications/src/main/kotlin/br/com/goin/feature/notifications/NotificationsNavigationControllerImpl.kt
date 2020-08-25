package br.com.goin.feature.notifications

import android.content.Context
import android.content.Intent
import br.com.goin.component.navigation.NotificationsNavigationController

class NotificationsNavigationControllerImpl: NotificationsNavigationController{

    override fun goToNotifications(context: Context) {
        context.startActivity(Intent(context, NotificationsActivity::class.java))
    }
}