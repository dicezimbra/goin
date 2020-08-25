package br.com.goin.feature.theaters.plays.movies.model

import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.model.SessionsByEvent
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface MovieApi {

    @POST("graphql")
    fun getAllMovies(@Body query: GraphqlBody): Observable<GraphQLResponse<PlaysListDTO>>

    @POST("graphql")
    fun getMovieSessions(@Body query: GraphqlBody): Observable<GraphQLResponse<SessionsByEvent>>
}