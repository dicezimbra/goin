package br.com.goin.feature.search.category.model.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SearchFilterResponse(@SerializedName("searchFilter")var searchFilter: SearchFilterResult) : Serializable
