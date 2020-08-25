package br.com.goin.feature.rating


interface RatingPresenter {
    fun loadRatings()
    fun onReceiveClub(clubId: String?)
    fun onDestroy()
    fun loadUserInfos()
    fun userRating(rating: Float, text: String?)
}