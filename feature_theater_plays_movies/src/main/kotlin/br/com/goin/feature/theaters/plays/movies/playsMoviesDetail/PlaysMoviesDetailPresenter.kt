package br.com.goin.feature.theaters.plays.movies.playsMoviesDetail

import androidx.annotation.StringRes
import java.io.Serializable

interface PlaysMoviesDetailPresenter {
    fun onCreate()
    fun onReceivedPlayId(playId: String?)
    fun onDestroy()
    fun onClickShare()
    fun onClickLike()
    fun onConfirmUnconfirmPresence()
    fun onClickAbout()
    fun onInviteClicked()
    fun onReceivedType(type: Serializable?)
    fun logScreenView()
}