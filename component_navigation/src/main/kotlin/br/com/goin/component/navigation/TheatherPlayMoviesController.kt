package br.com.goin.component.navigation

import android.content.Context

interface TheatherPlayMoviesController{
    fun goToMovieActivity(context: Context, categoriyId: String, categoryName: String)
    fun goToTheatersActivity(context: Context, categoriyId: String, categoryName: String)
    fun goToMovieDetail(context: Context, destinationId: String?, name: String? = "")
    fun goToTheaterDetail(context: Context, destinationId: String?, name: String? = "")
}