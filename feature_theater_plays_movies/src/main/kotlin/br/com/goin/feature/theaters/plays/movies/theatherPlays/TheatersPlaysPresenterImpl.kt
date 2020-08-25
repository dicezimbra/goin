package br.com.goin.feature.theaters.plays.movies.theatherPlays

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.extensions.useCache
import br.com.goin.base.extensions.useMemoryCache
import br.com.goin.feature.theaters.plays.movies.model.PlaysListModel
import br.com.goin.component.session.user.location.UserLocation
import br.com.goin.component.session.user.location.UserLocationInteractor
import br.com.goin.feature.theaters.plays.movies.model.MovieInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class TheatersPlaysPresenterImpl(val view: TheaterPlaysView) : TheatersPlaysPresenter {

    private val interactor = MovieInteractor.instance
    private val userLocationInteractor = UserLocationInteractor.instance
    private var disposable = CompositeDisposable()
    private lateinit var userLocation: UserLocation
    private var categoryId: String? = null

    override fun onCreate() {
        userLocation = userLocationInteractor.fetch()

        val city = if(userLocation.uf == null) {
            "${userLocation.city}"
        }else{
            "${userLocation.city},${userLocation.uf}"
        }

        view.configureToolbar(city)
        loadMovies()
    }

    override fun onReceivedCategoryId(categoryId: String?) {
        this.categoryId = categoryId
    }

    override fun onToolbarSearchClicked() {
        categoryId?.let {
            view.goToMovieSearch(it)
        }
    }

    override fun onDestroy() {
        disposable.dispose()
    }

    private fun loadMovies() {
        val location = arrayListOf(userLocation.lat.toFloat(), userLocation.lng.toFloat())
        interactor.getTheatersPlays(location)
            .useMemoryCache(key = "THEATERS_CACHE${location}", type = PlaysListModel::class.java)
            .ioThread()
            .doOnSubscribe { view.showProgress() }
            .subscribe({ plays ->
                plays.highlighted?.let {
                    view.configureCarousel(it)
                }
                view.configureViewPager(plays)
                view.configureTabs()
                view.hideProgress()
            }, { t ->
                view.handleError(t)
                Log.e("MoviesPresnter", t.message)
            })
                .addTo(disposable)
    }
}