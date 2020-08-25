package br.com.goin.feature_theater

import      android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import br.com.goin.base.helpers.GmapHelper
import br.com.goin.base.helpers.ShareHelper
import br.com.goin.base.utils.GpsUtils
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.model.uber.UberHelper
import br.com.goin.component.model.uber.UberInformation
import br.com.goin.component.navigation.NavigationController
import br.com.goin.component.ui.kit.dialog.DialogUtils
import br.com.goin.component.ui.kit.views.GmapLifecycleObserver
import br.com.goin.component_model_club.model.ClubModel
import br.com.goin.feature_theater.model.Info
import br.com.goin.feature_theater.model.TheaterResponse
import br.com.goin.feature_theater.session.SessionTheaterComponent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_theater_detail.*


class TheaterDetailActivity : AppCompatActivity(), TheaterDetailView {

    val presenter: TheaterDetailPresenter = TheaterDetailPresenterImpl(this)
    val navigationControler = NavigationController.instance
    var categoryId: String? = ""
    var categoryName: String? = ""
    var clubId: String? = ""
    private var firstTime: Boolean = true

    companion object {

        private const val CATEGORY_NAME = "CATEGORY_NAME"
        private const val CATEGORY_ID = "CATEGORY_ID"
        private const val CLUB_ID = "CLUB_ID"

        fun starter(context: Context, clubId: String?, categoryId: String, categoryName: String) {
            val intent = Intent(context, TheaterDetailActivity::class.java)
            intent.putExtra(CATEGORY_ID, categoryId)
            intent.putExtra(CATEGORY_NAME, categoryName)
            intent.putExtra(CLUB_ID, clubId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theater_detail)

        if (intent != null) {

            categoryId = intent.getStringExtra(CATEGORY_ID)
            categoryName = intent.getStringExtra(CATEGORY_NAME)
            clubId = intent.getStringExtra(CLUB_ID)
        }

        firstTime = true
        presenter.onCreate(clubId!!)
        lifecycle.addObserver(GmapLifecycleObserver(map_view))
        map_view.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        Analytics.instance.screenView(this, getString(R.string.movie_detail_screen_name))
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun configureToolbar() {

        toolbar.setTitle(R.string.theater_title)
        toolbar.setOnBackButtonClicked { onBackPressed() }
    }

    override fun openMaps(clubModel: ClubModel) {

        if (clubModel.latitude != null && clubModel.longitude != null)

            GmapHelper.openMapsInPosition(this, clubModel.latitude!!.toDouble(),
                    clubModel.longitude!!.toDouble())
        else {
            DialogUtils.show(this, getString(R.string.get_location_error))
        }
    }

    override fun configureMapPosition(clubModel: ClubModel) {
        map_view.getMapAsync {
            it.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(clubModel.latitude!!.toDouble(),
                    clubModel.longitude!!.toDouble()), 15f))
        }
    }

    override fun goToInvite(id: String?) {

        navigationControler?.legacyApp()?.goToInviteFriends(this, id!!)
    }

    override fun configureOnClickListeners() {

        constraintLayoutUber.setOnClickListener {
            presenter.onUberClicked()
        }

        movie_detail_share_button.setOnClickListener {
            presenter.onClickShare()
        }

        clickMap.setOnClickListener {
            presenter.onMapClicked()
        }

        constraintLayoutDirection.setOnClickListener {
            presenter.onMapClicked()
        }

        movie_detail_invite_button.setOnClickListener {

                  presenter.onInviteClicked()
        }
    }

    override fun configureUberButton(clubModel: ClubModel) {

        GpsUtils.getCurrentLocation(this, {
            val uberLatLng = LatLng(it.latitude, it.longitude)

            UberHelper.openUberDeeplink(uberLatLng, LatLng(clubModel.latitude!!.toDouble(),
                    clubModel.longitude!!.toDouble()), clubModel.clubName ?: "",
                    clubModel.address ?: "", this)
        }, {})
    }

    override fun onReceiveTheater(clubModel: ClubModel) {

        Glide.with(this)
                .load(clubModel.clubLogoUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.theater))
                .into(movie_detail_cover_image)

        toolbar.title = clubModel.clubName
        movie_detail_category.text = categoryName
        movie_detail_title.text = clubModel.clubName
        full_address_text.text = clubModel.address
    }

    @SuppressLint("SetTextI18n")
    override fun configureUber(uberInformation: UberInformation) {
        uber_estimated_price.text = "- R$ ${String.format("%.2f", uberInformation.price)}"
        val uberTimeText = getString(R.string.await)
        uber_estimated_time.text = uberTimeText + " " + String.format(resources.getString(R.string.uber_estimate_time_arrival), uberInformation.time)
    }

    override fun shareClub(clubModel: ClubModel) {

        ShareHelper.shareClub(this, clubModel.clubName!!, clubModel.clubId)
    }

    override fun configureSessionComponent(theaterResponse: TheaterResponse) {

        if (!theaterResponse.sessionsByClub.isEmpty()) {
            component_container.visibility = View.VISIBLE
            var sessionComponent = component_container.findViewWithTag<SessionTheaterComponent>(SessionTheaterComponent.TAG)
            if (sessionComponent == null) {
                sessionComponent = SessionTheaterComponent(this, theaterResponse)
                component_container.addView(sessionComponent)
                sessionComponent.onClickListener = { eventInfo ->
                    openBrowser(eventInfo)
                }

                if (firstTime) {
                    firstTime = false
                    sessionComponent.reloadSession(theaterResponse)
                }
            } else {
                sessionComponent.reloadSession(theaterResponse)
            }
        }
    }

    private fun openBrowser(eventInfo: Info) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(eventInfo.originURL))
    }

    @SuppressLint("RestrictedApi")
    override fun showProgress() {
        movie_group.visibility = View.GONE
        movie_detail_category.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE
        movie_detail_like_button.visibility = View.GONE
        movie_detail_share_button.visibility = View.GONE
    }

    @SuppressLint("RestrictedApi")
    override fun hideProgress() {
        movie_detail_category.visibility = View.VISIBLE
        movie_group.visibility = View.VISIBLE
        progress_bar.visibility = View.GONE
        movie_detail_like_button.visibility = View.VISIBLE
        movie_detail_share_button.visibility = View.VISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}