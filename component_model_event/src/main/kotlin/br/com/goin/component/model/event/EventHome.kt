package br.com.goin.component.model.event

import br.com.goin.base.utils.GpsUtils
import java.util.*

class EventHome {

    var actionValue: String? = ""
    var image: String? = ""
    var action: String? = ""
    var lat: Double? = null
    var long: Double? = null
    var title: String? = ""
    var subtitle: String? = ""
    var date: Calendar? = null
    var imageWidth: Int? = null
    var imageHeight: Int? = null
    var endDate: Calendar? = null
    var originType: String? = ""
    var partnersInfo: PartnerInfo? = null
    var distance : Float? = null

    class PartnerInfo {
        var mainColor: String? = ""
        var textColor: String? = ""
        var logo: String? = ""
        var buttonColor: String? = ""
        var bigIcon: String? = ""
        var smallIconColored: String? = ""
        var smallIconWhite: String? = ""
    }


    fun getDistance( location : Pair<Double, Double> ?): String{
        if(distance == null || distance == 0F) distance = GpsUtils.calculate(location?.first, location?.second, lat, long)
        if(distance == 0F ) return ""
        return GpsUtils.formatt(distance)
    }

}