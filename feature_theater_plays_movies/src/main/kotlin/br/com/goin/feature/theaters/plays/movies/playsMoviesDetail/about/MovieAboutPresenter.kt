package br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.about

import java.io.Serializable

interface MovieAboutPresenter {
    fun onReceiveEvent(event: Any?)
    fun onCreate()
    fun logScreenName()
    fun onReceiveType(serializable: Serializable?)
}