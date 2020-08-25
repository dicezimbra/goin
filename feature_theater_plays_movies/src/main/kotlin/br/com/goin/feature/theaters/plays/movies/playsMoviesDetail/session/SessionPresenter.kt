package br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.session

import java.io.Serializable

interface SessionPresenter {
    fun onCreate()
    fun onReceivedSerializable(serializable: Serializable?)
    fun onDestroy()
    fun onUberClicked(session: Session)
}