package br.com.goin.feature.search.event.model.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SearchByNameResponse(@SerializedName("getEventByName") val getEventByName: MutableList<SearchEvent>?) : Serializable
