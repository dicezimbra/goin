package br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.about

import br.com.goin.component.model.event.Event

interface MovieAboutView {
    fun configureEventView(event: Event)
    fun configureToolbar()
    fun logScreenNameMovie()
    fun logScreenNameTheater()
}