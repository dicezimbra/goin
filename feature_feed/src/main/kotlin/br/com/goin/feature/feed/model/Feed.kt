package br.com.goin.feature.feed.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Feed(@SerializedName("post") val post: List<Post>? = arrayListOf(),
                @SerializedName("lastPostId") val lastPostId: String? = null,
                @SerializedName("lastMasterPostId") val lastMasterPostId: String? = null,
                @SerializedName("morePostsToGet") val morePostsToGet: Boolean = false): Serializable

