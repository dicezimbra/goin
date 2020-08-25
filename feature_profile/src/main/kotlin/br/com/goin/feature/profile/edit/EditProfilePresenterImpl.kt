package br.com.goin.feature.profile.edit

import android.graphics.Bitmap
import android.util.Log
import br.com.goin.base.BuildConfig
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.utils.Constants
import br.com.goin.component.session.user.UserModel
import br.com.goin.component.session.user.UserSessionInteractor
import br.com.goin.feature.profile.R
import br.com.goin.feature.profile.model.ProfileInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class EditProfilePresenterImpl(val view: EditProfileView) : EditProfilePresenter {

    private val imagePath = "${BuildConfig.BUCKET_PATH}${Constants.USERS_PATH}/"
    private val ext = ".png"

    private val interactor = ProfileInteractor.instance
    private val userInteractor = UserSessionInteractor.instance
    private val disposable = CompositeDisposable()

    override fun onCreate() {
        view.configureToolbar()
        view.configureClickListeners()
        loadProfileInfo()
    }

    private fun loadProfileInfo() {
        val user = userInteractor.fetchUser()
        user?.let {
            view.loadUserInfo(it)
        }
    }

    override fun updateProfile(name: String?, picture: Bitmap?) {

        view.showLoading()
        val user = userInteractor.fetchUser()
        val profilePictureFullPath = buildProfilePicturePath(user)
        val imageName = buildProfilePictureName(user)

        if (name.isNullOrEmpty()) {
            view.showMessageError(R.string.error_name_empty)
            return
        }

        if(user?.name.equals(name) && picture == null){
            view.onSuccessUpdate()
            return
        }

        user?.name = name!!

        if (picture == null) {
            user?.let {  user ->
                interactor.onUpdateProfile(user).ioThread()
                        .subscribe({
                            userInteractor.save(user)
                            view.onSuccessUpdate()
                        }, { throwable ->
                            Log.e("EditProfilePresenter", throwable.message, throwable)
                            view.showError(throwable)
                        }).addTo(disposable)
            }
        } else {
            picture?.let { pic ->
                user?.let { user ->
                    interactor.uploadPhoto(pic, imageName)
                            .ioThread()
                            .flatMap {
                                if (!profilePictureFullPath.isNullOrEmpty()) {
                                    user?.profilePicture = profilePictureFullPath
                                }
                                interactor.onUpdateProfile(user).ioThread()
                            }
                            .subscribe({
                                userInteractor.save(user)
                                view.onSuccessUpdate()
                            }, { throwable ->
                                Log.e("EditProfilePresenter", throwable.message, throwable)
                                view.showError(throwable)
                            }).addTo(disposable)

                }
            }
        }
    }

    private fun buildProfilePictureName(user: UserModel?) =
            "${Constants.USERS_PATH}/${user?.id}$ext"

    private fun buildProfilePicturePath(user: UserModel?) =
            "$imagePath${user?.id}$ext"

}