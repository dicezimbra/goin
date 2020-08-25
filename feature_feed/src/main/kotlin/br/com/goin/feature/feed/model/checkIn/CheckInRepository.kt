package br.com.goin.feature.feed.model.checkIn

import br.com.goin.base.BuildConfig
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import io.reactivex.Observable

class CheckInRepository {

    private val service = RetrofitService(CheckInApi::class.java, BuildConfig.BASE_URL)

    fun getAvailableEventsToCheckIn(myLocation: FloatArray): Observable<GraphQLResponse<CheckInResponse>> {
        val graphqlQuery = GraphqlBody.Builder(CheckInQueries.GET_AVAILABLE_EVENTS_TO_CHECK_IN)
                .`var`("myLocation", myLocation)
                .build()

        return service.apiService.eventsToCheckin(graphqlQuery)
    }
}