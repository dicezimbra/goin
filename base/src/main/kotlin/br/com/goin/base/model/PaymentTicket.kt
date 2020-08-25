package br.com.goin.base.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class PaymentTicket  : Serializable {
    @SerializedName("ticketId")
    @Expose
    public var ticketId: String? = null
    @SerializedName("quantity")
    @Expose
    public var quantity: Int? = null
    @SerializedName("info")
    @Expose
    public var info: PaymentInfo = PaymentInfo()
}