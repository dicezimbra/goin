package br.com.goin.component_model_club.model

import java.io.Serializable

import br.com.goin.component.model.event.api.response.ApiEvent

data class SearchFilter(var currentPage: Int = 0,
                        var totalPages: String? = null,
                        var totalItems: String? = null,
                        var events: List<ApiEvent>? = null,
                        var clubs: List<Club>? = null) : Serializable
