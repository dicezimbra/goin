package br.com.goin.feature_theater.model

import com.google.gson.annotations.SerializedName

data class MovieTheaterEvent(@SerializedName("id")val id: String?,
                             @SerializedName("name")val name: String?,
                             @SerializedName("description")val description: String?,
                             @SerializedName("image")val image: String,
                             @SerializedName("sessions")val sessions: Sessions)