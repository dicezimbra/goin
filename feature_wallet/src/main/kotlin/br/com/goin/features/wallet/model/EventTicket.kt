package br.com.goin.features.wallet.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EventTicket {

    @SerializedName("id")
    @Expose
    public val id: String? = null
    @SerializedName("name")
    @Expose
    public val name: String? = null
    @SerializedName("price")
    @Expose
    public val price: Float = 0.0F
    @SerializedName("description")
    @Expose
    public val description: String? = null
    @SerializedName("quantity")
    @Expose
    public val quantity: Int? = null
    @SerializedName("ticketType")
    @Expose
    public val ticketType: String? = null
    @SerializedName("isHalfPrice")
    @Expose
    public val isHalfPrice: Boolean? = null
    @SerializedName("remaining")
    @Expose
    public val remaining: Int? = null
    @SerializedName("eventDate")
    @Expose
    public val eventDate: Long? = null
    @SerializedName("eventName")
    @Expose
    public val eventName: String? = null
    @SerializedName("eventAddress")
    @Expose
    public val eventAddress: String? = null
    @SerializedName("clubName")
    @Expose
    public val clubName: String? = null
    @SerializedName("available")
    @Expose
    public val available: Boolean? = null
    @SerializedName("capacity")
    @Expose
    public val capacity: Int? = null


    public var buyQuantity: Int = 0


    public var isPlusActive: Boolean = true
    public var isMinusActive: Boolean = false

}