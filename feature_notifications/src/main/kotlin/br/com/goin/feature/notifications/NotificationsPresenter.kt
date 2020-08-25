package br.com.goin.feature.notifications

import br.com.goin.feature.notifications.model.Notification

interface NotificationsPresenter {
    fun onDestroy()
    fun onCreate()
    fun onConfirmUnfollow(notification: Notification)
    fun onFollowClick(notification: Notification)
}