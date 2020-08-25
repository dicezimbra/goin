package br.com.goin.features.wallet.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

enum class Validate {
    EXPIRED, USED, VALID
}

data class Ticket(@SerializedName("ticketId") val ticketId: String? = null,
                @SerializedName("userId") val userId: String? = null,
                  @SerializedName("type") val type: String? = null,
                  @SerializedName("title") val title: String? = null,
                  @SerializedName("subtitle") val subtitle: String? = null,
                  @SerializedName("dateText") val dateText: String? = null,
                  @SerializedName("receiptText") val receiptText: String? = null,
                  @SerializedName("image") val image: String? = null,
                  @SerializedName("validate") val validate: Validate? = null,
                  @SerializedName("regulation") val regulation: String? = null,
                  @SerializedName("voucherNumber") val voucherNumber: String? = null,
                  @SerializedName("detailTitle") val detailTitle: String? = null,
                  @SerializedName("placeName") val placeName: String? = null,
                  @SerializedName("detail") val detail: TicketDetail?): Serializable