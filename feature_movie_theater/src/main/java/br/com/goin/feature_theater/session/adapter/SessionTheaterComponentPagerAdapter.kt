package br.com.goin.feature_theater.session.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.goin.feature_theater.R
import br.com.goin.feature_theater.model.ClubSessions
import br.com.goin.feature_theater.model.Info
import kotlinx.android.synthetic.main.view_theater_session_component_list.view.*

class SessionTheaterComponentPagerAdapter(val sessionsByEventByDate: List<ClubSessions>) : androidx.viewpager.widget.PagerAdapter() {

    var onClickListener: ((Info) -> Unit)? = null

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return o === view
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.view_theater_session_component_list,
                container, false)

        sessionsByEventByDate[position].let {
            it.events?.let {
                val adapter = SessionTheaterComponentListAdapter(it)
                adapter.onClickListener = {
                    onClickListener?.invoke(it)
                }
                view.recycler_view.adapter = adapter
                view.recycler_view.layoutManager = LinearLayoutManager(container.context)
                view.recycler_view.isNestedScrollingEnabled = false
            }
        }

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return sessionsByEventByDate.size
    }
}