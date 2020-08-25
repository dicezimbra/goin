package br.com.goin.feature.search.event.model.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SearchEvent(@SerializedName("id") val id: String,
                       @SerializedName("name") val name: String,
                       @SerializedName("categorization") val categorization: String,
                       @SerializedName("description") val description: String?,
                       @SerializedName("logoImage") val logoImage: String?,
                       @SerializedName("city") val city: String,
                       @SerializedName("state") val state: String,
                       @SerializedName("rating") val rating: Float,
                       @SerializedName("placeName") val placeName: String?,
                       @SerializedName("startDate") val startDate: Long?,
                       @SerializedName("latitude") val latitude: Double?,
                       @SerializedName("longitude") val longitude: Double?,
                       @SerializedName("categoryType") val categoryType: String) : Serializable