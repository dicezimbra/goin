package br.com.goin.component_model_club

import br.com.goin.component_model_club.model.*
import io.reactivex.Completable
import io.reactivex.Observable

private const val CLUB_TYPE = "CLUB"

class ClubInteractorImpl : ClubInteractor {
    override fun userRating(rating: Float, text: String?, clubId: String, id: String?): Observable<ClubRatingCardModel> {
        return repository.userRating(rating, text, clubId, id).map { it.data?.let { it1 -> mapper.mapToModel(it1.rateClub) } }
    }

    private val repository = ClubRepository()
    private val mapper = ClubMapper()

    override fun get(clubId: String): Observable<ClubModel> {
        return repository.getClub(clubId).map { mapper.mapToModel(it.data?.club!!) }
    }

    override fun follow(clubId: String): Observable<ClubModel> {
        return repository.followClub(clubId).map { mapper.mapToModel(it.data?.club!!) }
    }

    override fun unfollow(clubId: String): Observable<ClubModel> {
        return repository.unfollowClub(clubId).map { mapper.mapToModel(it.data?.club!!) }
    }

    override fun filterResults(searchFilterInput: SearchFilterInput, paginate: Int,
                               limit: Int, firstRequest: Boolean): Observable<SearchFilterModel> {
        return repository.filterResults(searchFilterInput, paginate, limit).map { mapper.mapToModel(it.data) }
    }

    override fun putInterest(id: String,
                             name: String,
                             image: String): Completable {
        return repository.putInterest(id, CLUB_TYPE, name, image)
    }

    override fun removeInterest(id: String): Completable {
        return repository.removeInterest(id)
    }

    override fun getClubRatings(clubId: String): Observable<ClubRatingModel> {
        return repository.getClubRatings(clubId).map { it.data?.ratings?.let { it1 -> mapper.mapToModel(it1) } }
    }
}