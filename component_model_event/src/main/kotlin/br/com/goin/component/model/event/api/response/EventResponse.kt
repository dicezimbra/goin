package br.com.goin.component.model.event.api.response

import br.com.goin.component.model.event.Banner
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class EventResponse : Serializable {

    @SerializedName("events")
    var events: List<ApiEvent> = arrayListOf()
    @SerializedName("allEvents")
    var allEvents: List<ApiEvent> = arrayListOf()
    @SerializedName("myEvents")
    var myEvents: List<ApiEvent> = arrayListOf()
    @SerializedName("banners")
    var banners: List<Banner> = arrayListOf()
    @SerializedName("getEvent")
    var getEvent: ApiEvent? = null
    @SerializedName("confirmTokenIsValid")
    var confirmTokenIsValid: TokenValid? = null
    @SerializedName("checkInAvailable")
    var checkInAvailable: List<ApiEvent> = arrayListOf()
    @SerializedName("nameOnList")
    var nameOnList: EventAttendanceResponse? = null

}