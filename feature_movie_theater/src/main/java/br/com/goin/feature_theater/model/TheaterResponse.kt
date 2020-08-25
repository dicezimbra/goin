package br.com.goin.feature_theater.model

import com.google.gson.annotations.SerializedName

class TheaterResponse(@SerializedName("sessionsByClub") val sessionsByClub: List<ClubSessions>)