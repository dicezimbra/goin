package br.com.goin.feature.search.location.searchLocation.model

import com.google.gson.annotations.SerializedName

data class CityResponse(@SerializedName("estado") val state: String,
                        @SerializedName("nome_municipio") val city: String,
                        @SerializedName("uf") val uf: String,
                        @SerializedName("latitude") val lat: Double,
                        @SerializedName("longitude") val lng: Double)