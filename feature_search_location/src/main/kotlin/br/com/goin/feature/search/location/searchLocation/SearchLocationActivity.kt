package br.com.goin.feature.search.location.searchLocation

import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.extensions.toUpperCaseNoSpace
import br.com.goin.base.utils.Constants
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.feature.search.location.searchLocation.model.CityLocation
import br.com.goin.component.session.user.location.UserLocationHelper
import br.com.goin.component.ui.kit.features.error.ErrorViewHelper
import br.com.goin.feature.search.location.R
import com.google.gson.Gson
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_search_location.*
import java.util.concurrent.TimeUnit

private const val RESULT_SEARCH_LOCATION = "RESULT_SEARCH_LOCATION"

class SearchLocationActivity : AppCompatActivity(), SearchLocationView {

    companion object {

        fun starter(fragment: Fragment) {
            fragment.startActivityForResult(Intent(fragment.context, SearchLocationActivity::class.java), Constants.SEARCH_LOCATION)
        }

        fun starter(activity: AppCompatActivity) {
            activity.startActivityForResult(Intent(activity, SearchLocationActivity::class.java), Constants.SEARCH_LOCATION)
        }

        fun starter(activity: Activity) {
            activity.startActivityForResult(Intent(activity, SearchLocationActivity::class.java), Constants.SEARCH_LOCATION)
        }
    }

    private val presenter: SearchLocationPresenter = SearchLocationPresenterImpl(this)
    private var adapter: SearchLocationAdapter? = null
    private val disposables = CompositeDisposable()
    private val userLocationHelper = UserLocationHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_location)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        supportActionBar?.setDisplayShowTitleEnabled(false)

        userLocationHelper.askPermission(this)
        presenter.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
        presenter.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        Analytics.instance.screenView(this, getString(R.string.location_screen_name))
    }

    override fun configureLocationChange() {
        userLocationHelper.onRequestLocationListener = {
            when (it) {
                null -> {
                    presenter.onLoadCurrentLocation(CityLocation(title = getString(R.string.location),
                            iconRes = R.drawable.ic_my_location_black_24dp,
                            highlight = true))
                }
                else -> loadCityLocation(it)
            }
        }
    }

    override fun askLocationPermission() {
        userLocationHelper.askPermission(this)
    }

    override fun configureCities(cityLocations: List<CityLocation>) {
        adapter?.configureCities(cityLocations)
    }

    override fun sendIntentResult(it: String) {
        val gson = Gson()
        val data = Intent()
        data.putExtra(RESULT_SEARCH_LOCATION, gson.toJson(it))
        setResult(Activity.RESULT_OK, data)
    }

    override fun showLoading(query: String?) {
        empty_city_text.visibility = View.GONE
        recycler_view.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE
        progress_text.visibility = View.VISIBLE
        progress_text.text = getString(R.string.searching)

        when (query) {
            null -> progress_text.text = getString(R.string.searching)
            "" -> progress_text.text = getString(R.string.searching)
            else -> progress_text.text = getString(R.string.searching_city, query)
        }
    }

    override fun hideLoading() {
        recycler_view.visibility = View.GONE
        empty_city_text.visibility = View.GONE
        recycler_view.visibility = View.VISIBLE
        progress_bar.visibility = View.GONE
        progress_text.visibility = View.GONE
    }

    override fun showEmptyResult(query: String) {
        recycler_view.visibility = View.GONE
        empty_city_text.visibility = View.VISIBLE
        empty_city_text.text = getString(R.string.empty_city_search, query)
    }

    override fun configureSearchField() {
        search_field.queryTextChanges()
                .skipInitialValue()
                .doOnNext { showLoading(it.toString()) }
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe { event ->
                    runOnUiThread { presenter.onSearchChanged(event.toString()) }
                }.addTo(disposables)
    }

    override fun configureRecyclerView() {
        adapter = SearchLocationAdapter()
        adapter?.onClickListener = { cityLocation ->
            if(cityLocation.highlight){
                Analytics.instance.logClickEvent(getString(R.string.city_category), getString(R.string.current_location_category))
            }else{
                Analytics.instance.logClickEvent(getString(R.string.city_category), cityLocation.city?.toUpperCaseNoSpace() ?: "")
            }

            presenter.onLocationClicked(cityLocation)
        }

        recycler_view.adapter = adapter
        recycler_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recycler_view.layoutManager = LinearLayoutManager(this)
    }

    override fun handleError(throwable: Throwable, query: String){
        ErrorViewHelper.handleErrorView(container, throwable){
            presenter.onSearchChanged(query)
        }
    }

    private fun loadCityLocation(location: Location) {
        LocationHelper.getCitiesFromLocation(this, location)
                .ioThread()
                .doOnSubscribe { showLoading(null) }
                .flatMapIterable { it }
                .filter { !it.locality.isNullOrBlank() }
                .distinct { it.locality }
                .map {
                    CityLocation(
                            lat = it.latitude,
                            lng = it.longitude,
                            title = it.locality,
                            city = it.locality,
                            iconRes = R.drawable.ic_my_location_black_24dp,
                            description = getString(R.string.current_location),
                            highlight = true)
                }
                .defaultIfEmpty(CityLocation(title = getString(R.string.location), highlight = true))
                .subscribe({ cityLocations ->
                    presenter.onLoadCurrentLocation(cityLocations)
                }, { t: Throwable ->
                    Log.e("SearchLocation", t.message, t)
                })
                .addTo(disposables)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        userLocationHelper.onRequestPermissionsResult(this, requestCode, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        userLocationHelper.onActivityResult(this, requestCode, resultCode, data)
    }
}