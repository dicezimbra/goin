package br.com.goin.component.model.uber

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UberInformation(@SerializedName("price")val price: Float?,
                           @SerializedName("time")val time: Int?): Serializable