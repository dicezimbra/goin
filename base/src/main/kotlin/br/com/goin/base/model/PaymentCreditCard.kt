package br.com.goin.base.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class PaymentCreditCard  : Serializable {
    @SerializedName("brand")
    @Expose
    public var brand: String? = null
    @SerializedName("installments")
    @Expose
    public var installments: Int? = null
    @SerializedName("number")
    @Expose
    public var number: Long? = null
    @SerializedName("holderName")
    @Expose
    public var holderName: String? = null
    @SerializedName("expiracyMonth")
    @Expose
    public var expiracyMonth: Int? = null
    @SerializedName("expiracyYear")
    @Expose
    public var expiracyYear: Int? = null
    @SerializedName("cvv")
    @Expose
    public var cvv: Long? = null
    @SerializedName("birthday")
    @Expose
    public var birthday: String? = null
    @SerializedName("cpf")
    @Expose
    public var cpf: String? = null

}