package br.com.goin.feature.theaters.plays.movies.model

import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.model.SessionsByEvent
import io.reactivex.Observable

interface MovieInteractor {

    companion object {
        const val THEATER_CATEGORY_ID = "163c71f7-686c-41b7-aa8a-07227470f556"
        val instance: MovieInteractor = MovieInteractorImpl()
    }

    fun getAllMovies(playType: String, latLng: List<Float>): Observable<PlaysListModel>
    fun getMovieSessions(playId: String): Observable<SessionsByEvent>
    fun getTheatersPlays(latLng: List<Float>): Observable<PlaysListModel>
}