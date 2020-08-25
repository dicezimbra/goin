package br.com.goin.component.session.user

class  UserSessionInteractorImpl : UserSessionInteractor {

    private val repository = UserSessionRepository()


    override fun saveToken(token: String, refreshToken: String?, identityProvider: String) {
        repository.saveToken(token, refreshToken, identityProvider)
    }

    override fun clearToken() {
        repository.clearToken()
    }

    override fun fetchToken(): Token? {
        return repository.fetchToken()
    }

    override fun save(userModel: UserModel) {
        repository.saveUser(userModel)
    }

    override fun isLoggedIn(): Boolean {
        return repository.fetchToken() != null
    }

    override fun fetchUser(): UserModel? {
        return repository.fetchUser()
    }

}
