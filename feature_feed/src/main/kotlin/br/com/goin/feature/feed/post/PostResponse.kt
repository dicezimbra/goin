package br.com.goin.feature.feed.post

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class CreatePostResponse(@SerializedName("createPost")val createPost: CreatePost) : Serializable

