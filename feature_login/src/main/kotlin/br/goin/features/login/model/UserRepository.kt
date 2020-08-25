package br.goin.features.login.model

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import br.com.goin.base.BaseApplication
import br.com.goin.base.BuildConfig
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import br.com.goin.component.rest.api.upload.UploadResponse
import br.com.goin.component.rest.api.upload.UploadService
import br.com.goin.component.session.user.UserModel
import br.goin.features.login.login.LoginRequest
import br.goin.features.login.login.LoginResponse
import br.goin.features.login.password.forgotpassword.model.ForgotPasswordResponse
import br.goin.features.login.register.NewRegisterUserInput
import br.goin.features.login.signUp.choosePicture.ChooseProfileHelper
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class UserRepository {

    companion object {
        var service = RetrofitService(UserApi::class.java, BuildConfig.BASE_URL)
        var uploadService = UploadService()
    }

    fun login(email: String, password: String): Observable<LoginResponse> {
        val body = LoginRequest(email, password)
        return service.apiService.login(body)
    }

    fun register(email: String, name: String, password: String, inviteCode: String?): Observable<br.goin.features.login.register.NewRegisterUserResponse> {
        val registerUserInput = NewRegisterUserInput(email = email, password = password, name = name, invitedBy = inviteCode)
        val graphqlQuery = GraphqlBody.Builder(UserQueries.REGISTER)
                .`var`("input", registerUserInput)
                .build()

        return service.apiService.register(graphqlQuery).map { it.data?.newRegisterUser }
    }

    fun syncUser(): Observable<UserModel> {
        val graphqlQuery = GraphqlBody.Builder(UserQueries.GET_MY_USER)
                .build()
        return service.apiService.getUser(graphqlQuery).map { it.data?.myUser }
    }

    fun updateUser(userModel: UserModel): Single<UserModel> {
        userModel.clean()
        val graphqlQuery = GraphqlBody.Builder(UserQueries.UPDATE_USER)
                .`var`("input", userModel)
                .build()

        return service.apiService.updateUser(graphqlQuery).map { it.data }
    }

    fun forgotPassword(email: String): Single<ForgotPasswordResponse> {
        val graphqlQuery = GraphqlBody.Builder(UserQueries.FORGOT_PASSWORD)
                .`var`("email", email)
                .build()
        return service.apiService.forgotPassword(graphqlQuery).map { it.data }
    }

    fun resetPassword(
            verificationCode: String,
            newPassword: String,
            email: String,
            passwordConfirmation: String
    ): Single<GraphQLResponse<ForgotPasswordResponse>> {

        val graphqlQuery = GraphqlBody.Builder(UserQueries.RESET_PASSWORD)
                .`var`("email", email)
                .`var`("verificationCode", verificationCode)
                .`var`("newPassword", newPassword)
                .`var`("passwordConfirmation", passwordConfirmation)
                .build()

        return service.apiService.resetPassword(graphqlQuery)
    }

    fun changePassword(
            email: String,
            oldPassword: String,
            password: String,
            passwordConfirmation: String
    ): Single<GraphQLResponse<ForgotPasswordResponse>> {

        val graphqlQuery = GraphqlBody.Builder(UserQueries.CHANGE_PASSWORD)
                .`var`("email", email)
                .`var`("oldPassword", oldPassword)
                .`var`("newPassword", password)
                .`var`("passwordConfirmation", passwordConfirmation)
                .build()

        return service.apiService.changePassword(graphqlQuery)
    }

    fun logWithFacebook(accessToken: String, identityProvider: String): Observable<UserModel> {
        return service.apiService.loginFacebook(accessToken, identityProvider)
    }


    fun uploadProfilePicture(picture: Bitmap, imageName: String): Observable<ResponseBody> {
        return uploadService.uploadImage(picture, imageName)
    }
}
