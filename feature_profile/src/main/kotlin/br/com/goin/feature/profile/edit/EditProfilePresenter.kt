package br.com.goin.feature.profile.edit

import android.graphics.Bitmap

interface EditProfilePresenter {
    fun onCreate()
    fun updateProfile(name: String?, picture: Bitmap?)
}