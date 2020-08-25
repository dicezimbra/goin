package br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.about

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import br.com.goin.feature.theaters.R
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.model.event.Event
import br.com.goin.base.helpers.CollapseToolbarHelper
import br.com.goin.base.utils.Constants
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.TYPE
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_movie_about.*

class MovieAboutActivity : AppCompatActivity(), MovieAboutView {

    companion object {

        private const val TYPE_PARAM = "TYPE_PARAM"

        fun starter(context: Context, movieTitle: String, movieDetailCategory: String,
                    movieDetailParentalControl: String, movieDetailDuration: String,
                    movieAbout: String, urlImage: String) {

            val intent = Intent(context, MovieAboutActivity::class.java)
            intent.putExtra(Constants.MOVIE_DETAIL_TITLE, movieTitle)
            intent.putExtra(Constants.MOVIE_DETAIL_CATEGORY, movieDetailCategory)
            intent.putExtra(Constants.MOVIE_DETAIL_PARENTAL_CONTROL, movieDetailParentalControl)
            intent.putExtra(Constants.MOVIE_DETAIL_DURATION, movieDetailDuration)
            intent.putExtra(Constants.MOVIE_DETAIL_ABOUT, movieAbout)
            intent.putExtra(Constants.MOVIE_DETAIL_URL_IMAGE, urlImage)
            context.startActivity(intent)
        }

        fun starter(context: Context, event: Event, type: TYPE) {
            val intent = Intent(context, MovieAboutActivity::class.java)
            intent.putExtra(Constants.EVENT, event)
            intent.putExtra(TYPE_PARAM, type)
            context.startActivity(intent)
        }
    }

    private val presenter: MovieAboutPresenter = MovieAboutPresenterImpl(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_about)

        presenter.onReceiveType(intent?.extras?.getSerializable(TYPE_PARAM))

        var event: Event?

        if (intent?.extras?.get(Constants.EVENT) != null) {
            event = intent?.extras?.get(Constants.EVENT) as Event
            event.buttonColor = "#16559F"
        } else {

            event = Event()
            event.name = intent.getStringExtra(Constants.MOVIE_DETAIL_TITLE)
            event.categoryName = intent.getStringExtra(Constants.MOVIE_DETAIL_CATEGORY)
            event.description = intent.getStringExtra(Constants.MOVIE_DETAIL_ABOUT)
            event.photoUrl = intent.getStringExtra(Constants.MOVIE_DETAIL_URL_IMAGE)
            event.buttonColor = "#16559F"
        }

        presenter.onReceiveEvent(event)
        presenter.onCreate()
    }

    override fun onResume() {
        super.onResume()
        presenter.logScreenName()
    }

    override fun configureToolbar() {
        toolbar.setOnBackButtonClicked {
            onBackPressed()
        }
    }

    override fun configureEventView(event: Event) {
        Glide.with(this)
                .load(event.photoUrl)
                .into(movie_detail_cover_image)

        CollapseToolbarHelper.configureTransparentToolbar(app_bar_layout,
                collaping_toolbar_layout,
                toolbar, event.name)

        movie_detail_title.text = event.name
        movie_about.text = event.description

        event.buttonColor?.let {
            DrawableCompat.setTint(movie_detail_parental_control.background, Color.parseColor(event.buttonColor))
        }
    }

    override fun logScreenNameMovie() {
        Analytics.instance.screenView(this, getString(R.string.movie_about_screen_name))
    }

    override fun logScreenNameTheater() {
        Analytics.instance.screenView(this, getString(R.string.theater_about_screen_name))
    }
}