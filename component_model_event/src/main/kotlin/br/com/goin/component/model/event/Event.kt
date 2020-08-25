package br.com.goin.component.model.event

import br.com.goin.component.model.event.api.response.ApiVoucher
import br.com.goin.base.utils.GpsUtils
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class Event : Serializable{

    enum class EventConfirmationType {
        Interested, CheckedIn
    }

    enum class CategoryType {
        EVENT, THEATER, PLACE, MOVIE
    }

    enum class EventType{
        CONFIRM_EVENT, REGULAR_EVENT
    }

    enum class OriginAction(val type: String) {
        GOIN("GOIN"),
        INGRESSE("INGRESSE"),
        ALOINGRESSOS("ALOINGRESSOS"),
        INGRESSORAPIDO("INGRESSORAPIDO"),
        SITE("SITE"),
        PAGAMENTO("PAGAMENTO"),
        PAGAMENTO_AUTENTICADO("PAGAMENTO_AUTENTICADO"),
        CORPORATIVO("corporation")
    }

    var id: String= ""
    var clubId: String? = ""
    var name: String= ""
    var description: String= ""
    var club: String? = ""
    var categoryName: String? = null
    var image: String? = ""
    var information: EventInformation? = null
    var placeName: String? = ""
    var placeAddress: String? = ""
    var startDate: Calendar? = null
    var city: String? = ""
    var originHasDiscount: Boolean = false
    var categoryType: CategoryType? = null
    var endDate: Calendar? = null
    var intentionCount: Int? = null
    var checkInCount: Int? = null
     var categoryEventType: String? = null
    var isConfirmed: Boolean = false
    var photoUrl: String? = null
    var subcategories: Array<String> = arrayOf()
    var lat: Double? = null
    var lng: Double? = null
    var confirmType: EventConfirmationType? = null
    var originType: String? = ""
    var originAction: String? = ""
    var originURL: String? = ""
    var lowestPrice: Int? = null
    var highestPrice: Int? = null
    var originId: String? = ""
    var imageWidth: String? = null
    var imageHeight: String? = null
    var imageAspect: String? = null
    var buttonColor: String? = null
    var bigIcon: String? = null
    var smallIconColored: String? = null
    var smallIconWhite: String? = null
    var categoryId: String? = null
    var partnersInfo : PartnerInfo? = null

    class PartnerInfo{
        var mainColor: String? = ""
        var textColor: String? = ""
        var logo: String? = ""
        var buttonColor: String? = ""
        var bigIcon: String? = ""
        var smallIconColored: String? = ""
        var smallIconWhite: String? = ""
    }

    var distance: Float? = null

    var vouchers: List<ApiVoucher>? = null
    fun isCheckedIn(): Boolean = "CheckedIn" == confirmType?.name ?: false

    fun hasEventStarted(): Boolean {
        val calendar = Calendar.getInstance()
        return startDate != null && endDate != null && startDate!!.before(calendar) && endDate!!.after(calendar)
    }

    fun isCorporative() = "corporation" == originType

    fun getDistance( location : Pair<Double, Double>?): String{
        if(distance == null || distance == 0F) distance = GpsUtils.calculate(location?.first, location?.second, lat, lng)
        if(distance == 0F ) return ""
        return GpsUtils.formatt(distance)
    }
}
