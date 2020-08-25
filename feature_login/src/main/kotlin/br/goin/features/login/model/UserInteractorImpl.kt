package br.goin.features.login.model

import android.graphics.Bitmap
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.session.user.UserModel
import br.com.goin.component.session.user.UserSessionInteractor
import br.com.goin.base.BuildConfig
import br.com.goin.base.extensions.ioThread
import br.com.goin.component.rest.api.upload.UploadResponse
import br.com.goin.component.session.user.model.NewRegisterUserResponse
import br.goin.features.login.password.forgotpassword.model.ForgotPasswordResponse
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody

class UserInteractorImpl : UserInteractor {


    private val IDENTITY_PROVIDER = BuildConfig.COGNITO_IP_PREFIX + BuildConfig.COGNITO_USER_POOL_ID

    private val repository = UserRepository()
    private val userSessionInteractor = UserSessionInteractor.instance

    override fun login(email: String, password: String): Observable<UserModel> {
        return repository.login(email, password)
                .doOnNext { userSessionInteractor.saveToken(it.token, it.refreshToken, IDENTITY_PROVIDER) }
                .flatMap { repository.syncUser().ioThread() }
                .doOnError { userSessionInteractor.clearToken() }
                .doOnNext { userSessionInteractor.save(it) }
    }

    override fun register(email: String, name: String, password: String, inviteCode: String?): Observable<UserModel> {
        return repository.register(email, name, password, inviteCode)
                .doOnNext { userSessionInteractor.saveToken(it.token, it.refreshToken, IDENTITY_PROVIDER) }
                .flatMap { repository.syncUser().ioThread() }
                .doOnNext { userSessionInteractor.save(it) }
                .doOnError { userSessionInteractor.clearToken() }
    }

    override fun syncUser(): Observable<UserModel> {
        return repository.syncUser()
    }

    override fun changePassword(
            email: String,
            oldPassword: String,
            password: String,
            passwordConfirmation: String
    ): Single<GraphQLResponse<ForgotPasswordResponse>> {
        return repository.changePassword(email, oldPassword, password, passwordConfirmation)
    }

    override fun forgotPassword(email: String) = repository.forgotPassword(email)

    override fun resetPassword(
            code: String,
            newPassword: String,
            email: String,
            passwordConfirmation: String
    ): Single<GraphQLResponse<ForgotPasswordResponse>> {
        return repository.resetPassword(code, newPassword, email, passwordConfirmation)
    }

    override fun loginWithFacebook(accessToken: String, identityProvider: String): Observable<UserModel> {
        return repository.logWithFacebook(accessToken, identityProvider)
                .doOnNext { userSessionInteractor.saveToken(accessToken, null, identityProvider) }
                .doOnNext { userSessionInteractor.save(it) }
    }

    override fun uploadProfilePicture(picture: Bitmap, userId: String): Observable<ResponseBody> {
        val userModel = userSessionInteractor.fetchUser()
        val key = "Users/$userId"
        return repository.uploadProfilePicture(picture, key)?.doOnNext { userModel?.profilePicture = key }
    }
}
