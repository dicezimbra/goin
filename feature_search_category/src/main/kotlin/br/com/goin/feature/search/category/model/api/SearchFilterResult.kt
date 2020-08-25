package br.com.goin.feature.search.category.model.api

import java.io.Serializable

import br.com.goin.component.model.event.api.response.ApiEvent
import br.com.goin.component_model_club.model.Club
import com.google.gson.annotations.SerializedName

data class SearchFilterResult(@SerializedName("currentPage") var currentPage: Int = 0,
                              @SerializedName("totalPages")var totalPages: Int? = null,
                              @SerializedName("totalItems")var totalItems: Int? = null,
                              @SerializedName("events")var events: List<ApiEvent>? = null,
                              @SerializedName("clubs")var clubs: List<Club>? = null): Serializable
