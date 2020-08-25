package br.com.goin.base.model

import java.io.Serializable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class PaymentModel : Serializable {
    @SerializedName("originType")
    @Expose
    public  var originType: String? = null
    @SerializedName("eventImage")
    @Expose
    public var eventImage: String? = null
    @SerializedName("eventId")
    @Expose
    public  var eventId: String? = null
    @SerializedName("userToken")
    @Expose
    public  var userToken: String? = null
    @SerializedName("userId")
    @Expose
    public  var userId: String? = null
    @SerializedName("tickets")
    @Expose
    public  var tickets: ArrayList<PaymentTicket> = ArrayList()
    @SerializedName("address")
    @Expose
    public  var address: PaymentAddress = PaymentAddress()
    @SerializedName("creditcard")
    @Expose
    public  var creditcard: PaymentCreditCard = PaymentCreditCard()
    @SerializedName("pushEndpoint")
    @Expose
    public  var pushEndpoint: String? = null
}