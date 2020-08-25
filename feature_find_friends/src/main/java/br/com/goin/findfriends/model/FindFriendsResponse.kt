package br.com.goin.findfriends.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FindFriendsResponse(@SerializedName("searchUsers") val searchUsers: MutableList<FindFriendsModel>) : Serializable