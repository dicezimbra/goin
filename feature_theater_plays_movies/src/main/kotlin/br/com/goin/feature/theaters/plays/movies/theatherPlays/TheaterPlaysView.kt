package br.com.goin.feature.theaters.plays.movies.theatherPlays

import br.com.goin.feature.theaters.plays.movies.model.PlayModel
import br.com.goin.feature.theaters.plays.movies.model.PlaysListModel

interface TheaterPlaysView {
    fun showProgress()
    fun hideProgress()
    fun configureTabs()
    fun configureViewPager(model: PlaysListModel)
    fun goToMovieSearch(playType: String)
    fun configureCarousel(highlightedPlays: List<PlayModel>)
    fun configureToolbar(userLocation: String?)
    fun handleError(throwable: Throwable)
}