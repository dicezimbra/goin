package br.com.goin.feature.notifications

import br.com.goin.feature.notifications.model.Notification

interface NotificationsView {
    fun configureRecyclerView()
    fun configureNotifications(notiications: List<Notification>)
    fun showLoading()
    fun hideLoading()
    fun configureEmptyNotificaitons()
    fun showUnfollowDialog(notification: Notification)
    fun updateNotifications()
}