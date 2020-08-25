package br.com.goin.feature.theaters.plays.movies.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PlaysListDTO: Serializable {
    @SerializedName("getPlaysTheater") var getAllPlays: PlaysLists? = null
    @SerializedName("getPlaysMovie") var getPlaysMovie: PlaysLists? = null

    inner class PlaysLists: Serializable {
        @SerializedName("highlighted") var highlighted: List<PlaysOutputSimpleView>? = null
        @SerializedName("inTheathers") var inTheathers: List<PlaysOutputSimpleView>? = null
        @SerializedName("comingSoon") var comingSoon: List<PlaysOutputSimpleView>? = null
    }

    inner class PlaysOutputSimpleView: Serializable {
        @SerializedName("id")var id: String? = null
        @SerializedName("image")var image: String? = null
        @SerializedName("name")var name: String? = null
        @SerializedName("club") var club: String? = null
        @SerializedName("city") var city: String? = null
    }
}