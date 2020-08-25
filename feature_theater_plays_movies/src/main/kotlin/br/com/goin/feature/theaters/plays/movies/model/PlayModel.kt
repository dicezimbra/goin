package br.com.goin.feature.theaters.plays.movies.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PlayModel(@SerializedName("id") var id: String? = null,
                     @SerializedName("name") var name: String? = null,
                     @SerializedName("club") var club: String? = null,
                     @SerializedName("city") var city: String? = null,
                     @SerializedName("image") var image: String? = null) : Serializable