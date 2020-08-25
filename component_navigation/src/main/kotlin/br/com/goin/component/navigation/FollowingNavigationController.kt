package br.com.goin.component.navigation

import android.app.Activity


interface FollowingNavigationController {

    fun goToFollowings(activity: Activity, currentUserId: String, requestCode: Int)
    fun goToFollowers(activity: Activity, currentUserId: String, requestCode: Int)
}