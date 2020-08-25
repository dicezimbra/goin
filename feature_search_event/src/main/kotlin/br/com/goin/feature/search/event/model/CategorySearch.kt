package br.com.goin.feature.search.event.model

import br.com.goin.feature.search.event.model.api.SearchEvent
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CategorySearch(@SerializedName("category")val category: String,
                          @SerializedName("items") val items: MutableList<SearchEvent>): Serializable