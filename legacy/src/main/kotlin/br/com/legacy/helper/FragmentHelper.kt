package br.com.legacy.helper

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity

object FragmentHelper {

    fun replace(context: AppCompatActivity, @IdRes container: Int, fragment: androidx.fragment.app.Fragment) {
        val supportFragmentManager = context.supportFragmentManager
        supportFragmentManager.beginTransaction()
                .replace(container, fragment, fragment.javaClass.simpleName)
                .commitAllowingStateLoss()

        supportFragmentManager.executePendingTransactions()
    }
}