package br.com.goin.feature.theaters.plays.movies

import android.content.Context
import android.view.View
import androidx.viewpager.widget.ViewPager
import br.com.goin.base.extensions.toUpperCaseNoSpace
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.feature.theaters.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_plays_selection.*
import kotlinx.android.synthetic.main.view_movies_tab.view.*

class MovieTabListener(val tab_layout: TabLayout, val context: Context): ViewPager.OnPageChangeListener{
    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        val tab = tab_layout.getTabAt(position)
        val text = tab?.customView?.tv_text?.text.toString().toUpperCaseNoSpace()
        Analytics.instance.logClickEvent(context.getString(R.string.filter_category), text)
    }

}