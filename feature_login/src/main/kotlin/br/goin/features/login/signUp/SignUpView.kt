package br.com.goin.features.signUp

import androidx.annotation.StringRes

interface SignUpView {
    fun hideLoading()
    fun showLoading()
    fun goToChooseProfilePicture()
    fun finishActivity()
    fun showDialogOnError(@StringRes error: Int)
    fun saveEmailOnSharedPreferences()
    fun setupTextTerms()
    fun setupClickListeners()
    fun setupTypeface()
}