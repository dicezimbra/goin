package br.com.goin.component.model.event.api.response

import com.google.gson.annotations.SerializedName


data class EventAttendanceResponse(@SerializedName("eventId") var eventId: String? = null,
                                   @SerializedName("userName") var userName: String = "",
                                   @SerializedName("userId") var userId: String = "",
                                   @SerializedName("type") var type: String = "",
                                   @SerializedName("placeName") var placeName: String = "",
                                   @SerializedName("image") var image: String = "",
                                   @SerializedName("title") var title: String = "",
                                   @SerializedName("subtitle") var subtitle: String = "",
                                   @SerializedName("expiration") var expiration: String = "",
                                   @SerializedName("dateText") var dateText: String = "",
                                   @SerializedName("regulation") var regulation: String = "",
                                   @SerializedName("receiptText") var receiptText: String = "",
                                   @SerializedName("validate") var validate: String = "",
                                   @SerializedName("detail") var detail: List<AttendanceDetail> = arrayListOf())

