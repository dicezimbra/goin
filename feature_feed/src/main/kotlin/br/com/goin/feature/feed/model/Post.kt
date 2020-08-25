package br.com.goin.feature.feed.model

import android.app.Activity
import android.graphics.drawable.Drawable
import br.com.goin.base.BaseApplication
import br.com.goin.feature.feed.R
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Post(@SerializedName("postId") var postId: String = "",
                @SerializedName("posterId") var posterId: String? = null,
                @SerializedName("description") var description: String? = null,
                @SerializedName("location") var location: String? = null,
                @SerializedName("eventName") var eventName: String? = null,
                @SerializedName("clubName") var clubName: String? = null,
                @SerializedName("feeling") var feeling: String? = null,
                @SerializedName("likesCount") var likesCount: Int? = null,
                @SerializedName("commentsCount") var commentsCount: Int? = null,
                @SerializedName("timeStamp") var timeStamp: Long? = null,
                @SerializedName("likedByMe") var likedByMe: Boolean = false,
                @SerializedName("image") var image: String? = null,
                @SerializedName("video") var video: String? = null,
                @SerializedName("avatar") var avatar: String? = null,
                @SerializedName("name") var name: String? = null,
                @SerializedName("comment") var comment: String? = null,
                @SerializedName("commentAuthor") var commentAuthor: String? = null,
                @SerializedName("checkInEventId") var checkInEventId: String? = null,
                @SerializedName("commentId") var commentId: String? = null,
                @SerializedName("emoji") var emoji: String? = null,
                @SerializedName("sharedBy") var sharedBy: SharedBy? = null) : Serializable

