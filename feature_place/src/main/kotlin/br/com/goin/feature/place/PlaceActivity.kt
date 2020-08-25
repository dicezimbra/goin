package br.com.goin.feature.place

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.goin.base.extensions.dpToPx
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.visible
import br.com.goin.base.helpers.CollapseToolbarHelper
import br.com.goin.base.helpers.GmapHelper
import br.com.goin.base.helpers.ShareHelper
import br.com.goin.base.utils.GpsUtils
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.model.event.Event
import br.com.goin.component.model.uber.UberHelper
import br.com.goin.component.navigation.NavigationController
import br.com.goin.component.ui.kit.features.error.ErrorViewHelper
import br.com.goin.component_model_club.model.ClubModel
import br.com.goin.feature.R.*
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_place.*
import kotlinx.android.synthetic.main.view_place_address.*
import kotlinx.android.synthetic.main.view_place_events.*
import kotlinx.android.synthetic.main.view_place_gallery.*
import kotlinx.android.synthetic.main.view_place_info.*
import kotlinx.android.synthetic.main.view_place_vouchers.*
import androidx.appcompat.app.AlertDialog
import br.com.goin.component.model.event.api.response.ApiVoucher
import kotlinx.android.synthetic.main.dialog_voucher.view.*
import android.graphics.Typeface
import android.text.style.TextAppearanceSpan
import android.content.res.ColorStateList
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.method.LinkMovementMethod.*
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.core.content.ContextCompat
import br.com.goin.component.session.user.UserModel
import br.com.goin.component_deep_link.branchIo.BranchIOHelper
import br.com.goin.feature.R
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.claim_voucher_error.*
import kotlinx.android.synthetic.main.claim_voucher_success.*
import kotlinx.android.synthetic.main.dialog_voucher.*

class PlaceActivity : AppCompatActivity(), PlaceView {

    private val presenter: PlacePresenter = PlacePresenterImpl(this)
    private var navigationController = NavigationController.instance
    private var eventAdapter: PlaceEventAdapter? = null
    private var vouchersAdapter: VouchersAdapter? = null
    var alertBuilder: AlertDialog? = null

    companion object {
        private const val PLACE_ID = "PLACE_ID"
        private const val CATEGORY_NAME = "CATEGORY_NAME"

        fun starter(context: Context, placeId: String, categoryName: String = "") {
            val intent = Intent(context, PlaceActivity::class.java)
            intent.putExtra(PLACE_ID, placeId)
            intent.putExtra(CATEGORY_NAME, categoryName)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)
        presenter.onReceivePlaceId(intent.getStringExtra(PLACE_ID))
        presenter.onReceiveCategoryName(intent.getStringExtra(CATEGORY_NAME))
        presenter.onCreate()

        lifecycle.addObserver(GmapLifecycleObserver(map_view))
        map_view.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        Analytics.instance.screenView(this, getString(string.place_screen_name))
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun openMaps(it: ClubModel) {
        if (it.latitude != null && it.longitude != null) {
            GpsUtils.getCurrentLocation(this, { myLocation ->
                GmapHelper.openMapsRoute(this, myLocation.latitude, myLocation.longitude, it.latitude, it.longitude)
            }, {})
        }
    }

    override fun retriveCurrentPosition() {
        GpsUtils.getCurrentLocation(this, {
            presenter.onReceiveLocation(it)
        }, {})
    }

    override fun configureToolbar(title: String) {
        setSupportActionBar(toolbar)
        toolbar.setBackButtonWhite()
        toolbar.setOnBackButtonClicked { finish() }
        toolbar.title = ""

        CollapseToolbarHelper.configureTransparentView(app_bar,
                collapse_toolbar_layout,
                title_container)
    }

    override fun configureOnClicks(club: ClubModel) {
        button_interrest.setOnClickListener {
            navigationController?.loginModule()?.goToLogin(this) {
                presenter.onFollowClicked()
            }
        }

        button_checkin.setOnClickListener {
            navigationController?.loginModule()?.goToLogin(this) {
                presenter.onCheckinClicked()
            }
        }

        button_send.setOnClickListener {
            ShareHelper.sharePlace(this, club.clubName ?: "", club.clubId)
        }

        constraintLayoutAddress.setOnClickListener {
            presenter.onMapClicked()
        }

        constraintLayoutDirection.setOnClickListener {
            presenter.onMapClicked()
        }

        constraintLayoutUber.setOnClickListener {
            GpsUtils.getCurrentLocation(this, {
                if (club.latitude != null && club.longitude != null) {
                    val uberLatLng = LatLng(it.latitude, it.longitude)
                    val userLatLng = LatLng(club.latitude!!.toDouble(), club.longitude!!.toDouble())
                    UberHelper.openUberDeeplink(uberLatLng, userLatLng, club.clubName
                            ?: "", club.address ?: "", this)
                }
            })
        }

        imageViewStarEvaluate.setOnClickListener {
            navigationController?.loginModule()?.goToLogin(this) {
                navigationController?.placeModule()?.goToPlaceRatings(this, club.clubId)
            }
        }

        textViewEvaluateRanking.setOnClickListener {
            navigationController?.loginModule()?.goToLogin(this) {
                navigationController?.placeModule()?.goToPlaceRatings(this, club.clubId)
            }
        }
    }

    override fun configurePlace(club: ClubModel, categoryName: String?) {
        view_info.visible()
        view_address.visible()
        place_info_txt.visible()

        if(club.priceRating != null && club.priceRating!! > 0) {
            club_money.text = "$".repeat(club.priceRating!!)
        }

        if(club.tags != null && club.tags!!.isNotEmpty()){
            club_tags.visible()
            tag.visible()
            club_tags.text = club.tags!!.map { it.name?.toLowerCase() }.joinToString(separator = ", ")
        }
        club_name.text = club.clubName
        textViewEvaluateRanking.text = String.format("%.1f", club.rating)
        full_address_text.text = club.address
        club_categories.text = categoryName
        button_interrest.isSelected = club.isFollowing

        cover_container.visible()
        val logoUrl = if(!club.clubLogoUrl.isNullOrBlank()) {
            club.clubLogoUrl
        }else{
            club.coverImage
        }

        Glide.with(this)
                .load(logoUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(club_logo)

        Glide.with(this)
                .load(club.coverImage)
                .into(club_cover_image)
    }

    override fun configureEvents(events: List<Event>) {
        view_events.visible()
        eventAdapter = PlaceEventAdapter(events)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        layoutManager.initialPrefetchItemCount = 4

        events_list.isNestedScrollingEnabled = false
        events_list.adapter = eventAdapter
        events_list.layoutManager = layoutManager
        eventAdapter?.onClickEventListener = {
            openEvent(it)
        }
    }

    override fun configureVouchers(vouchers: List<ApiVoucher>?) {
        vouchersAdapter = VouchersAdapter(vouchers!!)
        vouchersList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        vouchersList.adapter = vouchersAdapter
        vouchersList.isNestedScrollingEnabled = false
        view_vouchers.visible()

        vouchersAdapter?.onClickVoucherListener = {
            navigationController?.loginModule()?.goToLogin(this) {
                showVoucherDialog(it)
            }
        }
    }

    override fun claimTicketSuccess(voucher: ApiVoucher) {
        alertBuilder!!.constraintClaim.gone()
        alertBuilder!!.constraintVoucherSuccess.visible()

        alertBuilder!!.btn_access_wallet.setOnClickListener {
            navigationController?.homeModule()?.goToHome(this@PlaceActivity, "WALLET")
        }
        alertBuilder!!.btn_close.setOnClickListener { alertBuilder!!.dismiss() }
        alertBuilder!!.constraintContainerShare.setOnClickListener {
            ShareHelper.shareEvent(this, voucher.title!!, voucher.giftId!!)
        }
    }

    override fun claimTicketError() {
        alertBuilder?.constraintClaim?.gone()
        alertBuilder?.constraintClaimError?.visible()
        alertBuilder?.btn_close_error?.setOnClickListener { alertBuilder!!.dismiss() }
    }

    private fun showVoucherDialog(apiVoucher: ApiVoucher) {
        alertBuilder = AlertDialog.Builder(this).create()
        val view = layoutInflater.inflate(R.layout.dialog_voucher, null)
        alertBuilder?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        Glide.with(this)
                .load(apiVoucher.image)
                .into(view.imageViewDialogVoucher)

        val title = "${apiVoucher.title} ${apiVoucher.subtitle}"
        val subtitle = apiVoucher.subtitle ?: ""
        val titleSpannable = SpannableString(title)

        val redColor = ColorStateList(arrayOf(intArrayOf()), intArrayOf(ContextCompat.getColor(this, color.gray_rating)))
        val highlightSpan = TextAppearanceSpan(null, Typeface.NORMAL, -1, redColor, null)
        titleSpannable.setSpan(highlightSpan, title.indexOf(subtitle), title.indexOf(subtitle) + subtitle.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        view.textViewTitle.text = titleSpannable
        view.textViewRegulation.text = apiVoucher.regulation
        view.textViewDate.text = apiVoucher.dateText
        view.textViewTour.text = apiVoucher.placeName
        view.textViewLabelVoucher.text = apiVoucher.detailTitle
        view.imageViewDialogClose.setOnClickListener { alertBuilder!!.dismiss() }
        view.confirmVoucher.setOnClickListener {
            presenter.claimTicket(apiVoucher)
        }

        alertBuilder!!.setView(view)
        alertBuilder!!.show()
    }

    override fun configureEmptyVouchers() {
        textViewLabelVouchers.gone()
        vouchersList.gone()
    }

    override fun configureGallery(galleryItem: List<String>) {
        view_gallery.visible()
        gallery_container.removeAllViews()

        galleryItem.forEach {
            val params = LinearLayout.LayoutParams(219.dpToPx(), 219.dpToPx())
            params.marginEnd = 10.dpToPx()

            val imageView = ImageView(this)
            imageView.setOnClickListener { _ ->
                navigationController?.fullImageModule()?.goToFullImage(this, it)
            }

            imageView.layoutParams = params
            gallery_container.addView(imageView)
            Glide.with(this)
                    .load(it)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(8.dpToPx())))
                    .into(imageView)
        }
    }

    override fun configureReadMore(club: ClubModel) {
        club_description.text = club.clubShortDescription
        club_description.doOnPreDraw {
            if (!club.clubDescription.isNullOrBlank() && club_description.text.toString() != club.clubDescription) {
                val text = "${club_description.text} ${getString(R.string.see_more)}"
                val spannable = SpannableStringBuilder(text)

                val clickableSpan = object: ClickableSpan() {
                    override fun onClick(widget: View) {
                        club_description.text = club.clubDescription
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        super.updateDrawState(ds);
                        ds.typeface = Typeface.DEFAULT_BOLD
                        ds.color = ContextCompat.getColor(this@PlaceActivity, R.color.battleship_grey)
                    }
                }

                val startIndex = text.indexOf(getString(R.string.see_more))
                val endIndex = startIndex + getString(R.string.see_more).length
                spannable.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                club_description.movementMethod = getInstance()
                club_description.text = spannable
            }
        }
    }

    override fun inviteFriend(user: UserModel){
        BranchIOHelper.onClickInvitefriend(this, userId = user.id, userName = user.name)
    }

    override fun configureButtonConfirmFollow(isActive: Boolean?) {
        button_interrest.isSelected = isActive == true
    }

    override fun showDialogUnconfirmPresence(club: ClubModel) {
        PlaceHelper.showDialogUnconfirmPresence(button_interrest, club) {
            presenter.onConfirmUnfollow()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun configureUberPrice(price: Float) {
        textViewPriceUber.text = "- R$ ${String.format("%.2f", price)}"
    }

    @SuppressLint("SetTextI18n")
    override fun configureUberTime(time: Int) {
        val finalTime = (time % 3600) / 60
        val uberTimeText = getString(string.await)
        textViewTimeUber.text = uberTimeText + " " + String.format(resources.getString(string.uber_estimate_time_arrival), finalTime)
    }

    override fun showLoading() {
        cover_container.gone()
        progress_bar.visible()
    }

    override fun hideLoading() {
        progress_bar.gone()
    }

    override fun hideLoadingVoucher() {
        alertBuilder!!.progressBarConfirmVoucher.gone()
    }

    override fun showLoadingVoucher() {
        alertBuilder!!.progressBarConfirmVoucher.visible()
    }

    private fun openEvent(it: Event) {
        when (it.categoryType) {
            Event.CategoryType.MOVIE -> {
                navigationController?.theatherPlayMovieModule()?.goToMovieDetail(this, it.id, it.name)
            }
            Event.CategoryType.THEATER -> {
                navigationController?.theatherPlayMovieModule()?.goToTheaterDetail(this, it.id, it.name)
            }
            else -> navigationController?.eventModule()?.goToEvent(this, it.id)
        }
    }

    override fun initializeMap(club: ClubModel) {
        try {
            MapsInitializer.initialize(this)
        } catch (e: Exception) {
        }

        map_view.getMapAsync {
            it.uiSettings.isMyLocationButtonEnabled = true
            if (club.latitude != null && club.longitude != null) {
                it.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(club.latitude!!.toDouble(), club.longitude!!.toDouble()), 15f))
            }
        }
    }

    override fun handleError(throwable: Throwable) {
        ErrorViewHelper.handleErrorView(coordinator_layout, throwable) {
            presenter.onCreate()
        }
    }

    override fun showCheckingDialog(club: ClubModel) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setMessage(R.string.confirmation_checkin)
        builder.setPositiveButton(R.string.yes) { _, _ ->
            navigationController?.legacyApp()?.goToPost(this,clubId= club.clubId, clubName= club.clubName)
        }.setNegativeButton(R.string.cancel) { _, _ -> }
        builder.create()
        builder.show()
    }
}