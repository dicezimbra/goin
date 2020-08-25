package br.com.goin.feature_theater.session

import br.com.goin.feature_theater.model.ClubSessions
import br.com.goin.feature_theater.model.TheaterResponse

interface SessionTheaterComponentView {

    fun reloadSession(theaterResponse: TheaterResponse)
    fun configureSession(clubSessions: List<ClubSessions>)
}