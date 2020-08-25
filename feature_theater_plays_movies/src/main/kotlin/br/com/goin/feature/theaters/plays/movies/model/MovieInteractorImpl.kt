package br.com.goin.feature.theaters.plays.movies.model

import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.model.SessionsByEvent
import br.com.goin.feature.theaters.plays.movies.model.MovieInteractor.Companion.THEATER_CATEGORY_ID
import io.reactivex.Observable

class MovieInteractorImpl : MovieInteractor {

    private val repository = MovieRepository()
    private val mapper = MovieMapper()

    override fun getTheatersPlays(latLng: List<Float>): Observable<PlaysListModel> {
        return repository.getTheatersPlays(THEATER_CATEGORY_ID, latLng).map { mapper.mapDtoToModel(it.data) }
    }

    override fun getAllMovies(playType: String, latLng: List<Float>): Observable<PlaysListModel> {
        return repository.getAllMovies(playType, latLng).map { mapper.mapDtoToModel(it.data) }
    }

    override fun getMovieSessions(playId: String): Observable<SessionsByEvent> {
        return repository.getMovieSessions(playId).map { it.data }
    }
}