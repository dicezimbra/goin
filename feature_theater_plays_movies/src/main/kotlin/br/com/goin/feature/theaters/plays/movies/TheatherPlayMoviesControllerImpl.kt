package br.com.goin.feature.theaters.plays.movies

import android.content.Context
import br.com.goin.component.navigation.TheatherPlayMoviesController
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.PlaysMoviesDetailActivity
import br.com.goin.feature.theaters.plays.movies.movies.MoviesActivity
import br.com.goin.feature.theaters.plays.movies.theatherPlays.TheaterPlaysActivity

class TheatherPlayMoviesControllerImpl: TheatherPlayMoviesController {

    override fun goToMovieActivity(context: Context, categoriyId: String, categoryName: String){
        MoviesActivity.starter(context, categoriyId, categoryName)
    }

    override fun goToTheatersActivity(context: Context, categoriyId: String, categoryName: String){
        TheaterPlaysActivity.starter(context, categoriyId, categoryName)
    }

    override fun goToTheaterDetail(context: Context, destinationId: String?, name: String?) {
        PlaysMoviesDetailActivity.starter(context, destinationId, name, "THEATER")
    }

    override fun goToMovieDetail(context: Context, destinationId: String?, name: String?) {
        PlaysMoviesDetailActivity.starter(context, destinationId, name, "MOVIE")
    }
}