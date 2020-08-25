package br.com.goin.component.session.user

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserCardDetails(@SerializedName("id")
                           var id: String? = null,
                           @SerializedName("followerName")
                           var followerName: String? = null,
                           @SerializedName("followerAvatar")
                           var followerAvatar: String? = null,
                           @SerializedName("userName")
                           var userName: String? = null,
                           @SerializedName("userAvatar")
                           var userAvatar: String? = null,
                           @SerializedName("followedByMe")
                           var followedByMe: Boolean? = null,
                           @SerializedName("userId")
                           var userId: String? = null,
                           @SerializedName("name")
                           var name: String? = null,
                           @SerializedName("avatar")
                           var avatar: String? = null,
                           @SerializedName("invitedByMe")
                           var invitedByMe: Boolean = false,
                           @SerializedName("profilePicture")
                           var profilePicture: String? = null,
                           @SerializedName("chatId")
                           var chatId: String? = null) : Serializable