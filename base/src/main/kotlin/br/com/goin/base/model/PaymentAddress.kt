package br.com.goin.base.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class PaymentAddress  : Serializable {
    @SerializedName("street")
    @Expose
    public var street: String? = null
    @SerializedName("number")
    @Expose
    public var number: String? = null
    @SerializedName("district")
    @Expose
    public var district: String? = null
    @SerializedName("zipcode")
    @Expose
    public var zipcode: String? = null
    @SerializedName("city")
    @Expose
    public var city: String? = null
    @SerializedName("state")
    @Expose
    public var state: String? = null
    @SerializedName("complement")
    @Expose
    public var complement: String? = null

    @SerializedName("label")
    @Expose
    public var label: String? = null
}