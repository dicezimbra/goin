package br.com.goin.feature.theaters.plays.movies.playsMoviesDetail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.goin.feature.theaters.R
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.session.component.SessionComponent
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.model.SessionsByEvent
import com.bumptech.glide.Glide
import androidx.core.graphics.drawable.DrawableCompat
import androidx.transition.TransitionManager
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.about.MovieAboutActivity
import br.com.goin.base.helpers.CollapseToolbarHelper
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.visible
import br.com.goin.component.navigation.NavigationController
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.ui.kit.dialog.DialogUtils
import br.com.goin.component.ui.kit.features.error.ErrorViewHelper
import kotlinx.android.synthetic.main.activity_movie_detail.*

enum class TYPE{
    THEATER, MOVIE
}

class PlaysMoviesDetailActivity : AppCompatActivity(), PlaysMoviesDetailView {

    private var firstTime: Boolean = true
    private val presenter: PlaysMoviesDetailPresenter = PlaysMoviesDetailPresenterImpl(this)
    private val navegationController = NavigationController.instance

    companion object {
        private const val PLAY_ID = "PLAY_ID"
        private const val MOVIE_TITLE = "MOVIE_TITLE"
        private const val TYPE_PARAM = "TYPE_PARAM"

        fun starter(context: Context, playId: String?, movieTitle: String?, type: String) {
            val intent = Intent(context, PlaysMoviesDetailActivity::class.java)
            intent.putExtra(PLAY_ID, playId)
            intent.putExtra(MOVIE_TITLE, movieTitle)
            intent.putExtra(TYPE_PARAM, TYPE.valueOf(type))
            context.startActivity(intent)
        }

        fun starter(context: Context, playId: String?, movieTitle: String?, type: TYPE) {
            val intent = Intent(context, PlaysMoviesDetailActivity::class.java)
            intent.putExtra(PLAY_ID, playId)
            intent.putExtra(MOVIE_TITLE, movieTitle)
            intent.putExtra(TYPE_PARAM, type)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        TransitionManager.beginDelayedTransition(coordinator_layout)

        firstTime = true
        presenter.onReceivedPlayId(intent?.extras?.getString(PLAY_ID))
        presenter.onReceivedType(intent?.extras?.getSerializable(TYPE_PARAM))
        presenter.onCreate()
    }

    override fun onResume() {
        super.onResume()
        presenter.logScreenView()
    }

    override fun configureToolbar() {
        setSupportActionBar(toolbar)
        toolbar_left_icon.setOnClickListener {
            onBackPressed()
        }

        toolbar_title.visibility = View.GONE
        CollapseToolbarHelper.configureTransparentToolbar(app_bar_layout, collaping_toolbar_layout, toolbar_title)
    }

    override fun configureOnClickListeners() {
        movie_detail_like_button.setOnClickListener {
            presenter.onClickLike()
        }

        movie_detail_share_button.setOnClickListener {
            presenter.onClickShare()
        }

        movie_detail_about.setOnClickListener{
            presenter.onClickAbout()
        }
    }

    override fun goToMovieAbout(event: br.com.goin.component.model.event.Event, type: TYPE) {
        MovieAboutActivity.starter(this, event, type)
    }

    override fun goToInvite(id: String?) {
        id?.let{
            navegationController?.legacyApp()?.goToInviteFriends(this, it)
        }
    }

    override fun onReceiveEventDetails(event: br.com.goin.component.model.event.Event) {
        Glide.with(this)
                .load(event.photoUrl)
                .thumbnail(0.5f)
                .into(movie_detail_cover_image)

        toolbar_title.text = event.name
        movie_detail_title.text = event.name
        movie_detail_like_button.isSelected = event.isConfirmed

        movie_detail_category.text = ""
        movie_detail_duration.text = event.information?.duration ?: ""

        if(event.information?.parentalRating.isNullOrBlank()){
            movie_detail_parental_control.gone()
        }else {
            movie_detail_parental_control.visible()
            movie_detail_parental_control.text = event.information?.parentalRating ?: ""
            DrawableCompat.setTint(movie_detail_parental_control.background, Color.parseColor(event.buttonColor))
        }

        if(event.description.isNullOrBlank()){
            movie_detail_about.visibility = View.GONE
        }

        movie_detail_invite_button.setOnClickListener {
            navegationController?.loginModule()?.goToLogin(this) {
                presenter.onInviteClicked()
            }
        }
    }

    override fun showCannotRemoveLikeDialog() {
        DialogUtils.show(this, getString(R.string.cant_remove_checkin))
    }

    override fun configureButtonConfirmPresence(event: br.com.goin.component.model.event.Event) {
        event.isConfirmed = true
        movie_detail_like_button.isSelected = true
    }

    override fun showDialogUnconfirmPresence(event: br.com.goin.component.model.event.Event) {
        val alert = AlertDialog.Builder(this)
        alert.setMessage(R.string.unfollow_event_question)
        alert.setPositiveButton(R.string.yes) { dialog, _ ->
            event.isConfirmed = false

            movie_detail_like_button.isSelected = false
            dialog.dismiss()
            presenter.onConfirmUnconfirmPresence()
        }

        alert.setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }
        alert.show()
    }

    override fun shareEvent(event: br.com.goin.component.model.event.Event) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Veja o evento: https://www.goin.com.br/PlayDetail?playId=" + event.id)
        shareIntent.type = "text/plain"
        startActivity(Intent.createChooser(shareIntent, "Convide seus amigos usando:"))
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun configureSessionComponent(sessionsByEvent: SessionsByEvent) {
        var sessionComponent = component_container.findViewWithTag<SessionComponent>(SessionComponent.TAG)
        if (sessionComponent == null) {
            sessionComponent = SessionComponent(this, sessionsByEvent, supportFragmentManager)
            component_container.addView(sessionComponent)
            if(firstTime){
                firstTime = false
                sessionComponent.reloadSession(sessionsByEvent)
            }
        } else {
            sessionComponent.reloadSession(sessionsByEvent)
        }
    }

    @SuppressLint("RestrictedApi")
    override fun showProgress() {
        movie_group.visibility = View.GONE
        movie_detail_category.visibility = View.GONE
        movie_detail_parental_control.visibility = View.GONE
        movie_detail_about.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE
        movie_detail_like_button.visibility = View.GONE
        movie_detail_share_button.visibility = View.GONE
        movie_detail_invite_button.visibility = View.GONE
    }

    @SuppressLint("RestrictedApi")
    override fun hideProgress() {
        movie_detail_category.visibility = View.VISIBLE
        movie_group.visibility = View.VISIBLE
        movie_detail_about.visibility = View.VISIBLE
        progress_bar.visibility = View.GONE
        movie_detail_like_button.visibility = View.VISIBLE
        movie_detail_share_button.visibility = View.VISIBLE
        movie_detail_invite_button.visibility = View.VISIBLE
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun handleError(throwable: Throwable){
        ErrorViewHelper.handleErrorView(coordinator_layout, throwable){
            presenter.onCreate()
        }
    }

    override fun logScreenViewMovie() {
        Analytics.instance.screenView(this, getString(R.string.movie_detail_screen_view))
    }

    override fun logScreenViewTheater() {
        Analytics.instance.screenView(this, getString(R.string.theater_detail_screen_view))
    }
}
