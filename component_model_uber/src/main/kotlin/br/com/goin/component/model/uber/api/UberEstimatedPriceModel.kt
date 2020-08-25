package br.com.goin.component.model.uber.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UberEstimatedPriceModel:Serializable {

    @SerializedName("estimate")var estimate: String? = null
    @SerializedName("low_estimate")var low_estimate: Float = 0.toFloat()
    @SerializedName("distance")var distance: Float = 0.toFloat()
    @SerializedName("product_id")var product_id: String? = null
    @SerializedName("duration")var duration: Int = 0
    @SerializedName("currency_code")var currency_code: String? = null

     val estimatePrice: String
        get() = if (estimate != null) { estimate!! } else ""
}