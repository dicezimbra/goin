package br.com.goin.feature.theaters.plays.movies.theatherPlays

interface TheatersPlaysPresenter {
    fun onCreate()
    fun onToolbarSearchClicked()
    fun onDestroy()
    fun onReceivedCategoryId(categoryId: String?)
}