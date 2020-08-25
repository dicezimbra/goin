package br.com.goin.component.model.uber.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UberPricesResponse<T>:Serializable {
    @SerializedName("status") var status: String? = null
    @SerializedName("prices") var prices: Collection<T>? = null
}
