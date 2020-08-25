package br.com.goin.feature.full.image

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import br.com.goin.component.navigation.FullImageNavigationController

class FullImageNavigationControllerImpl: FullImageNavigationController {

   override fun goToFullImage(context: Context, url: String){
      FullImageActivity.starter(context, url)
   }

   override fun goToFullImage(activity: Activity, view: View, url: String){
      FullImageActivity.starter(activity, view, url)
   }
}

