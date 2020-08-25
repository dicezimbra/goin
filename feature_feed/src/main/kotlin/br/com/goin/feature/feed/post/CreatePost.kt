package br.com.goin.feature.feed.post

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class CreatePost(
        @SerializedName("description")var description: String? = null,
        @SerializedName("feeling")var feeling: String? = null,
        @SerializedName("image")var image: Boolean? = false,
        @SerializedName("video")var video: Boolean? = false,
        @SerializedName("timeStamp")var timeStamp: Long? = null,
        @SerializedName("location")var location: String? = null,
        @SerializedName("postId")var postId: String? = null,
        @SerializedName("avatar")var avatar: String? = null,
        @SerializedName("eventName")var eventName: String? = null,
        @SerializedName("posterId")var posterId: String? = null,
        @SerializedName("name")var name: String? = null,
        @SerializedName("checkInEventId")var checkInEventId: String? = null) : Serializable

