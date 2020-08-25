package br.com.goin.feature.theaters.plays.movies.playsMoviesDetail

import android.util.Log
import androidx.annotation.StringRes
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.model.SessionsByEvent
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.extensions.useCache
import br.com.goin.base.extensions.useMemoryCache
import br.com.goin.component.model.event.Event
import br.com.goin.component.model.event.EventInteractor
import br.com.goin.feature.theaters.plays.movies.model.MovieInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.io.Serializable

class PlaysMoviesDetailPresenterImpl(val view: PlaysMoviesDetailView) : PlaysMoviesDetailPresenter {

    private val interactor = MovieInteractor.instance
    private val eventInteractor = EventInteractor.instance

    private var playId: String? = null
    private var event: Event? = null
    private var type: TYPE? = null

    private var disposables = CompositeDisposable()

    override fun onCreate() {
        view.configureOnClickListeners()
        view.configureToolbar()
        loadEvent()
    }

    override fun onReceivedType(type: Serializable?) {
        this.type = type as TYPE
    }

    override fun onDestroy() {
        disposables.dispose()
    }

    override fun onReceivedPlayId(playId: String?) {
        this.playId = playId
    }

    override fun logScreenView() {
        when(type){
            TYPE.MOVIE -> {
                view.logScreenViewMovie()
            }

            TYPE.THEATER -> {
                view.logScreenViewTheater()
            }
        }
    }

    override fun onClickLike() {
        event?.let {
            if (it.isCheckedIn()) {
                view.showCannotRemoveLikeDialog()
            } else {
                if (it.isConfirmed) {
                    view.showDialogUnconfirmPresence(it)
                } else {
                    confirmPresence()
                    view.configureButtonConfirmPresence(it)
                }
            }
        }
    }

    override fun onConfirmUnconfirmPresence() {
        unconfirmPresence()
    }

    override fun onClickShare() {
        event?.let {
            view.shareEvent(it)
        }
    }

    override fun onClickAbout() {
        event?.let {
            view.goToMovieAbout(it, type ?: TYPE.MOVIE)
        }
    }

    private fun confirmPresence() {
        event?.let {
            eventInteractor.confirmEventPresence(it.id)
                    .ioThread()
                    .subscribe({ _ ->
                    }, { t -> Log.w("PlaysMoviesDetail", t) }).addTo(disposables)
        }
    }

    private fun unconfirmPresence() {
        event?.let {
            eventInteractor.unconfirmEventPresence(it.id)
                    .ioThread()
                    .subscribe({ _ ->
                    }, { t -> Log.w("PlaysMoviesDetail", t) }).addTo(disposables)
        }
    }

    private fun loadEvent() {
        playId?.let { playId ->
            eventInteractor.getDetail(playId, null)
                    .useMemoryCache("MOVIE_EVENT-$playId", Event::class.java)
                    .ioThread()
                    .doOnNext { event ->
                        this.event = event
                        view.hideProgress()
                        view.onReceiveEventDetails(event)
                    }
                    .flatMap { interactor.getMovieSessions(playId).ioThread() }
                    .useCache("MOVIE_SESSION-$playId", SessionsByEvent::class.java)
                    .ioThread()
                    .doOnSubscribe { view.showProgress() }
                    .subscribe({ sessionsDTO ->
                        view.hideProgress()
                        view.configureSessionComponent(sessionsDTO)
                    }, { t ->
                        view.handleError(t)
                        Log.e("PlaysMoviesDetail", t.message)
                    }).addTo(disposables)
        }
    }

    override fun onInviteClicked() {
        event?.let {
            view.goToInvite(it.id)
        }
    }
}