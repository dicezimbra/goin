package br.com.goin.component.navigation

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment

interface FullImageNavigationController{
   fun goToFullImage(context: Context, url: String)
   fun goToFullImage(activity: Activity, view: View, url: String)
}

