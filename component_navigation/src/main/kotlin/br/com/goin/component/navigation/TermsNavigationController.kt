package br.com.goin.component.navigation

import android.content.Context

interface TermsNavigationController{
    fun goToTermOfUse(context: Context)
    fun goToHalfPricePolicy(context: Context)
    fun goToPrivacyPolicy(context: Context)

}