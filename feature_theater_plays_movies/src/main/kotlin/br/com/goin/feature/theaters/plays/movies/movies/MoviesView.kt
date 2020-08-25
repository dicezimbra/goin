package br.com.goin.feature.theaters.plays.movies.movies

import br.com.goin.feature.theaters.plays.movies.model.PlayModel
import br.com.goin.feature.theaters.plays.movies.model.PlaysListModel

interface MoviesView {
    fun showProgress()
    fun hideProgress()
    fun configureToolbar(toolbarTitle : String?)
    fun configureTabs()
    fun configureViewPager(model: PlaysListModel)
    fun goToMovieSearch(playType: String)
    fun configureCarousel(highlightedPlays: List<PlayModel>)
    fun handleError(throwable: Throwable)
}