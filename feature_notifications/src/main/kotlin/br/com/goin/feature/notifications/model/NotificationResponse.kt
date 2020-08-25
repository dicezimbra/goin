package br.com.goin.feature.notifications.model

import com.google.gson.annotations.SerializedName

data class NotificationResponse(@SerializedName("notifications") val notification: List<Notification>)
