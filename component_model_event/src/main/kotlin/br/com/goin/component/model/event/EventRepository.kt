package br.com.goin.component.model.event

import br.com.goin.base.BuildConfig
import br.com.goin.component.model.event.api.*
import br.com.goin.component.model.event.api.response.EventResponse
import br.com.goin.component.model.event.api.response.StateResponse
import br.com.goin.component.model.event.api.response.ValidateCouponModel
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.ResponseBody

class EventRepository {

    private val service = RetrofitService(EventApi::class.java, BuildConfig.BASE_URL)
    private val serviceHome = RetrofitService(EventApi::class.java, br.com.goin.component.model.event.BuildConfig.BASE_HOME_URL)

    fun getInterests(userId: String): Observable<List<Interrest>> {
        return service.apiService.getInterest(userId)
    }

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

    fun claimVoucher(ticketId: String?, clubId: String, userName: String): Completable {
        val graphqlQuery = GraphqlBody.Builder(EventQueries.CLAIM_VOUCHER)
                .`var`("ticketId", ticketId)
                .`var`("clubId", clubId)
                .`var`("username", userName)
                .build()

        return service.apiService.claimVoucher(graphqlQuery)
    }

    fun getMyEvents(userId: String?): Observable<GraphQLResponse<EventResponse>> {
        val graphqlQuery = GraphqlBody.Builder(EventQueries.GET_MY_EVENTS)
                .`var`("userId", userId)
                .build()

        return service.apiService.getEventsShort(graphqlQuery)
    }

    fun validateDiscountcode(eventId: String, coupon: String): Observable<ValidateCouponModel> {
        val graphqlQuery = GraphqlBody.Builder(EventQueries.VALIDATE_COUPOM)
                .`var`("eventId", eventId)
                .`var`("coupon", coupon)
                .build()

        return service.apiService.validateCoupom(graphqlQuery).map { it.data?.validateCoupon }
    }

    fun confirmTokenValid(originType: String): Observable<GraphQLResponse<EventResponse>> {
        val graphqlQuery = GraphqlBody.Builder(EventQueries.CONFIRM_TOKEN_IS_VALID)
                .`var`("originType", originType)
                .build()

        return service.apiService.confirmTokenIsValid(graphqlQuery)
    }

    fun getBanners(): Observable<GraphQLResponse<EventResponse>> {
        val graphqlQuery = GraphqlBody.Builder(EventQueries.GET_BANNER_HOME)
                .build()

        return service.apiService.getBanner(graphqlQuery)
    }

    fun getDetail(queryId: String?): Observable<GraphQLResponse<EventResponse>> {
        val graphqlQuery = GraphqlBody.Builder(EventQueries.GET_EVENT)
                .`var`("id", queryId)
                .build()

        return service.apiService.getDetail(graphqlQuery)
    }

    fun confirmEventPresence(eventId: String): Observable<GraphQLResponse<EventResponse>> {
        val graphqlQuery = GraphqlBody.Builder(EventQueries.CONFIRM_PRESENCE)
                .`var`("eventId", eventId)
                .build()

        return service.apiService.confirmPresence(graphqlQuery)
    }

    fun unconfirmEventPresence(eventId: String): Observable<GraphQLResponse<EventResponse>> {
        val graphqlQuery = GraphqlBody.Builder(EventQueries.UNCONFIRM_PRESENCE)
                .`var`("eventId", eventId)
                .build()

        return service.apiService.unconfirmPresence(graphqlQuery)
    }

    fun confirmStefaniniTicket(eventId: String): Completable {
        val graphqlQuery = GraphqlBody.Builder(EventQueries.CONFIRM_STEFANINI_TICKET)
                .`var`("eventId", eventId)
                .build()

        return service.apiService.confirmStefanini(graphqlQuery)
    }

    fun getAvailableEventsToCheckIn(myLocation: FloatArray): Observable<GraphQLResponse<EventResponse>> {
        val graphqlQuery = GraphqlBody.Builder(EventQueries.GET_AVAILABLE_EVENTS_TO_CHECK_IN)
                .`var`("myLocation", myLocation)
                .build()

        return service.apiService.unconfirmPresence(graphqlQuery)
    }

    fun confirmAttendanceOnEvent(eventId: String?, userName: String?): Completable {
        val graphqlQuery = GraphqlBody.Builder(EventQueries.EVENT_CONFIRM_ATTENDANCE)
                .`var`("eventID", eventId)
                .`var`("userName", userName)
                .build()

        return service.apiService.confirmAttendanceEvent(graphqlQuery)
    }

    fun getEvents(city: String, state: String, type: String): Observable<List<EventHomeApi>> {
        return serviceHome.apiService.getAllEvents(city, state, type).map { it }
    }

    fun getSate(lat: Double, lng: Double, radius: Int): Observable<StateResponse> {
        return service.apiService.getState(lat, lng, radius).map { it[0] }
    }
}