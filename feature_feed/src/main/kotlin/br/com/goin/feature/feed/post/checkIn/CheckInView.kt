package br.com.goin.feature.feed.post.checkIn

import br.com.goin.feature.feed.model.checkIn.CheckIn

interface CheckInView {

    fun configureSearchView()
    fun showLoading()
    fun hideLoading()
    fun onReceiveEventsToCheckIn(it: List<CheckIn>)
    fun showNoEventsAvaiable()
    fun askPermissionsLocation()
    fun configureOnClickListeners()
}
