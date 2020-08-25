package br.com.goin.feature.home.goin

import br.com.goin.component.model.category.Category
import br.com.goin.component.model.event.Banner
import br.com.goin.component.model.event.EventHome
import br.com.goin.component.model.event.api.EventHomeApi
import br.com.goin.component.session.user.location.UserLocation

interface GoinView {
    fun configureViewPager(events: List<Banner>)
    fun configureCategories(categories: List<Category>)
    fun configureCategoriesEmpty()
    fun configureRefreshView()
    fun hideLoading()
    fun configureBannerEmpty()
    fun configureBanner(banner: List<Banner>)
    fun setupPermissions()
    fun configureClickListeners()
    fun onClickSearch()
    fun configureCurrentLocation(userLocation: UserLocation)
    fun handleError(throwable: Throwable)
    fun configureHighlightedEventsEmpty()
    fun configureHightlightedEvents(events: List<EventHome>, userLocation: UserLocation)
    fun configureWeekEventsEmpty()
    fun configureWeekEvents(events: List<EventHome>, userLocation: UserLocation)
    fun configureWeekendEventsEmpty()
    fun configureWeekendEvents(events: List<EventHome>, userLocation: UserLocation)
}