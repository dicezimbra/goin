package br.com.goin.feature.theaters.plays.movies.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PlaysListModel:Serializable{
    @SerializedName("highlighted")var highlighted: List<PlayModel>? = null
    @SerializedName("inTheaters")var inTheaters: List<PlayModel> = arrayListOf()
    @SerializedName("comingSoon")var comingSoon: List<PlayModel> = arrayListOf()
}
