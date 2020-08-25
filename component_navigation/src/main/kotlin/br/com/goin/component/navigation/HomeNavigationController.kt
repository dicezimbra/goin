package br.com.goin.component.navigation

import android.content.Context
import androidx.fragment.app.Fragment

interface HomeNavigationController {

    fun goToHome(context: Context, deepLink: String = "")
    fun getGoin(): Fragment
}

