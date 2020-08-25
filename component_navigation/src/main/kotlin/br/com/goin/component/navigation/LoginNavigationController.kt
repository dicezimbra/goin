package br.com.goin.component.navigation

import android.content.Context

interface LoginNavigationController{
    fun goToLogin(context: Context, listener: () -> Unit)
    fun goToForgotPassword(context: Context, fromApp: Boolean)
}