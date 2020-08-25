package br.com.goin.feature.event

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.visible
import br.com.goin.base.helpers.ActivityHelper
import br.com.goin.base.helpers.CollapseToolbarHelper
import br.com.goin.base.helpers.GmapHelper
import br.com.goin.base.helpers.ShareHelper
import br.com.goin.base.utils.DateUtils
import br.com.goin.base.utils.GpsUtils
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.navigation.NavigationController
import br.com.goin.component.model.event.Event
import br.com.goin.component.model.event.api.response.ApiVoucher
import br.com.goin.component.session.user.UserModel
import br.com.goin.component.ui.kit.GlideBackgroundFromBitmapListener
import br.com.goin.component.ui.kit.dialog.DialogUtils
import br.com.goin.feature.event.action.ActionComponent
import br.com.goin.feature.event.discountDialog.DiscountDialog
import br.com.goin.component.model.uber.UberHelper
import br.com.goin.component.ui.kit.features.error.ErrorViewHelper
import br.com.goin.feature.event.voucher.EventVouchersAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.uber.sdk.android.core.auth.LoginManager
import iammert.com.expandablelib.Section
import kotlinx.android.synthetic.main.activity_event.*
import kotlinx.android.synthetic.main.claim_voucher_error.*
import kotlinx.android.synthetic.main.claim_voucher_success.*
import kotlinx.android.synthetic.main.confirm_presence_dialog.*
import kotlinx.android.synthetic.main.confirm_presence_dialog.view.*
import kotlinx.android.synthetic.main.dialog_event_voucher.*
import kotlinx.android.synthetic.main.error_presence_dialog.view.*
import kotlinx.android.synthetic.main.success_presence_dialog.view.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val REQUEST_CHECK_SETTINGS = 1
private val permissionsArray: Array<String> = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
private const val requestLocationCode: Int = 1

class EventActivity : AppCompatActivity(), EventView, OnMapReadyCallback {

    private val presenter: EventPresenter = EventPresenterImpl(this)
    private val navigationController = NavigationController.instance
    private var dialogAttendance: AlertDialog? = null
    private var dialogSuccessAttendance: AlertDialog? = null
    private var dialogErrorAttendance: AlertDialog? = null
    var dialogVoucher: AlertDialog? = null

    companion object {
        private const val EVENT_ID = "EVENT_ID"
        private const val CLUB_ID = "CLUB_ID"

        var actionComponent: ActionComponent? = null

        fun starter(eventId: String, clubId: String?, context: Context) {
            val intent = Intent(context, EventActivity::class.java)
            intent.putExtra(EVENT_ID, eventId)
            intent.putExtra(CLUB_ID, clubId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        lifecycle.addObserver(GmapLifecycleObserver(map_view))

        presenter.onReceivedEventId(intent.getSerializableExtra(EVENT_ID))
        presenter.onReceivedClubId(intent.getSerializableExtra(CLUB_ID))

        presenter.onCreate()
        map_view.onCreate(savedInstanceState)

        TransitionManager.beginDelayedTransition(coordinator_layout)
    }

    override fun onResume() {
        super.onResume()
        Analytics.instance.screenView(this, getString(R.string.event_screen_name))
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun shareEvent(event: Event) {
        ShareHelper.shareEvent(this, event.name, event.id)
    }

    override fun openMaps(event: Event) {
        if (event.lat != null && event.lng != null)
            GmapHelper.openMapsInPosition(this, event.lat!!, event.lng!!)
        else {
            DialogUtils.show(this, "Ainda não está disponível as coordenadas desse evento")
        }
    }

    override fun showCannotRemoveLikeDialog() {
        DialogUtils.show(this, getString(R.string.cant_remove_checkin))
    }

    override fun openAloIngressos(event: Event) {
        event.originURL?.let {
            BrowserHelper.openBrowser(this, it)
        }
    }

    override fun openTickets(event: Event) {
        navigationController?.legacyApp()?.goToEventTickets(this, event.id, event.originType!!, null)
    }

    override fun openIngresseRapidoSite(event: Event) {
        event.originURL?.let {
            BrowserHelper.openBrowser(this, it)
        }
    }

    override fun loginInIngresse(event: Event) {
        val url = "https://auth.ingresse.com?publickey=2nf8b62PpC5djVpPrCYvQFVgTaKEcibY&returnUrl=https://www.goin.com.br/PurchaseTicket?originType=${event.originType};;eventId=${event.id}"
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    override fun configureToolbar() {
        setSupportActionBar(toolbar)
        toolbar_left_icon.setOnClickListener {
            presenter.logOnAnalytics(getString(R.string.analytics_event_details_back_action))
            onBackPressed()
        }
        toolbar_title.visibility = View.GONE

        CollapseToolbarHelper.configureTransparentToolbar(app_bar_layout,
                collaping_toolbar_layout,
                toolbar_title)
    }

    override fun configureOnClickListeners() {
        card_confirmation_status.setOnClickListener {
            navigationController?.loginModule()?.goToLogin(this) {
                presenter.logOnAnalytics(getString(R.string.analytics_event_details_favorite_action))
                presenter.onLikeClicked()
            }
        }

        constraintLayoutUber.setOnClickListener {
            presenter.logOnAnalytics(getString(R.string.analytics_event_details_uber_action))
            presenter.onUberClicked()
        }

        share_button.setOnClickListener {
            presenter.logOnAnalytics(getString(R.string.analytics_event_details_share_action))
            presenter.onShareClicked()
        }

        clickMap.setOnClickListener {
            presenter.onMapClicked()
        }

        constraintLayoutDirection.setOnClickListener {
            presenter.logOnAnalytics(getString(R.string.analytics_event_details_waypoint_action))
            presenter.onMapClicked()
        }

        detail_event_tickets_button.setOnClickListener {
            navigationController?.loginModule()?.goToLogin(this) {
                presenter.logOnAnalytics(getString(R.string.analytics_event_details_buy_main_ticket_action))
                presenter.onClickBuyTicket()
            }

        }
    }

    override fun configureUberButton(event: Event) {
        GpsUtils.getCurrentLocation(this, {
            it.latitude.let { latitude ->
                it.longitude.let { longitude ->
                    val uberLatLng = LatLng(latitude, longitude)
                    UberHelper.openUberDeeplink(uberLatLng, LatLng(event.lat!!, event.lng!!), event.placeName
                            ?: "", event.placeAddress ?: "", this)
                }
            }
        }, {})
    }

    override fun configureButtonConfirmLike(isActive: Boolean) {
        card_confirmation_status.isSelected = isActive
    }

    override fun configureVouchers(vouchers: List<ApiVoucher>?) {
        val vouchersAdapter = EventVouchersAdapter(vouchers!!)
        vouchersList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        vouchersList.adapter = vouchersAdapter
        vouchersList.isNestedScrollingEnabled = false
        textViewLabelVouchers.visible()
        vouchersList.visible()

        vouchersAdapter.onClickVoucherListener = {
            navigationController!!.loginModule().goToLogin(this) {
                showDialogVoucher(it)
            }
        }
    }

    private fun showDialogVoucher(apiVoucher: ApiVoucher) {
        dialogVoucher = EventDialogHelper.showVoucherEvent(this, apiVoucher) {
            presenter.claimTicket(it)
        }
    }

    override fun showLoadingVoucher() {

        dialogVoucher!!.progressBarConfirmVoucher.visible()
    }

    override fun hideLoadingVoucher() {
        dialogVoucher!!.progressBarConfirmVoucher.gone()
    }

    override fun claimTicketSuccess(voucher: ApiVoucher) {

        dialogVoucher!!.constraintClaim.gone()
        dialogVoucher!!.constraintVoucherSuccess.visible()

        dialogVoucher!!.btn_access_wallet.setOnClickListener {
            navigationController?.homeModule()?.goToHome(this@EventActivity, "WALLET")
        }
        dialogVoucher!!.btn_close.setOnClickListener { dialogVoucher!!.dismiss() }
        dialogVoucher!!.constraintContainerShare.setOnClickListener {

            ShareHelper.shareEvent(this, voucher.title!!, voucher.giftId!!)
        }
    }

    override fun claimTicketError() {

        dialogVoucher!!.constraintClaim.gone()
        dialogVoucher!!.constraintClaimError.visible()

        dialogVoucher!!.btn_close_error.setOnClickListener { dialogVoucher!!.dismiss() }
    }

    override fun configureEmptyVouchers() {
        textViewLabelVouchers.gone()
        vouchersList.gone()
    }

    override fun configureActionsComponent(event: Event) {
        component_container.removeAllViews()

        actionComponent = ActionComponent(this, event)
        component_container.addView(actionComponent, 0)

        actionComponent?.onCheckinClick = {
            presenter.logOnAnalytics(getString(R.string.analytics_event_details_checkin_action))
        }

        actionComponent?.onInviteFriendsClick = {
            presenter.logOnAnalytics(getString(R.string.analytics_event_details_invite_friends_action))
        }

        actionComponent?.setBuyButtonClickListener {
            navigationController?.loginModule()?.goToLogin(this) {
                presenter.logOnAnalytics(getString(R.string.analytics_event_details_buy_ticket_carousel_action))
                presenter.onClickBuyTicket()
            }
        }
    }

    @SuppressLint("SetTextI18n", "RestrictedApi")
    override fun onEventLoaded(event: Event) {
        toolbar_title.text = event.name

        text_date.text = DateUtils.formatEventDate(event.startDate, event.endDate)
        address_text.text = event.city
        full_address_text.text = event.placeAddress
        card_confirmation_status.isSelected = event.isConfirmed


        txt_month.text = DateUtils.getMonth(event.startDate)
        txt_day.text = DateUtils.getDay(event.startDate)
        event_name.text = event.name
        if (event.club.isNullOrEmpty()) {
            event_place.text = event.placeAddress
        } else {
            event_place.text = event.club
        }

        if (event.lowestPrice != null && event.highestPrice != null) {
            price_text.text = "De ${NumberFormat.getCurrencyInstance().format(event.lowestPrice)} até ${NumberFormat.getCurrencyInstance().format(event.highestPrice)}"
        }
        if (event.lowestPrice != null && event.lowestPrice == event.highestPrice) {
            price_text.text = NumberFormat.getCurrencyInstance().format(event.lowestPrice)
        }
        if (event.lowestPrice != null && event.highestPrice == null) {
            price_text.text = getString(R.string.fromPrice) + NumberFormat.getCurrencyInstance().format(event.lowestPrice)
        }
        if (event.lowestPrice == null && event.highestPrice != null) {
            price_text.text = getString(R.string.fromPrice) + NumberFormat.getCurrencyInstance().format(event.highestPrice)
        }
        if (event.lowestPrice == 0 && event.highestPrice == 0) {
            price_text.text = getString(R.string.free)
        }
        if (event.lowestPrice == null && event.highestPrice == null) {
            price_text.visibility = View.GONE
            ticket_icon.visibility = View.GONE
        }

        if (event.isCorporative()) {
            card_confirmation_status.visibility = View.GONE
        } else {
            card_confirmation_status.visibility = View.VISIBLE
        }

        Glide.with(this)
                .load(event.photoUrl)
                .transition(withCrossFade())
                .listener(GlideBackgroundFromBitmapListener(iv_background))
                .into(detail_event_image)

        if (event.isCorporative()) {
            card_confirmation_status.visibility = View.GONE
        } else {
            card_confirmation_status.visibility = View.VISIBLE
        }
    }

    override fun onMapReady(gMap: GoogleMap?) {
        MapsInitializer.initialize(this)
        gMap?.uiSettings?.isMyLocationButtonEnabled = true
    }

    override fun configureMapPosition(lat: Double, lng: Double) {
        map_view.getMapAsync {
            it.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 15f))
        }
    }

    @SuppressLint("SetTextI18n")
    override fun configureUberPrice(bestPrice: Float) {
        uber_estimated_price.text = "- R$ ${String.format("%.2f", bestPrice)}"
    }

    @SuppressLint("SetTextI18n")
    override fun configureUberTime(time: Int) {
        val finalTime = (time % 3600) / 60
        val uberTimeText = getString(R.string.await)
        uber_estimated_time.text = uberTimeText + " " + String.format(resources.getString(R.string.uber_estimate_time_arrival), finalTime)
    }

    override fun retriveCurrentPosition() {
        GpsUtils.getCurrentLocation(this, {
            presenter.onReceiveLocation(it)
            presenter.loadUberInformation(it.latitude, it.longitude)
        }, {})
    }

    override fun configureBuyButtonByColor(event: Event) {
        DrawableCompat.setTint(detail_event_tickets_button.background, Color.parseColor(event.buttonColor))

        when (event.smallIconWhite) {
            null -> {
                logo_origin.visibility = View.GONE
                divider.visibility = View.GONE
            }
            else -> {
                logo_origin.visibility = View.VISIBLE
                Glide.with(this)
                        .load(event.smallIconWhite)
                        .transition(withCrossFade())
                        .into(logo_origin)
            }
        }
    }

    override fun configureGoInButton() {
        detail_event_tickets_button.background = ContextCompat.getDrawable(this, R.drawable.button_orange_style)
        txtPartnersEvent.visibility = View.VISIBLE
        logo_origin.visibility = View.GONE
        divider.visibility = View.GONE
    }

    override fun showDiscountDialog(event: Event) {
        DiscountDialog(this, event).show()
    }

    override fun showConfirmAttendanceButton() {
        configureGoInButton()
        txtPartnersEvent.visibility = View.VISIBLE
        txtPartnersEvent.text = getString(R.string.attendance_confirmation)
        actionComponent?.visibility = View.GONE
    }

    override fun requestUberInfos(event: Event) {
        GpsUtils.getCurrentLocation(this, { presenter.loadUberInformation(it.latitude, it.longitude) }, {})
    }

    override fun showDialogConfirmedPresenceSuccess() {
        EventHelper.showDialogConfirmedPresenceSuccess(this)
    }

    override fun showErrorOnConfirmAttendance(error: String) {
        EventHelper.showErrorDialog(this, error)
    }

    override fun showDialogNotAuthorized() {
        val dialog = EventHelper.showErrorDialog(this, getString(R.string.you_dont_have_permissions_to_see_event))
        dialog.setOnDismissListener {
            finish()
        }
    }

    override fun showDialogUnconfirmPresence(event: Event) {
        EventHelper.showDialogUnconfirmPresence(card_confirmation_status, event) {
            presenter.onConfirmUnLike()
        }
    }

    override fun showLoading() {
        event_group.gone()
        progress_bar.visible()
        share_button.gone()
        card_confirmation_status.gone()
        detail_event_tickets_button.gone()
    }

    @SuppressLint("RestrictedApi")
    override fun hideLoading(isCorporative: Boolean) {
        event_group.visible()
        progress_bar.gone()
        share_button.visible()
        detail_event_tickets_button.visible()
        if (isCorporative) card_confirmation_status.gone() else card_confirmation_status.visible()
    }

    override fun showButtonProgress() {
        button_progress_bar.visibility = View.VISIBLE
        txtPartnersEvent.visibility = View.GONE
        logo_origin.visibility = View.GONE
        detail_event_tickets_button.isClickable = false
    }

    override fun hideButtonProgress() {
        button_progress_bar.visibility = View.GONE
        presenter.configureBuyButton()
        detail_event_tickets_button.isClickable = true
    }

    override fun hideBuyTicketButton() {
        detail_event_tickets_button.visibility = View.GONE
        actionComponent?.apply {
            hideBuyAction()
        }
    }

    override fun hideShareButton() {
        share_button.gone()
    }

    override fun hideLikeButton() {
        card_confirmation_status.gone()
    }

    override fun verifyGpsEnabled() {
        EventHelper.checkGPSIsActive(this, REQUEST_CHECK_SETTINGS)

        if (!ActivityHelper.hasPermissions(this, permissionsArray)) {
            ActivityCompat.requestPermissions(this, permissionsArray, requestLocationCode)
        } else {
            GpsUtils.getCurrentLocation(this, {
                presenter.onReceiveLocation(it)
            }, {})
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == Activity.RESULT_OK) {
            GpsUtils.getCurrentLocation(this, {
                presenter.onReceiveLocation(it)
                presenter.loadUberInformation(it.latitude, it.longitude)
            }, { })
        }
    }

    override fun configureExpandableLayout(event: Event) {
        EventHelper.settingExpandableLayout(this, expandable_layout)

        try {
            if (expandable_layout.sections.size <= 0) {
                val section = Section<ExpandableParent, ExpandableChild>()
                section.expanded = false

                val category = ExpandableParent(getString(R.string.information_section), R.drawable.evento_info_icon)
                val item1 = ExpandableChild(event.description)

                section.parent = category
                section.children.add(item1)
                expandable_layout.addSection(section)
                expandable_layout.setExpandListener<ExpandableParent> { _, _, _ ->
                    presenter.logOnAnalytics(getString(R.string.analytics_event_details_informations_action))
                }
            }
        } catch (e: Exception) {
            Log.e("EventActivity", e.message, e)
        }
    }

    override fun openPaymentScreen(event: Event) {
        navigationController?.legacyApp()?.goToEventTickets(this, event.id, event.originType!!, null)
    }

    override fun handleError(throwable: Throwable) {
        ErrorViewHelper.handleErrorView(coordinator_layout, throwable) {
            presenter.onCreate()
        }
    }

    override fun logOnAnalytics(action: String, eventName: String) {
    }

    override fun configureEventAttendance(eventType: String?, eventId: String?, user: UserModel?, event: Event?) {
        actionComponent?.hideBuyAction()
        detail_event_tickets_button.setOnClickListener {
            showConfirmAttendanceDialog(eventId, user, event)
        }
        txtPartnersEvent.text = getString(R.string.go_to_party)
        if (logo_origin != null) {
            logo_origin.gone()
        }
        if (divider != null) {
            divider.gone()
        }
    }

    @SuppressLint("InflateParams")
    private fun showConfirmAttendanceDialog(eventId: String?, user: UserModel?, event: Event?) {
        val inflater = this@EventActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.confirm_presence_dialog, null)

        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 40)

        dialogAttendance = AlertDialog.Builder(ContextThemeWrapper(this@EventActivity, R.style.PresenceDialog)).create()
        dialogAttendance?.window?.setBackgroundDrawable(inset)
        dialogAttendance?.setView(dialogView)
        dialogAttendance?.show()

        dialogView.textViewTitleEvent.text = event?.name

        event?.let {
            dialogView.textViewSubTitle.text = if (event.startDate != null) {
                val shortDate = DateUtils.formatEventDate(event.startDate, event.endDate).toUpperCase()
                "$shortDate"
            } else {
                ""
            }
        }

        dialogView.textViewLabelRegulation.text = user?.name
        dialogView.confirmVoucherList.setOnClickListener {

            navigationController!!.loginModule().goToLogin(this){

                dialogView.progress_bar_presence.visible()
                dialogView.confirmVoucherList.gone()
                presenter.onAttendanceConfirm(eventId, user?.name)
            }
        }
    }

    override fun onSuccessAttendance() {
        dialogAttendance?.dismiss()

        val inflater = this@EventActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.success_presence_dialog, null)

        dialogView.btn_access_wallet.setOnClickListener {
            navigationController?.homeModule()?.goToHome(this@EventActivity, "WALLET")
        }

        dialogView.share_success.setOnClickListener { presenter.onShareClicked() }
        dialogView.btn_close.setOnClickListener { dialogSuccessAttendance?.dismiss() }

        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 40)

        dialogSuccessAttendance = AlertDialog.Builder(ContextThemeWrapper(this@EventActivity, R.style.PresenceDialog)).create()
        dialogSuccessAttendance?.window?.setBackgroundDrawable(inset)
        dialogSuccessAttendance?.setView(dialogView)
        dialogSuccessAttendance?.show()
    }

    override fun loadingNameOnList() {
        if (confirmVoucherList != null) {
            confirmVoucherList.gone()
        }
        if (progress_bar_presence != null) {
            progress_bar_presence.visible()
        }
    }

    override fun loadingNameOnListFinished() {
        if (confirmVoucherList != null) {
            confirmVoucherList.visible()
        }
        if (progress_bar_presence != null) {
            progress_bar_presence.gone()
        }
    }

    override fun onErrorAttendance(errorMessage: String) {
        if (dialogSuccessAttendance != null) {
            dialogSuccessAttendance?.dismiss()
        }
        if (dialogAttendance != null) {
            dialogAttendance?.dismiss()
        }

        val inflater = this@EventActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.error_presence_dialog, null)

        dialogView.error_msg.text = errorMessage

        dialogErrorAttendance = AlertDialog.Builder(this)
                .setPositiveButton(getString(R.string.close)) { dialog, _ -> dialog.dismiss() }.create()

        dialogErrorAttendance?.setView(dialogView)
        dialogErrorAttendance?.show()
    }
}
