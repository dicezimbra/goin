package br.com.goin.features.wallet.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable



enum class ACTION {
    EVENT, CLUB
}

data class TicketDetail(@SerializedName("action") val action: ACTION? = null,
                        @SerializedName("actionValue") val actionValue: String? = null): Serializable