package br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.session.component

import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.model.SessionsByEvent

interface SessionComponentPresenter {
    fun onCreate()
    fun onReceivedSessions(movieSessionsByEvent: SessionsByEvent)
    fun reloadSession(movieSessionsByEvent: SessionsByEvent)
}