package br.com.goin.feature_theater.session

import br.com.goin.feature_theater.model.TheaterResponse

interface SessionTheaterComponentPresenter {

    fun onCreate()
    fun onReceivedSessions(movieSessionsByEvent: TheaterResponse)
    fun reloadSession(movieSessionsByEvent: TheaterResponse)
}