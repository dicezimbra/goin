package br.com.goin.feature.search.event.model.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SearchResponse(@SerializedName("searchByExpression") val searchByExpression: MutableList<SearchEvent>) : Serializable
