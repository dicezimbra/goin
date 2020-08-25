package br.com.goin.feature_theater.model

import br.com.goin.base.BuildConfig
import br.com.goin.base.GraphqlQuery
import br.com.goin.base.OutsmartResponse
import br.com.goin.base.utils.Constants
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import io.reactivex.Observable

class TheaterRepository {

    private val service = RetrofitService(TheaterApi::class.java, BuildConfig.BASE_URL)

    fun sessionsByClub(clubId: String?): Observable<GraphQLResponse<TheaterResponse>> {
        val graphqlQuery = GraphqlQuery.builder(TheaterQueries.GET_THEATER)
                .`var`("clubId", clubId)
                .build()

        return service.apiService.sessionsByClub(graphqlQuery)
    }

    fun getClub(clubId: String?): Observable<GraphQLResponse<ClubResponse>> {
        val graphqlQuery = GraphqlQuery.builder(TheaterQueries.GET_CLUB)
                .`var`("clubId", clubId)
                .`var`("eventCount", 0)
                .`var`("postCount", 0)
                .build()

        return service.apiService.getClub(graphqlQuery)
    }
}