package br.com.goin.component.model.event.api.response

import java.io.Serializable

data class OriginInfos(var buttonColor: String? = null,
                       var bigIcon: String? = null,
                       var smallIconColored: String? = null,
                       var smallIconWhite: String? = null) : Serializable {

}
