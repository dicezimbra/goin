package br.com.goin.component_model_club

import br.com.goin.base.BuildConfig
import br.com.goin.base.GraphqlQuery
import br.com.goin.component.model.event.api.RemoveInterest
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import br.com.goin.component_model_club.model.*
import io.reactivex.Completable
import io.reactivex.Observable

class ClubRepository {

    private val service = RetrofitService(ClubApi::class.java, BuildConfig.BASE_URL)

    fun removeInterest(id: String): Completable {
        return service.apiService.interest(RemoveInterest(id))
    }

    fun putInterest(id: String,
                    type: String,
                    name: String,
                    image: String): Completable {
        val body = RequestInterestBody(id, type, name, image)
        return service.apiService.interest(body)
    }

    fun getClub(clubId: String): Observable<GraphQLResponse<Club>> {
        val graphql = GraphqlBody.Builder(ClubQueries.GET_CLUB)
                .`var`("clubId", clubId)
                .`var`("eventCount", null)
                .build()
        return service.apiService.getClub(graphql)
    }

    fun followClub(clubId: String): Observable<GraphQLResponse<Club>> {

        val graphql = GraphqlQuery.builder(ClubQueries.FOLLOW_CLUB)
                .`var`("clubId", clubId)
                .build()
        return service.apiService.followClub(graphql)
    }

    fun unfollowClub(clubId: String): Observable<GraphQLResponse<Club>> {
        val graphql = GraphqlQuery.builder(ClubQueries.UNFOLLOW_CLUB)
                .`var`("clubId", clubId)
                .build()
        return service.apiService.followClub(graphql)
    }

    fun filterResults(searchFilterInput: SearchFilterInput, paginate: Int?, limit: Int?)
            : Observable<GraphQLResponse<SearchFilterResponse>> {

        val graphqlQuery = GraphqlQuery.builder(ClubQueries.FILTER_RESULTS)
                .`var`("input", searchFilterInput)
                .`var`("limit", limit)
                .`var`("paginate", paginate)
                .build()

        return service.apiService.filterResults(graphqlQuery)
    }

    fun getClubRatings(clubId: String): Observable<GraphQLResponse<Rating>> {

        val graphql = GraphqlQuery.builder(ClubQueries.GET_CLUB_RATINGS)
                .`var`("clubId", clubId)
                .build()
        return service.apiService.getClubRating(graphql)
    }

    fun userRating(rating: Float, text: String?, clubId: String, id: String?): Observable<GraphQLResponse<UserRating>> {

        val graphqlQuery = GraphqlQuery.builder(ClubQueries.RATE_CLUB)
                .`var`("clubId", clubId)
                .`var`("rating", rating)
                .`var`("comment", text)
                .build()

        return service.apiService.userRating(graphqlQuery)
    }
}