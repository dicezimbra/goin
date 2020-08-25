package br.com.goin.component_model_club

import br.com.goin.component_model_club.model.*
import io.reactivex.Completable
import io.reactivex.Observable

interface ClubInteractor {

    companion object {

        val instance : ClubInteractor = ClubInteractorImpl()
    }

    fun get(clubId: String): Observable<ClubModel>
    fun follow(clubId: String): Observable<ClubModel>
    fun unfollow(clubId: String): Observable<ClubModel>
    fun filterResults(searchFilterInput: SearchFilterInput, paginate: Int, limit: Int, firstRequest: Boolean): Observable<SearchFilterModel>
    fun getClubRatings(clubId: String) : Observable<ClubRatingModel>
    fun userRating(rating: Float, text: String?, clubId: String, id: String?): Observable<ClubRatingCardModel>
    fun putInterest(id: String, name: String, image: String): Completable
    fun removeInterest(id: String): Completable
}