package br.com.goin.component.navigation

import androidx.fragment.app.Fragment

interface FeedNavigationController{

    fun getFeed(userId: String? = null): Fragment

}