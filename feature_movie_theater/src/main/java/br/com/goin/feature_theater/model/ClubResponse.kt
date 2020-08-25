package br.com.goin.feature_theater.model

import br.com.goin.component_model_club.model.Club
import com.google.gson.annotations.SerializedName

class ClubResponse(@SerializedName("club") val club: Club) {
}