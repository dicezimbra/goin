package br.com.goin.feature_social

import android.app.Activity
import br.com.goin.component.navigation.FollowingNavigationController

class FollowNavigationControllerImpl : FollowingNavigationController {

    override fun goToFollowings(activity: Activity, currentUserId: String, requestCode: Int) {
        FollowActivity.starter(activity, currentUserId, FollowRelation.FOLLOWING, requestCode)
    }

    override fun goToFollowers(activity: Activity, currentUserId: String, requestCode: Int) {
        FollowActivity.starter(activity, currentUserId, FollowRelation.FOLLOWERS, requestCode)
    }
}