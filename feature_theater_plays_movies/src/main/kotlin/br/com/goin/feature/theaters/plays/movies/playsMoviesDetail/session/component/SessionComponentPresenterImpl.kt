package br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.session.component

import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.model.SessionsByEvent

class SessionComponentPresenterImpl(val view: SessionComponentView) : SessionComponentPresenter {

    private var movieSessionsByEvent: SessionsByEvent? = null

    override fun onCreate() {
        movieSessionsByEvent?.sessionsByEvent?.let {
            view.configureSession(it)
        }
    }

    override fun onReceivedSessions(movieSessionsByEvent: SessionsByEvent) {
        this.movieSessionsByEvent = movieSessionsByEvent
    }

    override fun reloadSession(movieSessionsByEvent: SessionsByEvent) {
        this.movieSessionsByEvent?.sessionsByEvent?.let {
            this.movieSessionsByEvent = movieSessionsByEvent
            view.configureSession(it)
        }
    }
}