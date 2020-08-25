package br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.session

import br.com.goin.component.session.user.location.UserLocation

interface SessionView {
    fun configureRecyclerView(sessions: List<Session>)
    fun openMaps(eventSession: Session)
    fun notifyAdapterPosiiton(position: Int)
    fun openUber(eventSession: Session, userLocation: UserLocation)
}