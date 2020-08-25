package br.com.goin.feature.theaters.plays.movies.movies

interface MoviesPresenter {
    fun onCreate()
    fun onReceivedCategoryId(categoryId: String?)
    fun onToolbarSearchClicked()
    fun onDestroy()
}