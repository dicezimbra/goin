package br.com.goin.feature_social.model

import com.google.gson.annotations.SerializedName

data class UserCardDetailsResponse(
        @SerializedName("id") val id: String? = null,
        @SerializedName("followerName") val followerName: String? = null,
        @SerializedName("followerAvatar") val followerAvatar: String? = null,
        @SerializedName("userName") val userName: String? = null,
        @SerializedName("userAvatar") val userAvatar: String? = null,
        @SerializedName("followedByMe") val followedByMe: Boolean? = null,
        @SerializedName("userId") val userId: String? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("avatar") val avatar: String? = null,
        @SerializedName("invitedByMe") val invitedByMe: Boolean = false,
        @SerializedName("profilePicture") val profilePicture: String? = null,
        @SerializedName("chatId") val chatId: String? = null
)