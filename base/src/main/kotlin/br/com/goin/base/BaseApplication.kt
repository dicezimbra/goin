package br.com.goin.base

import android.annotation.SuppressLint
import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

class BaseApplication{

    var context: Context? = null

    companion object {
        @SuppressLint("StaticFieldLeak")
        var instance: BaseApplication = BaseApplication()
    }

    fun onCreate(context: Context? = null){
        this.context = context

        FirebaseApp.initializeApp(context)
    }
}