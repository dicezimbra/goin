package br.com.goin.component.model.event

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Banner(@SerializedName("id") val id: String,
             @SerializedName("url")val url: String,
             @SerializedName("typeBanner")val typeBanner: String,
             @SerializedName("eventId")val eventId: String?,
             @SerializedName("clubId")val clubId: String,
             @SerializedName("actionValue")val actionValue: String?,
             @SerializedName("action")val action: String?) : Serializable {
    companion object {
        val TYPE_BANNER = "BANNER"
        val TYPE_HIGHLIGHT = "HIGHLIGHT"
    }
}