package br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.session.component

import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.model.EventSessions
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.model.SessionsByEvent

interface SessionComponentView {
    fun reloadSession(movieSessionsByEvent: SessionsByEvent)
    fun configureSession(sessionsByEvent: List<EventSessions>)
}