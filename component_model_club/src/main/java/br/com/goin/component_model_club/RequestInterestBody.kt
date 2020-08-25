package br.com.goin.component_model_club

import com.google.gson.annotations.SerializedName

data class RequestInterestBody(@SerializedName("id") val id: String,
                               @SerializedName("type") val type: String,
                               @SerializedName("name") val name: String,
                               @SerializedName("image") val image: String)
