package br.com.goin.feature.profile.model

import android.graphics.Bitmap
import br.com.goin.component.rest.api.upload.UploadResponse
import br.com.goin.component.session.user.UserModel
import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.ResponseBody

interface ProfileInteractor {

    companion object {
        val instance: ProfileInteractor = ProfileInteractorImpl()
    }

    fun onUpdateProfile(user: UserModel?): Observable<ResponseBody>
    fun uploadPhoto(picture: Bitmap, userId: String): Observable<ResponseBody>

    fun findById(profileId: String): Observable<UserModel>
    fun follow(profileId: String): Completable
    fun unfollow(profileId: String): Completable
}