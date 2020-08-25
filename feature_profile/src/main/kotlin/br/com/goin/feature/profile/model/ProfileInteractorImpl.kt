package br.com.goin.feature.profile.model

import android.graphics.Bitmap
import br.com.goin.component.rest.api.upload.UploadResponse
import br.com.goin.component.session.user.UserModel
import br.com.goin.component.session.user.UserSessionInteractor
import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.ResponseBody

class ProfileInteractorImpl : ProfileInteractor {

    private val repository = ProfileRepository()

    override fun unfollow(profileId: String): Completable {
        return repository.unfollow(profileId)
    }

    override fun follow(profileId: String): Completable {
        return repository.follow(profileId)
    }

    override fun findById(profileId: String): Observable<UserModel> {
        return repository.findById(profileId)
    }

    override fun onUpdateProfile(user: UserModel?): Observable<ResponseBody> {



        return repository.onUpdateProfile(user!!.convert())
    }

    override fun uploadPhoto(picture: Bitmap, imageName: String): Observable<ResponseBody> {
        return repository.uploadProfilePicture(picture, imageName)
    }
}