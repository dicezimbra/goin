package br.com.goin.feature.theaters.plays.movies.playsMoviesDetail

import androidx.annotation.StringRes
import br.com.goin.component.model.event.Event
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.model.SessionsByEvent

interface PlaysMoviesDetailView {
    fun showProgress()
    fun hideProgress()
    fun onReceiveEventDetails(event: Event)
    fun configureOnClickListeners()
    fun showCannotRemoveLikeDialog()
    fun configureButtonConfirmPresence(event: Event)
    fun showDialogUnconfirmPresence(event: Event)
    fun configureToolbar()
    fun shareEvent(event: Event)
    fun goToMovieAbout(event: Event, type: TYPE)
    fun configureSessionComponent(sessionsByEvent: SessionsByEvent)
    fun goToInvite(id: String?)
    fun handleError(throwable: Throwable)
    fun logScreenViewMovie()
    fun logScreenViewTheater()
}