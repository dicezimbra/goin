package br.com.goin.feature.home.goin

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.extensions.useCache
import br.com.goin.base.extensions.useMemoryCache
import br.com.goin.component.model.category.CategoriesInteractor
import br.com.goin.component.model.category.Category
import br.com.goin.component.model.event.Banner
import br.com.goin.component.model.event.EventHome
import br.com.goin.component.model.event.EventInteractor
import br.com.goin.component.session.user.location.UserLocation
import br.com.goin.component.session.user.location.UserLocationInteractor
import com.google.gson.reflect.TypeToken
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

private const val CATEGORIES_CACHE = "CATEGORIES_HOME"
private const val HIGHLIGHTED_EVENTS_HOME = "HIGHLIGHTED_EVENT_HOME"

private const val WEEK_EVENTS_HOME = "WEEK_EVENT_HOME"
private const val WEEKEND_EVENTS_HOME = "WEEKEND_EVENT_HOME"
private const val BANNERS_HOME = "BANNERS_HOME"

class GoinPresenterImpl(val view: GoinView) : GoinPresenter {

    private val categoriesInteractor = CategoriesInteractor.instance
    private val eventInteractor = EventInteractor.instance
    private val userLocationInteractor = UserLocationInteractor.instance
    private var disposables = CompositeDisposable()

    private var requestsFinished = 0

    override fun onCreate() {
        requestsFinished = 0
        view.configureRefreshView()
        view.configureClickListeners()

        if (userLocationInteractor.hasLocation()) {
            refresh()
        } else {
            view.setupPermissions()
        }
    }

    override fun onResume() {
        val userLocation = userLocationInteractor.fetch()
        userLocation.let {
            reloadData(it)
            view.configureCurrentLocation(it)
        }
    }

    override fun onDestroy() {
        disposables.dispose()
    }

    override fun refresh() {
        val userLocation = userLocationInteractor.fetch()
        view.configureCurrentLocation(userLocation)

        checkState(userLocation) {
            loadData(userLocation.city, it)
        }
    }

    private fun checkState(userLocation: UserLocation, function: (String?) -> Unit) {
        if (userLocation.uf == null || "BR".equals(userLocation.uf)) {
            //TODO remover essa porra de BR!
            eventInteractor.getSate(userLocation.lat, userLocation.lng)
                    .ioThread()
                    .subscribe({ state ->
                        function.invoke(state.uf)
                    }, { t ->
                        view.handleError(t)
                        Log.e("GoinPresenter", t.message, t)
                    }).addTo(disposables)

        } else {
            function.invoke(userLocation.uf)
        }
    }

    private fun checkState(state: String?, function: (String?) -> Unit) {
        if (state == null || "BR".equals(state)) {
            //TODO remover essa porra de BR!
            val userLocation = userLocationInteractor.fetch()
            eventInteractor.getSate(userLocation.lat, userLocation.lng)
                    .ioThread()
                    .subscribe({ state ->
                        function.invoke(state.uf)
                    }, { t ->
                        view.handleError(t)
                        Log.e("GoinPresenter", t.message, t)
                    }).addTo(disposables)

        } else {
            function.invoke(state)
        }
    }

    override fun reloadData(userLocation: UserLocation) {
        checkState(userLocation) {
            loadEventsHighlighted(userLocation.city, it, GoinTypes.HIGHLIGHTED.name)
            loadEventsThisWeek(userLocation.city, it, GoinTypes.WEEK.name)
            loadEventsThisWeekend(userLocation.city, it, GoinTypes.WEEKEND.name)
        }
    }

    private fun loadData(city: String?, state: String?) {
        checkState(state) {
            view.hideLoading()
            loadBanner()
            loadCategories()
            loadEventsHighlighted(city, it, GoinTypes.HIGHLIGHTED.name)
            loadEventsThisWeek(city, it, GoinTypes.WEEK.name)
            loadEventsThisWeekend(city, it, GoinTypes.WEEKEND.name)
        }
    }

    private fun loadEventsHighlighted(city: String?, state: String?, type: String) {
        checkState(state) {
            city?.let { c ->
                it?.let { s ->
                    eventInteractor.getEvents(city = c, state = s, type = type)
                            .useMemoryCache("$HIGHLIGHTED_EVENTS_HOME$c$s$type", object : TypeToken<ArrayList<EventHome>>() {}.type)
                            .ioThread()
                            .subscribe({ events ->
                                val userLocation = userLocationInteractor.fetch()
                                onHighlightEventsLoaded(events, userLocation)
                            }, { t ->
                                view.handleError(t)
                                Log.e("GoinPresenter", t.message, t)
                            }).addTo(disposables)

                }
            }
        }
    }

    private fun loadEventsThisWeek(city: String?, state: String?, type: String) {
        checkState(state) {
            city?.let { c ->
                it?.let { s ->
                    eventInteractor.getEvents(city = c, state = s, type = type)
                            .useMemoryCache("$WEEK_EVENTS_HOME$c$s$type", object : TypeToken<ArrayList<EventHome>>() {}.type)
                            .ioThread()
                            .subscribe({ events ->
                                val userLocation = userLocationInteractor.fetch()
                                onThisWeekEventsLoaded(events, userLocation)
                            }, { t ->
                                view.handleError(t)
                                Log.e("GoinPresenter", t.message, t)
                            }).addTo(disposables)
                }
            }
        }
    }

    private fun loadEventsThisWeekend(city: String?, state: String?, type: String) {
        checkState(state) {
            city?.let { c ->
                it?.let { s ->
                    eventInteractor.getEvents(city = c, state = s, type = type)
                            .useMemoryCache("$WEEKEND_EVENTS_HOME$c$s$type", object : TypeToken<ArrayList<EventHome>>() {}.type)
                            .ioThread()
                            .subscribe({ events ->
                                val userLocation = userLocationInteractor.fetch()
                                onThisWeekendEventsLoaded(events, userLocation)
                            }, { t ->
                                view.handleError(t)
                                Log.e("GoinPresenter", t.message, t)
                            }).addTo(disposables)
                }
            }
        }
    }

    private fun onHighlightEventsLoaded(eventApis: List<EventHome>, userLocation: UserLocation) {
        if (eventApis.isEmpty()) {
            view.configureHighlightedEventsEmpty()
        } else {
            view.configureHightlightedEvents(eventApis, userLocation)
        }

    }

    private fun onThisWeekEventsLoaded(eventApis: List<EventHome>, userLocation: UserLocation) {
        if (eventApis.isEmpty()) {
            view.configureWeekEventsEmpty()
        } else {
            view.configureWeekEvents(eventApis, userLocation)
        }
    }

    private fun onThisWeekendEventsLoaded(eventApis: List<EventHome>, userLocation: UserLocation) {
        if (eventApis.isEmpty()) {
            view.configureWeekendEventsEmpty()
        } else {
            view.configureWeekendEvents(eventApis, userLocation)
        }
    }

    private fun loadCategories() {
        categoriesInteractor.getCategories()
                .useMemoryCache(CATEGORIES_CACHE, object : TypeToken<ArrayList<Category>>() {}.type)
                .ioThread()
                .subscribe({
                    checkLoading()
                    onCategoriesLoaded(it)
                }, { t ->
                    view.handleError(t)
                    Log.e("GoinPresenter", t.message, t)
                }).addTo(disposables)
    }

    private fun loadBanner() {
        eventInteractor.getBanners()
                .useMemoryCache(BANNERS_HOME, object : TypeToken<ArrayList<Banner>>() {}.type)
                .ioThread()
                .subscribe({
                    checkLoading()
                    if (it.isEmpty()) {
                        view.configureBannerEmpty()
                    } else {
                        view.configureViewPager(it.filter { e -> Banner.TYPE_HIGHLIGHT == e.typeBanner })
                        view.configureBanner(it.filter { e -> Banner.TYPE_BANNER == e.typeBanner })
                    }
                }, { t ->
                    view.handleError(t)
                    Log.e("GoinPresenter", t.message, t)
                }).addTo(disposables)
    }

    private fun onCategoriesLoaded(categories: List<Category>) {
        if (categories.isEmpty()) {
            view.configureCategoriesEmpty()
        } else {
            view.configureCategories(categories)
        }
    }

    private fun checkLoading() {
        requestsFinished++
        if (requestsFinished > 4) {
            view.hideLoading()
        }
    }
}