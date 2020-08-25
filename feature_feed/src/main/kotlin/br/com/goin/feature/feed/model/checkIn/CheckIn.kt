package br.com.goin.feature.feed.model.checkIn

import java.io.Serializable

data class CheckIn(var name: String? = null,
                   var club: String? = null,
                   var clubId: String? = null,
                   var id: String? = null) : Serializable {

}