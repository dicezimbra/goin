package br.com.goin.feature.search.location.searchLocation

import android.util.Log
import br.com.goin.feature.search.location.searchLocation.model.CityInteractor
import br.com.goin.feature.search.location.searchLocation.model.CityLocation
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.extensions.useCache
import br.com.goin.base.helpers.PreferenceHelper
import br.com.goin.component.session.user.location.UserLocation
import br.com.goin.component.session.user.location.UserLocationInteractor
import com.google.gson.reflect.TypeToken
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

private const val USER_LOCATION = "USER_LOCATION"

class SearchLocationPresenterImpl(val view: SearchLocationView) : SearchLocationPresenter {

    private val cityInteractor = CityInteractor.instance
    private val userLocationInteractor = UserLocationInteractor.instance

    private var disposable = CompositeDisposable()
    private var currentLocation: CityLocation? = null

    private var cities: List<CityLocation>? = null

    override fun onCreate() {
        view.configureRecyclerView()
        view.configureLocationChange()
        view.configureSearchField()
        view.showLoading(null)
    }

    override fun onLoadCurrentLocation(cityLocation: CityLocation) {
        this.currentLocation = cityLocation
        this.cities = listOf(cityLocation)
        view.configureCities(cities!!)
        view.hideLoading()

        currentLocation?.let {
            if (it.lat != null && it.lng != null) {
                fetchCitiesNearby(it.lat, it.lng)
            }
        }
    }

    override fun onDestroy() {
        disposable.dispose()
        disposable = CompositeDisposable()
    }

    override fun onLocationClicked(cityLocation: CityLocation) {
        when {
            cityLocation.hasLatLng() -> {
                if (cityLocation.lat != null && cityLocation.lng != null) {
                    val userLocation = UserLocation(lat = cityLocation.lat, lng = cityLocation.lng,
                             cityName = cityLocation.title,
                            city = cityLocation.city, state = cityLocation.state,
                            uf = cityLocation.uf)
                    userLocationInteractor.save(userLocation)

                    val json = PreferenceHelper.read(USER_LOCATION) as? String
                    json?.let {
                        view.sendIntentResult(it)
                    }
                }
                view.finish()
            }
            else -> {
                view.askLocationPermission()
            }
        }
    }

    override fun onSearchChanged(query: String) {
        if (query.isBlank()) {
            val cities = arrayListOf<CityLocation>()
            currentLocation?.let { cities.add(it) }

            view.configureCities(cities)
            view.hideLoading()

            currentLocation?.let { cityLocation ->
                if (cityLocation.lat != null && cityLocation.lng != null) {
                    fetchCitiesNearby(cityLocation.lat, cityLocation.lng)
                }
            }

        } else {
            fetchCitiesByName(query)
        }
    }

    private fun fetchCitiesNearby(lat: Double, lng: Double) {
        cityInteractor.fetchByLocation(lat = lat, lng = lng)
                .useCache("CITIES_NEARBY", object : TypeToken<ArrayList<CityLocation>>() {}.type)
                .ioThread()
                .subscribe({
                    view.hideLoading()
                    when {
                        !it.isEmpty() -> {
                            val cities = ArrayList(it)
                            cities.add(0, currentLocation)

                            if(this.cities != cities) {
                                this.cities = cities
                                view.configureCities(cities)
                            }
                        }
                    }
                }, { t: Throwable ->
                    Log.e("SearchLocation", t.message, t)
                }).addTo(disposable)
    }

    private fun fetchCitiesByName(query: String) {
        disposable.dispose()
        disposable = CompositeDisposable()

        cityInteractor.fetchByName(query)
                .useCache("CITIES_NAME$query", object : TypeToken<ArrayList<CityLocation>>() {}.type)
                .ioThread()
                .subscribe({
                    view.hideLoading()

                    when {
                        it.isEmpty() -> {
                             cities = ArrayList(it)
                             view.showEmptyResult(query)
                        }
                        else -> {
                            val cities = ArrayList(it)
                            cities.add(0, currentLocation)

                            if(this.cities != cities) {
                                this.cities = cities
                                view.configureCities(cities)
                            }
                        }
                    }
                }, { t: Throwable ->
                    view.handleError(t, query)
                    Log.e("SearchLocation", t.message, t)
                }).addTo(disposable)
    }
}