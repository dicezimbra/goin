package br.com.goin.component.model.event.api

import br.com.goin.component.model.event.EventHome
import br.com.goin.component.model.event.api.response.EventResponse
import br.com.goin.component.model.event.api.response.StateResponse
import br.com.goin.component.model.event.api.response.ValidateCuponResponse
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import io.reactivex.Completable
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EventApi {

    @POST("graphql")
    fun getBanner(@Body body: GraphqlBody): Observable<GraphQLResponse<EventResponse>>

    @GET("home/get-carousel")
    fun getAllEvents(@Query("city") city: String,
                     @Query("state") state: String,
                     @Query("carousel") carousel: String): Observable<List<EventHomeApi>>

    @POST("graphql")
    fun getDetail(@Body body: GraphqlBody): Observable<GraphQLResponse<EventResponse>>

    @POST("graphql")
    fun getEventsShort(@Body body: GraphqlBody): Observable<GraphQLResponse<EventResponse>>

    @POST("graphql")
    fun claimVoucher(@Body body: GraphqlBody): Completable

    @POST("graphql")
    fun confirmPresence(@Body body: GraphqlBody): Observable<GraphQLResponse<EventResponse>>

    @POST("graphql")
    fun unconfirmPresence(@Body body: GraphqlBody): Observable<GraphQLResponse<EventResponse>>

    @POST("graphql")
    fun confirmTokenIsValid(@Body body: GraphqlBody): Observable<GraphQLResponse<EventResponse>>

    @POST("graphql")
    fun confirmStefanini(@Body body: GraphqlBody): Completable

    @POST("graphql")
    fun validateCoupom(@Body graphqlQuery: GraphqlBody): Observable<GraphQLResponse<ValidateCuponResponse>>

    @POST("graphql")
    fun confirmAttendanceEvent(@Body graphqlQuery: GraphqlBody): Completable

    @GET("cities")
    fun getState(@Query("lat") lat: Double,
                 @Query("lng") lng: Double,
                 @Query("radius") radius: Int): Observable<List<StateResponse>>

    @POST("social/put-interest")
    fun interest(@Body body: RequestInterestBody): Completable

    @POST("social/delete-interest")
    fun interest(@Body body: RemoveInterest): Completable

    @GET("social/get-interest")
    fun getInterest(@Query("userId") userId: String): Observable<List<Interrest>>
}