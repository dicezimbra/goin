package br.com.goin.feature.profile.myinterests

import br.com.goin.component.model.event.Event
import br.com.goin.component.model.event.api.Interrest

interface MyInterestView {
    fun showEmptyView()
    fun configureMyInterests(events: List<Interrest>)
    fun showLoading()
    fun hideLoading()
}