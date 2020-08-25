package br.com.goin.feature.theaters.plays.movies.model

import br.com.goin.base.BuildConfig
import br.com.goin.component.rest.api.RetrofitService
import br.com.goin.component.rest.api.graphQL.GraphQLResponse
import br.com.goin.component.rest.api.graphQL.GraphqlBody
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.model.SessionsByEvent
import io.reactivex.Observable

class MovieRepository {

    private val service = RetrofitService(MovieApi::class.java, BuildConfig.BASE_URL)

    fun getTheatersPlays(categoryId: String, latLng: List<Float>): Observable<GraphQLResponse<PlaysListDTO>> {
        val graphqlQuery = GraphqlBody.Builder(MovieQueries.GET_ALL_THEATERS_PLAY)
                .`var`("subcategoryId", categoryId)
                .`var`("myLocation", latLng)
                .build()

        return service.apiService.getAllMovies(graphqlQuery)
    }

    fun getAllMovies(categoryId: String, latLng: List<Float>): Observable<GraphQLResponse<PlaysListDTO>> {
        val graphqlQuery = GraphqlBody.Builder(MovieQueries.GET_ALL_MOVIES)
                .`var`("subcategoryId", categoryId)
                .`var`("myLocation", latLng)
                .build()

        return service.apiService.getAllMovies(graphqlQuery)
    }

    fun getMovieSessions(playId: String): Observable<GraphQLResponse<SessionsByEvent>> {
        val graphqlQuery = GraphqlBody.Builder(MovieQueries.GET_SESSIONS_EVENT_BY_ID)
                .`var`("eventId", playId)
                .build()

        return service.apiService.getMovieSessions(graphqlQuery)
    }
}