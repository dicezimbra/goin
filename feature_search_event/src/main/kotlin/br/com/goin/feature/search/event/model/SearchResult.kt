package br.com.goin.feature.search.event.model

import br.com.goin.feature.search.event.model.api.SearchEvent
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SearchResult(val searchEvent: MutableList<SearchEvent>,val screenName: Int): Serializable