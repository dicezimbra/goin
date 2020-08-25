package br.com.goin.friends.model

import android.content.Context
import br.com.goin.component.session.user.UserModel
import br.com.goin.friends.R
import com.google.gson.annotations.SerializedName

data class FriendToInvite(@SerializedName("name")val name: String?,
                          @SerializedName("avatar")val avatar: String?,
                          @SerializedName("userId")val userId: String,
                          @SerializedName("invitedByMe")var invitedByMe: Boolean?)
