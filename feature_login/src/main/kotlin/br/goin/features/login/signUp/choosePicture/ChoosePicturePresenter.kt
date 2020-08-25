package br.goin.features.login.signUp.choosePicture

import android.app.Activity
import android.graphics.Bitmap

interface ChoosePicturePresenter {
    fun onCreate()
    fun onSavePhoto(activity: Activity, bitmap: Bitmap?)

}
