package br.com.goin.feature.profile.model

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import br.com.goin.base.BaseApplication
import br.com.goin.base.BuildConfig
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import br.com.goin.component.rest.api.upload.UploadResponse
import br.com.goin.component.rest.api.upload.UploadService
import br.com.goin.component.session.user.UpdateUserModel
import br.com.goin.component.session.user.UserModel
import br.com.goin.feature.profile.edit.ProfileHelper
import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.ResponseBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class ProfileRepository {

    private val service = RetrofitService(ProfileApi::class.java, BuildConfig.BASE_URL)
    private val uploadService = UploadService()

    private var KEY = "Users/"

    fun follow(profileId: String): Completable {
        val graphqlBody = GraphqlBody.Builder(ProfileQueries.FOLLOW_USER)
                .`var`("userId", profileId)
                .build()

        return service.apiService.follow(graphqlBody)
    }

    fun unfollow(profileId: String): Completable {
        val graphqlBody = GraphqlBody.Builder(ProfileQueries.UNFOLLOW_USER)
                .`var`("userId", profileId)
                .build()

        return service.apiService.follow(graphqlBody)
    }

    fun findById(profileId: String): Observable<UserModel> {
        val graphqlBody = GraphqlBody.Builder(ProfileQueries.GET_USER_BY_ID)
                .`var`("id", profileId)
                .build()

        return service.apiService.findById(graphqlBody).map { it.data?.user }
    }

    fun onUpdateProfile(userModel: UpdateUserModel): Observable<ResponseBody> {
        val graphqlBody = GraphqlBody.Builder(ProfileQueries.UPDATE_PROFILE)
                .`var`("input", userModel)
                .build()

        return service.apiService.updateUserProfile(graphqlBody)
    }

    fun uploadProfilePicture(picture: Bitmap, imageName: String): Observable<ResponseBody> {
        return uploadService.uploadImage(picture, imageName)
    }

}