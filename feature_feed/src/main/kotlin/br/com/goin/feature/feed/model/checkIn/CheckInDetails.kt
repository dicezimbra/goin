package br.com.goin.feature.feed.model.checkIn

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*


data class CheckInDetails(@SerializedName("id")var id: String? = null,
                          @SerializedName("name")var name: String? = null,
                          @SerializedName("type")var type: String? = null) : Serializable


