package br.com.goin.component_model_club.model

class ClubRating  {
    var ratingsList: List<ClubRatingCard>? = null
    var globalRating: Float = 0.toFloat()
    var globalRatingCount: Int? = null
    var ratedByMe: Boolean = false
}
