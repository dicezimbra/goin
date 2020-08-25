package br.com.goin.findfriends.model

import com.google.gson.annotations.SerializedName

data class FindFriendsModel(@SerializedName("name") val name: String,
                            @SerializedName("id") val id: String,
                            @SerializedName("followedByMe") val followedByMe: Boolean,
                            @SerializedName("profilePicture") val profilePicture: String)