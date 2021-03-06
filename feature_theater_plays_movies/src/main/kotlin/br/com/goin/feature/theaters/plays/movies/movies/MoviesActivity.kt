package br.com.goin.feature.theaters.plays.movies.movies

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.toUpperCaseNoSpace
import br.com.goin.base.extensions.visible
import br.com.goin.feature.theaters.R
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.navigation.NavigationController
import br.com.goin.component.ui.kit.features.error.ErrorViewHelper
import br.com.goin.feature.theaters.plays.movies.MovieTabListener
import br.com.goin.feature.theaters.plays.movies.adapter.MoviesPagerAdapter
import br.com.goin.feature.theaters.plays.movies.adapter.RecommendedPlaysAdapter
import br.com.goin.feature.theaters.plays.movies.model.PlayModel
import br.com.goin.feature.theaters.plays.movies.model.PlaysListModel
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.PlaysMoviesDetailActivity
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.TYPE
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_plays_selection.*
import kotlinx.android.synthetic.main.view_movies_tab.view.*

private const val CATEGORY_NAME = "CATEGORY_NAME"
private const val CATEGORY_ID = "CATEGORY_ID"

class MoviesActivity : AppCompatActivity(), MoviesView {

    private val presenter: MoviesPresenter = MoviesPresenterImpl(this)
    private val navigationController = NavigationController.instance
    private lateinit var movieTabListener: MovieTabListener

    companion object {
        fun starter(context: Context, categoryId: String, name: String) {
            val intent = Intent(context, MoviesActivity::class.java)
            intent.putExtra(CATEGORY_ID, categoryId)
            intent.putExtra(CATEGORY_NAME, name)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plays_selection)

        movieTabListener = MovieTabListener(tab_layout, this)
        presenter.onReceivedCategoryId(intent.extras?.getString(CATEGORY_ID))
        presenter.onCreate()
    }

    override fun onResume() {
        super.onResume()
        Analytics.instance.screenView(this, getString(R.string.movie_screen_name))
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun goToMovieSearch(playType: String) {
        navigationController?.searchModule()?.goToSearch(this, playType)
    }

    override fun configureToolbar(toolbarTitle: String?) {
        setSupportActionBar(toolbar)
        toolbar.setOnBackButtonClicked {
            finish()
        }
        supportActionBar?.title = "${this.getString(R.string.movie_title)} ($toolbarTitle)"
        toolbar.setRightButton(R.drawable.ic_search_orange){
            presenter.onToolbarSearchClicked()
        }
    }

    override fun configureCarousel(highlightedPlays: List<PlayModel>) {
        val adapter = RecommendedPlaysAdapter(highlightedPlays, this)
        horizontal_carousel.adapter = adapter

        highlightedPlays.firstOrNull()?.let {
            recommended_play_item_location?.text = it.city
            recommended_play_item_name?.text = it.name
        }

        horizontal_carousel.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                val playModel = highlightedPlays[horizontal_carousel.realItem]
                recommended_play_item_location?.text = playModel.city
                recommended_play_item_name?.text = playModel.name

                if (playModel.city.isNullOrBlank()) {
                    recommended_play_item_location?.visibility = View.GONE
                }
            }
        })

        adapter.onClickListener = { movie ->
            Analytics.instance.logClickEvent(getString(R.string.detail_category), movie.name?.toUpperCaseNoSpace() ?: "")
            PlaysMoviesDetailActivity.starter(this, movie.id, movie.name, TYPE.MOVIE)
        }
    }

    override fun configureTabs() {
        tab_layout.tabMode = TabLayout.MODE_FIXED
        tab_layout.setupWithViewPager(view_pager)
        tab_layout.setSelectedTabIndicatorColor(Color.TRANSPARENT)

        val inTheatherTab = tab_layout.getTabAt(0)
        val soonTab = tab_layout.getTabAt(1)

        val inTheatherTabView = layoutInflater.inflate(R.layout.view_movies_tab, tab_layout, false)
        val soonTabView = layoutInflater.inflate(R.layout.view_movies_tab, tab_layout, false)

        inTheatherTabView.background = ContextCompat.getDrawable(this, R.drawable.background_movies_tab_left)
        soonTabView.background = ContextCompat.getDrawable(this, R.drawable.background_movies_tab_right)

        inTheatherTab?.customView = inTheatherTabView
        soonTab?.customView = soonTabView

        inTheatherTabView.tv_text.text = getString(R.string.plays_in_theaters)
        soonTabView.tv_text.text = getString(R.string.plays_coming_soon)

        inTheatherTab?.select()
    }

    override fun configureViewPager(model: PlaysListModel) {
        val adapter = MoviesPagerAdapter(model.inTheaters, model.comingSoon)
        adapter.onClickListener = { movie ->
            Analytics.instance.logClickEvent(getString(R.string.detail_category), movie.name?.toUpperCaseNoSpace() ?: "")
            PlaysMoviesDetailActivity.starter(this, movie.id, movie.name, TYPE.MOVIE)
        }
        view_pager.adapter = adapter

        view_pager.removeOnPageChangeListener(movieTabListener)
        view_pager.addOnPageChangeListener(movieTabListener)
    }

    override fun showProgress() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progress_bar.visibility = View.GONE
    }

    override fun handleError(throwable: Throwable){
        ErrorViewHelper.handleErrorView(coordinator_layout, throwable){
            presenter.onCreate()
        }
    }
}
