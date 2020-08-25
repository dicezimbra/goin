package br.com.goin.feature_theater.model

import com.google.gson.annotations.SerializedName

data class ClubSessions(@SerializedName("date")val date: String?,
                        @SerializedName("week")val week: String?,
                        @SerializedName("events")val events: List<MovieTheaterEvent>?)