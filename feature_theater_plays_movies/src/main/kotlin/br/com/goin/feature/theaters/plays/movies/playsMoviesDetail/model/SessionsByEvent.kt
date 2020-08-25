package br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SessionsByEvent(@SerializedName("sessionsByEvent")var sessionsByEvent: List<EventSessions>? = null): Serializable

data class EventSessions(@SerializedName("date") var date: String? = null,
                         @SerializedName("week") var week: String? = null,
                         @SerializedName("sessions") var sessions: List<EventSession>? = null): Serializable

data class EventSession(@SerializedName("id") var id: String? = null,
                        @SerializedName("eventId") var eventId: String? = null,
                        @SerializedName("clubName") var clubName: String = "",
                        @SerializedName("addressInfo") var addressInfo: SessionAddressInfo? = null,
                        @SerializedName("details") var details: List<EventSessionDetail>? = null): Serializable

data class SessionAddressInfo(@SerializedName("latitude") var lat: Double?,
                        @SerializedName("longitude") var lng: Double?,
                        @SerializedName("placeAddress") var address: String = ""): Serializable

data class EventSessionDetail(@SerializedName("room") var room: String? = null,
                              @SerializedName("labels") var labels: List<String>? = null,
                              @SerializedName("info") var eventSessionDetailInfo: List<EventSessionDetailInfo>? = null): Serializable

data class EventSessionDetailInfo(@SerializedName("isAvailable") var isAvailable: Boolean = false,
                                  @SerializedName("hour") var hour: String? = null,
                                  @SerializedName("originURL") var originURL: String? = null): Serializable