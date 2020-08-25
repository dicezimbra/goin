package br.com.goin.feature.notifications.model

import com.google.gson.annotations.SerializedName

enum class NotificationType {
    undefined, comment, like, invite, follow, raffleWon, eventComing, friendCheckin, commentTag, postTag, addedToGroup, ticketReceived, ticketConfirmed, vipBoxInvite, THEATER
}

data class Notification(@SerializedName("id") val id: String,
                        @SerializedName("firstName") val userName: String? = "",
                        @SerializedName("secondName") val secondName: String? = "",
                        @SerializedName("avatar") val avatar: String? = null,
                        @SerializedName("type") val type: NotificationType,
                        @SerializedName("categoryType") val categoryType: String? = null,
                        @SerializedName("timeStamp") val time: Long? = null,
                        @SerializedName("creatorId") val creatorId: String? = null,
                        @SerializedName("destinationId") val destinationId: String? = null,
                        @SerializedName("followedByMe") var followedByMe: Boolean? = false)
