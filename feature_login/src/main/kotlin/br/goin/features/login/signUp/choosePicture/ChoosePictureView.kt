package br.goin.features.login.signUp.choosePicture

interface ChoosePictureView {
    fun configureClickListeners()
    fun openGallery()
    fun askForGalleryPermissions()
    fun onErrorUploadPhoto(e: Exception)
    fun onPhotoSendSuccess()
    fun showLoading()

}
