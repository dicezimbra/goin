package br.com.goin.feature_theater.session

import br.com.goin.feature_theater.model.TheaterResponse

class SessionTheaterComponentPresenterImpl(val view: SessionTheaterComponentView) : SessionTheaterComponentPresenter {

    private var theaterResponse: TheaterResponse? = null

    override fun onCreate() {
        theaterResponse?.sessionsByClub?.let {
            view.configureSession(it)
        }
    }

    override fun onReceivedSessions(movieSessionsByEvent: TheaterResponse) {
        this.theaterResponse = movieSessionsByEvent
    }

    override fun reloadSession(movieSessionsByEvent: TheaterResponse) {
        this.theaterResponse?.sessionsByClub?.let {
            this.theaterResponse = movieSessionsByEvent
            view.configureSession(it)
        }
    }
}