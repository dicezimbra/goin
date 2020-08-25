package br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.session

import br.com.goin.component.model.uber.UberInformation
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.model.EventSessionDetail
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Session(var uberInformation: UberInformation? = null,
                   var lat: Double? = null,
                   var lng: Double? = null,
                   var clubName: String = "",
                   var address: String = "",
                   var date: String? = null,
                   var week: String? = null,
                   var details: List<EventSessionDetail>? = null) : Serializable
