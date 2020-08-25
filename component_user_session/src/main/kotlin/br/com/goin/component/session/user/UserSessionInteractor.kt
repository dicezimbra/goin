package br.com.goin.component.session.user

interface UserSessionInteractor {

    companion object {
        val instance: UserSessionInteractor by lazy { UserSessionInteractorImpl() }
    }

    fun fetchToken(): Token?
    fun fetchUser(): UserModel?
    fun isLoggedIn(): Boolean
    fun save(userModel: UserModel)
    fun saveToken(token: String, refreshToken: String?, identityProvider: String)
    fun clearToken()
}