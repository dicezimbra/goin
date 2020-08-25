package br.com.goin.base.helpers

import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

object CollapseToolbarHelper {

    fun configureTransparentToolbar(
            app_bar_layout: AppBarLayout,
            collaping_toolbar_layout: CollapsingToolbarLayout,
            toolbar: Toolbar,
            title: String
    ) {

        collaping_toolbar_layout.title = " "
        app_bar_layout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    toolbar.title = title
                    isShow = true
                } else if (isShow) {
                    toolbar.title = ""
                    isShow = false
                }
            }
        })
    }

    fun configureTransparentToolbar(
            app_bar_layout: AppBarLayout,
            collaping_toolbar_layout: CollapsingToolbarLayout,
            toolbar_title: TextView
    ) {

        collaping_toolbar_layout.title = " "
        app_bar_layout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    toolbar_title.visibility = View.VISIBLE
                    isShow = true
                } else if (isShow) {
                    toolbar_title.visibility = View.GONE
                    isShow = false
                }
            }
        })
    }

    fun configureTransparentView(
            app_bar_layout: AppBarLayout,
            collaping_toolbar_layout: CollapsingToolbarLayout,
            view: View) {

        collaping_toolbar_layout.title = " "
        app_bar_layout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    view.visibility = View.GONE
                    isShow = true

                } else if (isShow) {
                    view.visibility = View.VISIBLE
                    isShow = false
                }
            }
        })
    }
}