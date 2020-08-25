package br.com.goin.feature.theaters.plays.movies.movies

import android.util.Log
import br.com.goin.feature.theaters.plays.movies.model.PlaysListModel
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.extensions.useCache
import br.com.goin.base.extensions.useMemoryCache
import br.com.goin.component.session.user.location.UserLocation
import br.com.goin.component.session.user.location.UserLocationInteractor
import br.com.goin.feature.theaters.plays.movies.model.MovieInteractor
import io.reactivex.disposables.Disposable

class MoviesPresenterImpl(val view: MoviesView) : MoviesPresenter {

    private val interactor = MovieInteractor.instance
    private var categoryId: String? = null
    private var userLocation: UserLocation? = null
    private var disposable: Disposable? = null
    private val userLocationInteractor = UserLocationInteractor.instance

    override fun onCreate() {
        userLocation = userLocationInteractor.fetch()

        val city = if(userLocation?.uf == null) {
             "${userLocation?.city}"
        }else{
            "${userLocation?.city},${userLocation?.uf}"
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
        disposable?.dispose()
    }

    private fun loadMovies() {
        val location = userLocation?.let {
            arrayListOf(it.lat.toFloat(), it.lng.toFloat())
        } ?: arrayListOf()

        categoryId?.let { categoryId ->
            disposable = interactor.getAllMovies(categoryId, location)
                    .useMemoryCache(key = "MOVIES_CACHE${location}", type = PlaysListModel::class.java)
                    .ioThread()
                    .doOnSubscribe { view.showProgress() }
                    .subscribe({ movies ->
                        movies.highlighted?.let {
                            view.configureCarousel(it)
                        }

                        view.configureViewPager(movies)
                        view.configureTabs()
                        view.hideProgress()
                    }, { t ->
                        view.handleError(t)
                        Log.e("MoviesPresnter", t.message)
                    })
        }
    }
}