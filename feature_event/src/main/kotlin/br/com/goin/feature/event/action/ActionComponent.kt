package br.com.goin.feature.event.action

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import br.com.goin.feature.event.BrowserHelper
import br.com.goin.base.utils.GpsUtils
import br.com.goin.component.model.event.Event
import br.com.goin.component.navigation.NavigationController
import br.com.goin.feature.event.R
import kotlinx.android.synthetic.main.view_event_component_action.view.*

import java.util.Calendar

@SuppressLint("ViewConstructor")
class ActionComponent(val activity: Activity, event: Event) : ConstraintLayout(activity), ActionComponentView {

    private var navigationController = NavigationController.instance

    companion object {
        const val TAG = "ActionComponent"
    }

    var onCheckinClick: (() -> Unit)? = null
    var onInviteFriendsClick: (() -> Unit)? = null

    private val presenter: ActionComponentPresenter = ActionComponentPresenterImpl(this)
    private var maxDist = 1000f

    init {
        presenter.onReceiveEventModel(event)
        presenter.onCreate()
        tag = TAG
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.onDestroy()
    }

    override fun configureActions() {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_event_component_action, this, true)
    }

    override fun reloadEventModel(event: Event) {
        presenter.reloadEventModel(event)
    }

    override fun configureCheckin(event: Event) {
        GpsUtils.getCurrentLocation(context!!, { myLocation ->
            if (event.lat != null && event.lng != null) {
                val results = FloatArray(3)
                Location.distanceBetween(myLocation.latitude, myLocation.longitude, event.lat!!, event.lng!!, results)

                if (results[0] <= maxDist) {
                    val currentDate = Calendar.getInstance()

                    if (event.startDate != null && event.endDate != null && event.startDate!!.before(currentDate) && event.endDate!!.after(currentDate))
                        detail_event_checkin_button?.visibility = View.VISIBLE

                    if (results[0] > maxDist) {
                        detail_event_checkin_button?.visibility = View.INVISIBLE
                    } else {
                        val date = Calendar.getInstance()
                        if (event.startDate != null && event.endDate != null && event.startDate!!.before(date) && event.endDate!!.after(date))
                            detail_event_checkin_button?.visibility = View.VISIBLE
                    }
                }
            }

        }, {})
    }

    override fun configureOnClickListeners() {
        detail_event_invite_button.setOnClickListener {
            navigationController?.loginModule()?.goToLogin(context) {
                onInviteFriendsClick?.invoke()
                presenter.onInviteClicked()
            }
        }

        detail_event_checkin_button.setOnClickListener {
            navigationController?.loginModule()?.goToLogin(context){
                onCheckinClick?.invoke()
                presenter.onCheckinClicked()
            }
        }
    }

    fun setBuyButtonClickListener(listener: () -> Unit) {
        buy_ticket.setOnClickListener {
            navigationController?.loginModule()?.goToLogin(context) {
                listener()
            }
        }
    }

    override fun goToInvite(eventId: String) {
        navigationController?.legacyApp()?.goToInviteFriends(context, eventId)
    }

    override fun openAloIngressos(event: Event) {
        event.originURL?.let {
            BrowserHelper.openBrowser(context!!, it)
        }
    }

    override fun openTickets(event: Event) {
        navigationController?.legacyApp()?.goToEventTickets(context, event.id, event.originType!!, null)
    }

    override fun loginInIngresse(event: Event) {
        val url = "https://auth.ingresse.com?publickey=2nf8b62PpC5djVpPrCYvQFVgTaKEcibY&returnUrl=https://www.goin.com.br/PurchaseTicket?originType=${event.originType};;eventId=${event.id}"
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }

    fun hideBuyAction() {
        buy_ticket.visibility = View.GONE
    }

    override fun showCheckingDialog(event: Event) {
        val builder = android.app.AlertDialog.Builder(context)
            builder.setMessage(R.string.confirmation_checkin)
            builder.setPositiveButton(R.string.yes) { _, _ ->
                navigationController?.legacyApp()?.goToPost(activity,eventId= event.id, eventName= event.name)
            }.setNegativeButton(R.string.cancel) { _, _ -> }
        builder.create()
        builder.show()
    }

    override fun showCheckingDisabledDialog(event: Event) {
        val builder = android.app.AlertDialog.Builder(context)
        builder.setMessage(R.string.check_in_disabled_message)
        builder.setPositiveButton(R.string.alert_ok) { _, _ -> }
        builder.create()
        builder.show()
    }
}