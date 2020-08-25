package br.com.goin.feature.search.event

import br.com.goin.component.session.user.location.UserLocation
import br.com.goin.feature.search.event.model.api.SearchEvent

interface SearchFragmentView {
    fun configureRecyclerView(userLocation: UserLocation)
    fun initList(list: MutableList<SearchEvent>)
}