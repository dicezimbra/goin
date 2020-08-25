package br.com.goin.component.push

import br.com.goin.base.extensions.ioThread
import br.com.goin.component.session.user.UserModel
import io.reactivex.Completable

class UserPushEndpointInteractorImpl : UserPushEndpointInteractor {
    private val repository = UserPushRepository()

    override fun updateMessagingToken(token: String?): Completable {

        repository.savePushToken(token!!)
        val model = UserModel().apply {
            pushEndpoint = token ?: "null"
        }

        return repository.updateUser(model).ioThread()
                .flatMap { repository.updateUser(model).ioThread() }.ignoreElement()

    }

    override fun fetchPushToken(): String? {
        return repository.fetchPushToken()
    }

}
