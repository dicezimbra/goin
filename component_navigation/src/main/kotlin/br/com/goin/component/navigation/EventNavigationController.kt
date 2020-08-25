package br.com.goin.component.navigation

import android.content.Context

interface EventNavigationController{

    fun goToEvent(context: Context, eventId: String?, clubId: String? = null)
}