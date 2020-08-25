package br.com.goin.feature.search.event

import br.com.goin.component.session.user.location.UserLocationInteractor
import br.com.goin.feature.search.event.model.SearchResult
import br.com.goin.feature.search.event.model.api.SearchEvent
import java.io.Serializable

class SearchFragmentPresenterImpl(val view: SearchFragmentView) : SearchFragmentPresenter {

    private val userLocationInteractor = UserLocationInteractor.instance
    private var events: MutableList<SearchEvent>? = null

    override fun onCreate() {
        val userLocation = userLocationInteractor.fetch()
        view.configureRecyclerView(userLocation)

            view.initList(events!!)

    }

    override fun onReceiveSearchResult(serializable: Serializable?) {
        this.events = serializable as MutableList<SearchEvent>?
    }
}