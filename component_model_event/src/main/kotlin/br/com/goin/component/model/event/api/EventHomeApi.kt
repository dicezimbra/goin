package br.com.goin.component.model.event.api

import java.io.Serializable

class EventHomeApi : Serializable {

    var actionValue: String? = ""
    var image: String? = ""
    var action: String? = ""
    var lat: Double? = null
    var long: Double? = null
    var title: String? = ""
    var subtitle: String? = ""
    var date: Long? = null
    var imageWidth: Int? = null
    var imageHeight: Int? = null
    var endDate: Long? = null
    var originType: String? = ""
    var partnerInfo: PartnerInfo? = null

    class PartnerInfo {
        var mainColor: String? = ""
        var textColor: String? = ""
        var logo: String? = ""
        var buttonColor: String? = ""
        var bigIcon: String? = ""
        var smallIconColored: String? = ""
        var smallIconWhite: String? = ""
    }

}
