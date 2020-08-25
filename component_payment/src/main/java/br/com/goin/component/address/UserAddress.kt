package br.com.goin.component.address

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserAddress : Serializable {

    @SerializedName("zipcode")
    var zipcode : String? = null

    @SerializedName("alias")
    var alias : String? = null

    @SerializedName("street")
    var street: String? = null

    @SerializedName("number")
    var number: String? = null

    @SerializedName("complement")
    var complement: String? = null

    @SerializedName("district")
    var district: String? = null

    @SerializedName("city")
    var city: String? = null

    @SerializedName("state")
    var state: String? = null

}