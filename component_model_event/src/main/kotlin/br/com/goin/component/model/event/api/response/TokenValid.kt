package br.com.goin.component.model.event.api.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class TokenValid : Serializable {
    @SerializedName("isValid")var isValid: Boolean = false

}
