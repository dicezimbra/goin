package br.goin.features.login.signUp.choosePicture

import android.app.Activity
import android.graphics.Bitmap
import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.component.session.user.UserSessionInteractor
import br.goin.features.login.model.UserInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class ChoosePicturePresenterImpl(val view: ChoosePictureView) : ChoosePicturePresenter {

    private var interactor = UserSessionInteractor.instance
    private var picInteractor = UserInteractor.instance
    private var disposable = CompositeDisposable()

    override fun onCreate() {
        view.configureClickListeners()
    }

    override fun onSavePhoto(activity: Activity, bitmap: Bitmap?) {
        val user = interactor.fetchUser()
        user?.let { userModel ->
            bitmap?.let { photo ->
                picInteractor.uploadProfilePicture(picture = photo, userId = userModel.id)
                        .ioThread()
                        .doOnSubscribe { view.showLoading() }
                        .subscribe({
                            view.onPhotoSendSuccess()
                        }, {
                            Log.e("ChoosePictureT", it.toString())
                        })
                        .addTo(disposable)
            }
        }
    }
}

