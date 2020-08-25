package br.goin.features.login.model

import android.graphics.Bitmap
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.upload.UploadResponse
import br.com.goin.component.session.user.UserModel
import br.com.goin.component.session.user.model.NewRegisterUserResponse
import br.goin.features.login.password.forgotpassword.model.ForgotPasswordResponse
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody

interface UserInteractor {

    companion object {
        val instance: UserInteractor by lazy { UserInteractorImpl() }
    }

    fun syncUser(): Observable<UserModel>
    fun changePassword(email: String, oldPassword: String, password: String, passwordConfirmation: String): Single<GraphQLResponse<ForgotPasswordResponse>>
    fun forgotPassword(email: String): Single<ForgotPasswordResponse>
    fun resetPassword(code: String, newPassword: String, email: String, passwordConfirmation: String): Single<GraphQLResponse<ForgotPasswordResponse>>
    fun login(email: String, password: String): Observable<UserModel>
    fun register(email: String, name: String, password: String, inviteCode: String?): Observable<UserModel>
    fun loginWithFacebook(accessToken: String, identityProvider: String): Observable<UserModel>
    fun uploadProfilePicture(picture: Bitmap, userId: String): Observable<ResponseBody>
}