package br.com.goin.component.model.event.api.response

import br.com.goin.component.model.event.Event
import br.com.goin.component.model.event.api.CategoriesInfo
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ApiVoucher(@SerializedName("giftId") var giftId: String? = null,
                        @SerializedName("title") var title: String? = null,
                      @SerializedName("subtitle") var subtitle: String? = null,
                      @SerializedName("dateText") var dateText: String? = null,
                      @SerializedName("image") var image: String? = null,
                      @SerializedName("detailTitle") var detailTitle: String? = null,
                      @SerializedName("rules") var regulation: String? = null,
                      @SerializedName("placeName") var placeName: String? = null) : Serializable

