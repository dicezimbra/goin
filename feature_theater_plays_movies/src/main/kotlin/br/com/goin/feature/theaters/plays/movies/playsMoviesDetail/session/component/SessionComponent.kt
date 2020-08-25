package br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.session.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import br.com.goin.feature.theaters.R
import kotlinx.android.synthetic.main.view_event_session_component.view.*
import kotlinx.android.synthetic.main.view_event_session_component_tab.view.*
import com.google.android.material.tabs.TabLayout
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.visible
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.model.EventSessionDetailInfo
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.model.EventSessions
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.model.SessionsByEvent
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.session.adapter.SessionComponentPagerAdapter

@SuppressLint("ViewConstructor")
class SessionComponent(context: Context, movieSessionsByEvent: SessionsByEvent, private val fragmentManager: FragmentManager) : LinearLayout(context), SessionComponentView {

    companion object {
        const val TAG = "SessionComponent"
    }

    private val presenter: SessionComponentPresenter = SessionComponentPresenterImpl(this)
    var onClickListener: ((EventSessionDetailInfo) -> Unit)? = null
    var onTabClickListener: ((day: String) -> Unit)? = null

    init {
        presenter.onReceivedSessions(movieSessionsByEvent)
        presenter.onCreate()
        tag = TAG
    }

    override fun reloadSession(movieSessionsByEvent: SessionsByEvent) {
        presenter.reloadSession(movieSessionsByEvent)
    }

    override fun configureSession(sessionsByEvent: List<EventSessions>) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.view_event_session_component, this, true)

        val adapter = SessionComponentPagerAdapter(fragmentManager, sessionsByEvent)
        view.view_pager.adapter = adapter
        view.tab_layout.tabMode = TabLayout.MODE_SCROLLABLE
        view.tab_layout.setupWithViewPager(view.view_pager)
        view.tab_layout.setSelectedTabIndicatorColor(Color.TRANSPARENT)

        view.tab_layout.addOnTabSelectedListener(object: TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                sessionsByEvent[tab?.position ?: 0].date?.let {
                    onTabClickListener?.invoke(it)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {  }

            override fun onTabUnselected(tab: TabLayout.Tab?) {  }
        } )

        configureTabs(view, inflater, sessionsByEvent)
        if(sessionsByEvent.isNotEmpty()) label_pick_time.visible() else label_pick_time.gone()
    }

    private fun configureTabs(view: View, inflater: LayoutInflater, days: List<EventSessions>) {
        for (i in 0 until view.tab_layout.tabCount) {
            val tab = view.tab_layout.getTabAt(i)
            if (tab != null) {
                val tabView = inflater.inflate(R.layout.view_event_session_component_tab, this, false)
                tab.customView = tabView

                tabView.tv_day.text = days[i].date
                tabView.tv_date.text = days[i].week
            }
        }

        view.tab_layout.getTabAt(0)?.select()
    }
}