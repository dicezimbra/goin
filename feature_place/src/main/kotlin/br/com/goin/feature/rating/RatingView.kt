package br.com.goin.feature.rating

import br.com.goin.component.session.user.UserModel
import br.com.goin.component_model_club.model.ClubRatingCardModel
import br.com.goin.component_model_club.model.ClubRatingModel

interface RatingView {
    fun hideLoading()
    fun showLoading()
    fun handleError(t: Throwable?)
    fun loadRatings(clubModel: ClubRatingModel?)
    fun loadUserInfos(it: UserModel?)
    fun ratingSuccess(it: ClubRatingCardModel)
}