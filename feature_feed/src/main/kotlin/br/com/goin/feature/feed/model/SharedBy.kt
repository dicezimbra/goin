package br.com.goin.feature.feed.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SharedBy(@SerializedName("id") val id: String? = null,
                    @SerializedName("name") val name: String? = null,
                    @SerializedName("avatar") val avatar: String? = null) : Serializable
