package br.com.goin.feature.feed.post.checkIn

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.goin.base.helpers.ActivityHelper
import br.com.goin.base.helpers.GpsHelper
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.session.user.location.UserLocation
import br.com.goin.feature.feed.R
import br.com.goin.feature.feed.model.checkIn.CheckIn
import br.com.goin.feature.feed.post.checkIn.adapter.CheckInAdapter
import com.google.gson.Gson
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_checkin.*
import java.util.concurrent.TimeUnit

private val permissionsArray: Array<String> = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
private const val requestLocationCode: Int = 1
private const val EVENT = "EVENT"
private const val PICK_CHECKIN = 333

class CheckInActivity : AppCompatActivity(), CheckInView {

    private val presenter: CheckInPresenter = CheckInPresenterImpl(this)
    private var location: FloatArray? = null
    private var userLocation: UserLocation? = null
    private val disposables = CompositeDisposable()
    private var adapterCheckIn: CheckInAdapter? = null

    companion object {
        fun starter(activity: Activity) {
            activity.startActivityForResult(Intent(activity, CheckInActivity::class.java), PICK_CHECKIN)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkin)

        presenter.onCreate()
    }

    override fun onResume() {
        super.onResume()
        Analytics.instance.screenView(this, getString(R.string.checkin_screen_name))
    }

    override fun askPermissionsLocation() {
        if (!ActivityHelper.hasPermissions(this, permissionsArray)) {
            CheckInHelper.permissionsMessage(this, getString(R.string.needed_permissions_check_in)) {
                ActivityCompat.requestPermissions(this, permissionsArray, requestLocationCode)
            }
        } else {
            GpsHelper.getCurrentLocation(this, {
                location = floatArrayOf(it.latitude.toFloat(), it.longitude.toFloat())
                presenter.onReceiveLocation(location)
            }, {
                CheckInHelper.showSimpleMessage(this, it.message!!)
            })
        }
    }

    override fun configureOnClickListeners() {
        toolbar_check_in.setNavigationOnClickListener {
            presenter.logOnAnalytics(
                    getString(R.string.analytics_checkin_back_action),
                    getString(R.string.analytics_back_label_identifier))
            onBackPressed()
        }
    }

    override fun configureSearchView() {
        search_view_check_in.queryTextChanges()
                .skipInitialValue()
                .doOnNext {
                    showLoading()
                    check_in_list.visibility = View.GONE
                }
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe { filteredResults ->
                    presenter.logOnAnalytics(
                            getString(R.string.analytics_checkin_search_action),
                            getString(R.string.analytics_search_term_label_identifier, filteredResults.toString())
                    )
                    runOnUiThread {
                        adapterCheckIn?.filter(filteredResults.toString())
                        check_in_list.visibility = View.VISIBLE
                        hideLoading()
                    }
                }.addTo(disposables)
    }

    override fun showLoading() {
        progress_bar_check_in.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progress_bar_check_in.visibility = View.GONE
    }

    override fun showNoEventsAvaiable() {
        progress_bar_check_in.visibility = View.GONE
        check_in_no_events.visibility = View.VISIBLE
    }

    override fun onReceiveEventsToCheckIn(it: List<CheckIn>) {
        adapterCheckIn = CheckInAdapter(it)
        check_in_list.layoutManager = LinearLayoutManager(this)
        check_in_list.adapter = adapterCheckIn
        check_in_list.visibility = View.VISIBLE

        adapterCheckIn?.onEvenClick = {
            val gson = Gson()
            val intent = Intent()

            presenter.logOnAnalytics(
                    getString(R.string.analytics_checkin_select_place_action),
                    getString(R.string.analytics_local_checkin_label_identifier, it.name))

            intent.putExtra(EVENT, gson.toJson(it))
            intent.putExtra("PICK_CHECKIN", PICK_CHECKIN)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        userLocation?.let {
            location = floatArrayOf(it.lat.toFloat(), it.lng.toFloat())
            presenter.onReceiveLocation(location)
        }
    }
}