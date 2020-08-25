package br.com.goin.component_model_club

import br.com.goin.base.GraphqlQuery
import br.com.goin.component.model.event.api.RemoveInterest
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import br.com.goin.component_model_club.model.*
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ClubApi {

    @POST("graphql")
    fun getClub(@Body body: GraphqlBody): Observable<GraphQLResponse<Club>>

    @POST("graphql")
    fun followClub(@Body body: GraphqlQuery): Observable<GraphQLResponse<Club>>

    @POST("graphql")
    fun filterResults(@Body body: GraphqlQuery): Observable<GraphQLResponse<SearchFilterResponse>>

    @POST("graphql")
    fun getClubRating(@Body body: GraphqlQuery): Observable<GraphQLResponse<Rating>>

    @POST("graphql")
    fun userRating(@Body graphqlQuery: GraphqlQuery?): Observable<GraphQLResponse<UserRating>>

    @POST("social/put-interest")
    fun interest(@Body body: RequestInterestBody): Completable

    @POST("social/delete-interest")
    fun interest(@Body body: RemoveInterest): Completable
}