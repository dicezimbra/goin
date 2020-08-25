package br.com.goin.feature.home.helper

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

object FragmentHelper {

    fun replace(context: AppCompatActivity, @IdRes container: Int, fragment: Fragment) {
        val supportFragmentManager = context.supportFragmentManager
        supportFragmentManager.beginTransaction()
                .replace(container, fragment, fragment.javaClass.simpleName)
                .commitAllowingStateLoss()

        supportFragmentManager.executePendingTransactions()
    }
}