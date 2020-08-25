package br.com.goin.findfriends

import android.content.Context
import br.com.goin.component.navigation.FindFriendsNavigationController

class FindFriendsNavigationControllerImpl : FindFriendsNavigationController {

    override fun goToFindFriends(context: Context) {
        FindFriendsActivity.starter(context)
    }
}