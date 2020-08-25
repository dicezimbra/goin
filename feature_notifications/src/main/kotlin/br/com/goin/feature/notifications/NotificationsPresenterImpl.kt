package br.com.goin.feature.notifications

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.extensions.useCache
import br.com.goin.base.helpers.PreferenceHelper
import br.com.goin.feature.notifications.model.Notification
import br.com.goin.feature.notifications.model.NotificationInteractor
import com.google.gson.reflect.TypeToken
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class NotificationsPresenterImpl(val view: NotificationsView) : NotificationsPresenter {

    private val interactor = NotificationInteractor.instance

    private var notifications: List<Notification>? = null
    private val disposable = CompositeDisposable()

    override fun onCreate() {
        view.configureRecyclerView()
        loadNotifications(null)
    }

    override fun onDestroy() {
        disposable.dispose()
    }

    override fun onFollowClick(notification: Notification) {
        when (notification.followedByMe) {
            true -> {
                view.showUnfollowDialog(notification)
            }
            false -> {
                notification.followedByMe = true
                notification.destinationId?.let {
                    followUser(it)
                }

                view.updateNotifications()
            }
        }
    }

    override fun onConfirmUnfollow(notification: Notification) {
        notification.followedByMe = false
        notification.destinationId?.let {
            unfollowUser(it)
        }
        view.updateNotifications()
    }

    private fun followUser(userId: String) {
        interactor.followUser(userId).ioThread().subscribe({}, { t: Throwable ->
            Log.e("NotificationPresenter", t.message, t)
        }).addTo(disposable)
    }

    private fun unfollowUser(userId: String) {
        interactor.unfollowUser(userId).ioThread().subscribe({}, { t: Throwable ->
            Log.e("NotificationPresenter", t.message, t)
        }).addTo(disposable)
    }

    private fun loadNotifications(lastId: String?) {
        interactor.fetch(lastId)
                .useCache("NOTIFICATIONS$lastId", object : TypeToken<ArrayList<Notification>>() {}.type)
                .ioThread()
                .doOnSubscribe { if (notifications == null) view.showLoading() }
                .filter { it.hashCode() != notifications?.hashCode() }
                .subscribe({
                    this.notifications = it
                    view.hideLoading()

                    if (it.isEmpty()) {
                        view.configureEmptyNotificaitons()
                    } else {
                        view.configureNotifications(it)
                    }
                }, { t: Throwable ->
                    view.configureEmptyNotificaitons()
                    Log.e("NotificationPresenter", t.message, t)
                }).addTo(disposable)
    }
}