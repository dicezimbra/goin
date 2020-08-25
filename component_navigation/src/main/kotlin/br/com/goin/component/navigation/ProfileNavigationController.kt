package br.com.goin.component.navigation

import android.content.Context
import android.app.Activity
import androidx.fragment.app.Fragment

interface ProfileNavigationController {
    fun getProfile(): Fragment
    fun goToProfile(context: Context, profileId: String?)
    fun goToEditProfile(activity: Activity)
}