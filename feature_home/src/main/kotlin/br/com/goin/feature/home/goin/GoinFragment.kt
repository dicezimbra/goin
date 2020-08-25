package br.com.goin.feature.home.goin

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import br.com.goin.base.decoration.SpacingItemDecoration
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.toUpperCaseNoSpace
import br.com.goin.base.extensions.visible
import br.com.goin.base.helpers.ShareHelper
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.model.category.Category
import br.com.goin.component.model.event.Banner
import br.com.goin.component.model.event.Event
import br.com.goin.component.model.event.EventHome
import br.com.goin.component.navigation.NavigationController
import br.com.goin.component.session.user.location.UserLocation
import br.com.goin.component.session.user.location.UserLocationHelper
import br.com.goin.component.ui.kit.features.error.ErrorViewHelper
import br.com.goin.feature.home.R
import br.com.goin.feature.home.R.drawable
import br.com.goin.feature.home.R.layout
import br.com.goin.feature.home.goin.adapter.GoinCategoriesAdapter
import br.com.goin.feature.home.goin.adapter.GoinPagerAdapter
import br.com.goin.feature.home.goin.adapter.GoinSugestionAdapter
import br.com.goin.feature.home.helper.ImageUtils
import br.com.goin.feature.home.helper.SpeedyLinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_goin.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

private const val BANNER_SCALE = 0.37f
private const val SEARCH_LOCATION = 10

class GoinFragment : Fragment(), GoinView {

    private val presenter: GoinPresenter = GoinPresenterImpl(this)
    private var adapterHighlighted: GoinSugestionAdapter? = null
    private var adapterWeekEvents: GoinSugestionAdapter? = null
    private var adapterWeekendEvents: GoinSugestionAdapter? = null
    private var categoriesAdapter: GoinCategoriesAdapter? = null

    private val navigationController = NavigationController.instance
    private val userLocationHelper = UserLocationHelper()

    companion object {
        fun newInstance() = GoinFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        try {
            val info = context?.packageManager?.getPackageInfo(context?.packageName, PackageManager.GET_SIGNATURES)

            info?.let {
                for (signature in info.signatures) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
                }
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }


        return inflater.inflate(R.layout.fragment_goin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onCreate()

        userLocationHelper.onRequestLocationListener = {
            presenter.refresh()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
        Analytics.instance.screenView(activity!!, getString(R.string.home_app_screen_name))
    }

    override fun setupPermissions() {
        userLocationHelper.askPermission(activity!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

    override fun configureClickListeners() {
        search_event.setOnClickListener {
            navigationController?.searchModule()?.goToSearch(context!!, "")
        }

        search_location.setOnClickListener {

            navigationController?.searchLocationModule()?.goToSearch(this)
        }
    }

    override fun configureCurrentLocation(userLocation: UserLocation) {
        if(userLocation.uf == null || "BR".equals(userLocation.uf)) {
            val loc = userLocation.cityName
            search_location?.setText(loc)
        }else{
            val loc = userLocation.cityName +", "+userLocation.uf
            search_location?.setText(loc)
        }

    }

    override fun configureViewPager(events: List<Banner>) {
        val adapter = GoinPagerAdapter(events)
        adapter.onClickListener = { banner ->
            if (!banner.eventId.isNullOrBlank()) {
                val actionName = getString(R.string.analytics_home_highlight_action)
                val label = getString(R.string.analytics_event_label_identifier, banner.eventId?.toUpperCase())

                Analytics.instance.logClickEvent(actionName, label)
                navigationController?.eventModule()?.goToEvent(activity!!, banner.eventId)
            }
        }
        view_page_container.adapter = adapter
    }

    override fun configureRefreshView() {
        event_refresh_layout.setOnRefreshListener {
            presenter.refresh()
        }
    }

    override fun configureCategories(categories: List<Category>) {
        val displayMetrics = context?.resources?.displayMetrics
        val dpWidth = displayMetrics!!.widthPixels
        if (categoriesAdapter == null) {
            categoriesAdapter = GoinCategoriesAdapter(categories, (dpWidth / 4.6).toInt())
            categoriesAdapter?.onClickListener = { category -> onCategoryClicked(category) }

            categories_recycler_view.isNestedScrollingEnabled = true
            categories_recycler_view.layoutManager = LinearLayoutManager(context!!, HORIZONTAL, false)
            categories_recycler_view.adapter = categoriesAdapter
        } else {
            categoriesAdapter?.components = categories
            categoriesAdapter?.notifyDataSetChanged()
        }

        categories_recycler_view.visibility = View.VISIBLE
        failure_get_categories_text.visibility = View.GONE
    }

    private fun onCategoryClicked(category: Category) {
        Analytics.instance.logClickEvent(getString(R.string.category_category), category.name.toUpperCaseNoSpace())

        when (category.categoryType) {
            Category.CategoryType.MOVIE -> {
                navigationController?.theatherPlayMovieModule()?.goToMovieActivity(context!!, category.id, category.name)
            }
            Category.CategoryType.THEATER -> {
                navigationController?.theatherPlayMovieModule()?.goToTheatersActivity(context!!, category.id, category.name)
            }
            else -> {
                navigationController?.searchCategoryModule()?.goToSearchCategory(context!!, category.categoryType?.name ?: "", category.name, category.id, category.bannerCategory)
            }
        }
    }

    override fun configureBanner(banner: List<Banner>) {
        ImageUtils.resizeImagesOnScreen(card_view_banner, BANNER_SCALE)

        context?.let { context ->
            Glide.with(context)
                    .load(banner[0].url)
                    .apply(RequestOptions.placeholderOf(drawable.profile_placerholder))
                    .apply(RequestOptions.errorOf(drawable.profile_placerholder))
                    .transition(withCrossFade())
                    .into(iv_banner)

            card_view_banner.visibility = View.VISIBLE
            card_view_banner.setOnClickListener {
                banner.firstOrNull()?.run {
                    actionValue?.let { actionId ->
                        if (actionValue?.isNotBlank() == true) {
                            navigationController?.deepLinkNavigationModule()?.goToBannerAction(activity!!, actionId, action ?: "")
                        }
                    }
                }
            }
        }
    }

    override fun configureBannerEmpty() {
        card_view_banner.visibility = View.GONE
    }

    override fun configureCategoriesEmpty() {
        categories_recycler_view.visibility = View.GONE
        failure_get_categories_text.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progress_bar.visibility = View.GONE
        event_refresh_layout.isRefreshing = false
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        userLocationHelper.onRequestPermissionsResult(activity!!, requestCode, grantResults)
    }

    override fun onClickSearch() {
        search_event.setOnClickListener {
            navigationController?.searchModule()?.goToSearch(context!!, "")
        }
    }

    override fun handleError(throwable: Throwable) {
        ErrorViewHelper.handleErrorView(container, throwable) {
            presenter.refresh()
        }
    }

    private fun openEvent(it: EventHome) {
        when (it.actionValue) {
            Event.CategoryType.MOVIE.name -> {
                navigationController?.theatherPlayMovieModule()?.goToMovieDetail(context!!, it.actionValue, it.title)
            }
            Event.CategoryType.THEATER.name -> {
                navigationController?.theatherPlayMovieModule()?.goToTheatersActivity(context!!, it.actionValue!!, it.actionValue!!)
            }
            else -> navigationController?.eventModule()?.goToEvent(activity!!, it.actionValue)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SEARCH_LOCATION) {
            presenter.refresh()
        }
    }

    override fun configureHighlightedEventsEmpty() {
        if (textViewHighlightedEvents != null) {
            textViewHighlightedEvents.visible()
        }
        if (event_no_highlighted_events != null) {
            event_no_highlighted_events.visible()
        }
        if (recyclerViewHighlightedEvents != null) {
            recyclerViewHighlightedEvents.gone()
        }
    }

    override fun configureHightlightedEvents(events: List<EventHome>, userLocation: UserLocation) {
        if (adapterHighlighted == null) {
            adapterHighlighted = GoinSugestionAdapter(events, userLocation)
            adapterHighlighted?.onClickShareListener = {
                ShareHelper.shareEvent(context!!, it.actionValue ?: "", it.title!!)
            }
            adapterHighlighted?.onClickEventListener = {
                Analytics.instance.logClickEvent(getString(R.string.category_carossel_highlighted_events), it.title!!.toUpperCaseNoSpace())
                openEvent(it)
            }

            configureCarosselAnalytics(recyclerViewHighlightedEvents, getString(R.string.category_carossel_highlighted_events))
            recyclerViewHighlightedEvents.setHasFixedSize(true)
            recyclerViewHighlightedEvents.setItemViewCacheSize(4)
            recyclerViewHighlightedEvents.adapter = adapterHighlighted
            recyclerViewHighlightedEvents.isNestedScrollingEnabled = true
            recyclerViewHighlightedEvents.layoutManager = SpeedyLinearLayoutManager(context!!, LinearLayoutManager.HORIZONTAL, false)
//            recyclerViewHighlightedEvents.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            recyclerViewHighlightedEvents.addItemDecoration(SpacingItemDecoration(0, 10))

        } else {
            adapterHighlighted?.events = events
            adapterHighlighted?.notifyDataSetChanged()
        }

        recyclerViewHighlightedEvents.visible()
        textViewHighlightedEvents.visible()
        event_no_highlighted_events.gone()
    }

    override fun configureWeekEvents(events: List<EventHome>, userLocation: UserLocation) {
        if (adapterWeekEvents == null) {
            adapterWeekEvents = GoinSugestionAdapter(events, userLocation)
            adapterWeekEvents?.onClickShareListener = {
                ShareHelper.shareEvent(context!!, it.actionValue ?: "", it.title!!)
            }
            adapterWeekEvents?.onClickEventListener = {
                Analytics.instance.logClickEvent(getString(R.string.category_carossel_week_events), it.title!!.toUpperCaseNoSpace())
                openEvent(it)
            }

            configureCarosselAnalytics(event_week_view, getString(R.string.category_carossel_week_events))
            event_week_view.setHasFixedSize(true)
            event_week_view.setItemViewCacheSize(4)
            event_week_view.adapter = adapterWeekEvents
            event_week_view.isNestedScrollingEnabled = true
            event_week_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            event_week_view.addItemDecoration(SpacingItemDecoration(0, 10))

        } else {
            adapterWeekEvents?.events = events
            adapterWeekEvents?.notifyDataSetChanged()
        }

        event_week_view.visible()
        text_this_week.visible()
        event_no_events_suggestions.gone()
    }

    override fun configureWeekEventsEmpty() {
        event_week_view?.gone()
        text_this_week.gone()
        event_no_events_suggestions.gone()
    }

    override fun configureWeekendEvents(events: List<EventHome>, userLocation: UserLocation) {
        if (adapterWeekendEvents == null) {
            adapterWeekendEvents = GoinSugestionAdapter(events, userLocation)
            adapterWeekendEvents?.onClickShareListener = {
                ShareHelper.shareEvent(context!!, it.actionValue ?: "", it.title!!)
            }
            adapterWeekendEvents?.onClickEventListener = {
                Analytics.instance.logClickEvent(getString(R.string.category_carossel_weekend_events), it.title!!.toUpperCaseNoSpace())
                openEvent(it)
            }

            configureCarosselAnalytics(event_this_weekend_view, getString(R.string.category_carossel_weekend_events))
            event_this_weekend_view.setHasFixedSize(true)
            event_this_weekend_view.setItemViewCacheSize(4)
            event_this_weekend_view.adapter = adapterWeekendEvents
            event_this_weekend_view.isNestedScrollingEnabled = true
            event_this_weekend_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            event_this_weekend_view.addItemDecoration(SpacingItemDecoration(0, 10))

        } else {
            adapterWeekendEvents?.events = events
            adapterWeekendEvents?.notifyDataSetChanged()
        }

        event_this_weekend_view.visible()
        text_this_weekend.visible()
    }

    override fun configureWeekendEventsEmpty() {
        if (event_this_weekend_view != null) {
            event_this_weekend_view.gone()
        }
        if (text_this_weekend != null) {
            text_this_weekend.visible()
        }
    }

    private fun configureCarosselAnalytics(recyclerView: RecyclerView, name: String) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when (newState) {
                    SCROLL_STATE_IDLE -> {
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val position = layoutManager.findLastVisibleItemPosition()
                        Analytics.instance.logSwipeEvent(getString(R.string.category_carossel_scroll), "${name}_$position")
                    }
                }
            }
        })
    }
}