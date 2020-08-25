package br.com.goin.base.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class PaymentInfo  : Serializable {
    @SerializedName("name")
    @Expose
    public var name: String? = null
    @SerializedName("price")
    @Expose
    public var price: Float? = null
    @SerializedName("description")
    @Expose
    public var description: String? = null
    @SerializedName("ticketType")
    @Expose
    public var ticketType: String? = null
    @SerializedName("isHalfPrice")
    @Expose
    public var isHalfPrice: Boolean? = null
    @SerializedName("remaining")
    @Expose
    public var remaining: Any? = null
    @SerializedName("eventDate")
    @Expose
    public var eventDate: Long? = null
    @SerializedName("eventName")
    @Expose
    public var eventName: String? = null
    @SerializedName("eventAddress")
    @Expose
    public var eventAddress: String? = null
    @SerializedName("clubName")
    @Expose
    public var clubName: String? = null
    @SerializedName("available")
    @Expose
    public var available: Boolean? = null
    @SerializedName("capacity")
    @Expose
    public var capacity: Int? = null
}