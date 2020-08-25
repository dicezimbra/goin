package br.com.goin.component.push

import android.content.Intent
import android.util.Log
import br.com.goin.base.utils.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable

class MessagingService : FirebaseMessagingService() {

    private val interactor = UserPushEndpointInteractor.instance

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)

        val intent = Intent()
        intent.action = Constants.RECEIVED_NOTIFICATION
        sendBroadcast(intent)
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)

        interactor.updateMessagingToken(p0).subscribe(object : CompletableObserver {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
                Log.e("MessagingService", e.message, e)
            }
        })
    }

}