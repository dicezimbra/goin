package br.com.goin.feature.feed.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class FeedResponse(@SerializedName("feed") val feed: Feed,
                        @SerializedName("posts") val posts: List<Post>? = arrayListOf()): Serializable

