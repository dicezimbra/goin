package br.com.goin.component_model_club

import android.location.Location
import br.com.goin.component.model.event.api.EventMapper
import br.com.goin.component.session.user.location.UserLocationInteractor
import br.com.goin.component_model_club.model.*

class ClubMapper {

    private val userLocationInteractor = UserLocationInteractor.instance

    fun mapToModel(club: Club): ClubModel {
        val model = ClubModel()
        model.clubId = club.id!!
        model.clubLogoUrl = club.logoImage
        model.subcategory = mapCategory(club.subcategories)
        model.followers = if (club.followersCount == null) 0 else club.followersCount
        model.hasLogo = club.logoImage != null
        model.clubName = club.name
        model.clubSite = if (club.website == null) "" else club.website
        model.rating = club.rating
        model.ratingCount = if (club.ratingCount == null) 0 else club.ratingCount
        model.isFollowing = if (club.followedByMe == null) false else club.followedByMe!!
        model.giftsGallery = club.giftsGallery
        model.priceRating = club.priceRating
        model.tags = club.tags

        if (club.events != null)
            if (!club.events!!.isEmpty())
                model.events = EventMapper().mapToModel(club?.events)
        model.clubDescription = club.description
        model.clubShortDescription = club.description?.take(200)
        model.coverImage = club.coverImage
        model.address = club.address

        model.galleryUrls = ArrayList()
        club.photoGallery?.let {
            model.galleryUrls!!.addAll(it)
        }

        if (club.latitude != null && club.longitude != null) {
            model.latitude = club.latitude
            model.longitude = club.longitude

            val results = FloatArray(3)
            if (userLocationInteractor.fetch() != null) {
                Location.distanceBetween(userLocationInteractor.fetch().lat,
                        userLocationInteractor.fetch().lng, club.latitude!!.toDouble(),
                        club.longitude!!.toDouble(), results)
            }
            model.distance = results[0]
        } else {
            model.distance = null
        }

        return model
    }

    fun mapToModel(response: SearchFilterResponse?): SearchFilterModel? {
        return if (response == null) null else SearchFilterModel(response.searchFilter!!.currentPage,
                response!!.searchFilter!!.totalPages!!, response.searchFilter!!.totalItems!!,
                EventMapper().mapToModel(response.searchFilter!!.events), mapDtoListToModelList(response?.searchFilter?.clubs))
    }

    fun mapDtoListToModelList(clubs: List<Club>?): List<ClubModel> {
        val clubsModels = java.util.ArrayList<ClubModel>()
        if (clubs == null)
            return clubsModels
        for (c in clubs) {
            clubsModels.add(mapToModel(c))
        }
        return clubsModels
    }

    fun mapToModel(clubRating: ClubRating): ClubRatingModel? {
        return ClubRatingModel(ratingsList = mapToModel(clubRating.ratingsList), globalRating = clubRating.globalRating, globalRatingCount = clubRating.globalRatingCount, ratedByMe =  clubRating.ratedByMe)
    }

    private fun mapToModel(ratingsList: List<ClubRatingCard>?): List<ClubRatingCardModel>? {

        val ratingListModel = java.util.ArrayList<ClubRatingCardModel>()
        if (ratingsList == null)
            return ratingListModel
        for (c in ratingsList) {
            ratingListModel.add(mapToModel(c))
        }
        return ratingListModel
    }

    fun mapToModel(rating: ClubRatingCard?): ClubRatingCardModel {
        return ClubRatingCardModel(
                userName = rating?.userName,
                comment = rating?.comment,
                avatar = rating?.avatar,
                rating = rating?.rating,
                ratedByMe = rating?.ratedByMe,
                userId = rating?.userId)
    }

    private fun mapCategory(categories: List<SubcategoriesInfo>): String {
        var fullCategories = ""
        categories?.let {
            it.forEach { element ->
                fullCategories+= " / ${element.name}"
            }
        }

        return fullCategories
    }
}