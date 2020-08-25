package br.com.goin.component.model.event.api.response

import com.google.gson.annotations.SerializedName

class ValidateCouponModel {

    @SerializedName("message")
    var message: String? = null
    @SerializedName("valid")
    var valid: Boolean = false
}