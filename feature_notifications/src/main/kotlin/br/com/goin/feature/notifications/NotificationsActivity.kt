package br.com.goin.feature.notifications

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.visible
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.navigation.NavigationController
import br.com.goin.feature.notifications.model.Notification
import kotlinx.android.synthetic.main.activity_notifications.*

class NotificationsActivity: AppCompatActivity(), NotificationsView{

    private val presenter: NotificationsPresenter = NotificationsPresenterImpl(this)
    private val navigationController = NavigationController.instance
    private var adapter: NotificationsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        setSupportActionBar(toolbar)
        toolbar.setOnBackButtonClicked { finish() }

        presenter.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        Analytics.instance.screenView(this, getString(R.string.notification_screen_name))
    }

    override fun configureRecyclerView(){
        adapter = NotificationsAdapter()

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter

        val animation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_fall_down)
        recycler_view.layoutAnimation = animation

        adapter?.apply {
            onFollowClicked = {
                presenter.onFollowClick(it)
            }

            onAvatarClicked = {
                it.destinationId?.let { profileId ->
                    navigationController?.profileModule()?.goToProfile(this@NotificationsActivity, profileId)
                }
            }

            onNotificationClicked = {
                navigationController?.let {  navigationController ->
                    NotificationHelper.goToDeeplink(it, navigationController, this@NotificationsActivity)
                }
            }
        }
    }

    override fun showUnfollowDialog(notification: Notification){
        NotificationHelper.showUnfollowAlert(this@NotificationsActivity, notification.userName){
            presenter.onConfirmUnfollow(notification)
        }
    }

    override fun updateNotifications(){
        adapter?.notifyDataSetChanged()
    }

    override fun configureNotifications(notiications: List<Notification>){
        adapter?.setNotifications(notiications)
    }

    override fun configureEmptyNotificaitons(){
        progress_bar.gone()
        no_notifications.visible()
        recycler_view.gone()
    }

    override fun showLoading(){
        progress_bar.visible()
    }

    override fun hideLoading(){
        progress_bar.gone()
    }
}