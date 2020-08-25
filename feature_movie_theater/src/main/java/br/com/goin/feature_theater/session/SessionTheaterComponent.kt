package br.com.goin.feature_theater.session

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import br.com.goin.feature_theater.R
import br.com.goin.feature_theater.model.ClubSessions
import br.com.goin.feature_theater.model.Info
import br.com.goin.feature_theater.model.TheaterResponse
import br.com.goin.feature_theater.session.adapter.SessionTheaterComponentPagerAdapter
import kotlinx.android.synthetic.main.view_theater_session_component.view.*
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.view_theater_session_component_tab.view.*

class SessionTheaterComponent(context: Context, theaterResponse: TheaterResponse) : LinearLayout(context), SessionTheaterComponentView {

    companion object {
        const val TAG = "SessionComponent"
    }

    private val presenter: SessionTheaterComponentPresenter = SessionTheaterComponentPresenterImpl(this)
    var onClickListener: ((Info) -> Unit)? = null

    init {
        presenter.onReceivedSessions(theaterResponse)
        presenter.onCreate()
        tag = TAG
    }

    override fun reloadSession(theaterResponse: TheaterResponse) {
        presenter.reloadSession(theaterResponse)
    }

    override fun configureSession(clubSessions: List<ClubSessions>) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.view_theater_session_component, this, true)

        view.textViewLabelBuy.visibility = View.VISIBLE

        val adapter = SessionTheaterComponentPagerAdapter(clubSessions)
        adapter.onClickListener = onClickListener
        view.view_pager.adapter = adapter
        view.tab_layout.tabMode = TabLayout.MODE_SCROLLABLE
        view.tab_layout.setupWithViewPager(view.view_pager)
        view.tab_layout.setSelectedTabIndicatorColor(Color.TRANSPARENT)

        configureTabs(view, inflater, clubSessions)
    }

    private fun configureTabs(view: View, inflater: LayoutInflater, days: List<ClubSessions>) {
        for (i in 0 until view.tab_layout.tabCount) {
            val tab = view.tab_layout.getTabAt(i)
            if (tab != null) {
                val tabView = inflater.inflate(R.layout.view_theater_session_component_tab, this, false)
                tab.customView = tabView

                tabView.tv_day.text = days[i].date
                tabView.tv_date.text = days[i].week
            }
        }

        if (view.tab_layout.tabCount == 0)
            view.textViewLabelBuy.visibility = View.GONE
        else
            view.textViewLabelBuy.visibility = View.VISIBLE

        view.tab_layout.getTabAt(0)?.select()
    }
}