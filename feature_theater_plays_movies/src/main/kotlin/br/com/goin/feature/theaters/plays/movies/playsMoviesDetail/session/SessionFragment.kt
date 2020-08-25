package br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.session

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.goin.base.extensions.toUpperCaseNoSpace
import br.com.goin.feature.theaters.R
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.model.EventSessionDetailInfo
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.model.EventSessions
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.session.adapter.SessionComponentListAdapter
import br.com.goin.base.helpers.GmapHelper
import br.com.goin.base.utils.GpsUtils
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.session.user.location.UserLocation
import br.com.goin.component.ui.kit.dialog.DialogUtils
import br.com.goin.feature.theaters.plays.movies.UberHelper
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.view_event_session_component_list.*

private const val EVENT_SESSIONS = "EVENT_SESSIONS"

class SessionFragment: Fragment(), SessionView{

    companion object {
        fun newInstance(eventSessions: EventSessions): SessionFragment{
            val bundle = Bundle()
            bundle.putSerializable(EVENT_SESSIONS, eventSessions)

            val fragment = SessionFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val presenter: SessionPresenter = SessionPresenterImpl(this)
    private var adapter: SessionComponentListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.view_event_session_component_list, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            presenter.onReceivedSerializable(it.getSerializable(EVENT_SESSIONS))
        }

        presenter.onCreate()
    }

    override fun configureRecyclerView(sessions: List<Session>){
        adapter = SessionComponentListAdapter(sessions)
        adapter?.onClickSession = { session, detail ->
            val value = "${session.date}_${detail.hour}"
            Analytics.instance.logClickEvent(getString(R.string.buy_category), value)
            openBrowser(detail)
        }

        adapter?.onClickMap = {
            openMaps(it)
        }

        adapter?.onClickUber = {
            presenter.onUberClicked(it)
        }

        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(context!!)
        recycler_view.isNestedScrollingEnabled = false
    }

    override fun notifyAdapterPosiiton(position: Int){
        adapter?.notifyItemChanged(position)
    }

    private fun openBrowser(eventInfo: EventSessionDetailInfo) {
        context?.let { context ->
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(context, Uri.parse(eventInfo.originURL))
        }
    }

    override fun openMaps(eventSession: Session) {
        if(eventSession.lat != null && eventSession.lng != null)
            GmapHelper.openMapsInPosition(context!!, eventSession.lat!!, eventSession.lng!!)
        else{
            DialogUtils.show(activity!!, getString(R.string.coordinates_unavailable))
        }
    }

    override fun openUber(eventSession: Session, userLocation: UserLocation) {
        if(eventSession.lat != null && eventSession.lng != null) {
            GpsUtils.getCurrentLocation(activity!!, {
                val currentLatLng = LatLng(userLocation.lat, userLocation.lng)
                val eventLatLng = LatLng(eventSession.lat!!, eventSession.lng!!)
                UberHelper.openUberDeeplink(currentLatLng, eventLatLng, eventSession.clubName, eventSession.clubName, context!!)
            }, {})
        }
    }
}