package br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.about

import br.com.goin.component.model.event.Event
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.TYPE
import java.io.Serializable

class MovieAboutPresenterImpl(val view: MovieAboutView): MovieAboutPresenter{

    private var event: Event? = null
    private var type: TYPE? = null

    override fun onReceiveEvent(event: Any?){
        this.event = event as? Event
    }

    override fun onReceiveType(serializable: Serializable?) {
        this.type = serializable  as? TYPE
    }

    override fun onCreate(){
        view.configureToolbar()
        event?.let {
            view.configureEventView(it)
        }
    }

    override fun logScreenName() {
        when(type){
            TYPE.MOVIE -> {
                view.logScreenNameMovie()
            }
            TYPE.THEATER -> {
                view.logScreenNameTheater()
            }
        }
    }
}