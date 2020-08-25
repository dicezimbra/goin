package br.com.goin.feature.search.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.goin.component.navigation.NavigationController
import br.com.goin.component.session.user.location.UserLocation
import br.com.goin.feature.search.event.adapter.SearchEventAdapter
import br.com.goin.feature.search.event.model.SearchEventType
import br.com.goin.feature.search.event.model.SearchResult
import br.com.goin.feature.search.event.model.api.SearchEvent
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment(), SearchFragmentView {

    private val presenter: SearchFragmentPresenter = SearchFragmentPresenterImpl(this)
    private val navigationController = NavigationController.instance

    private var searchEventAdapter: SearchEventAdapter? = null
    private val searchList: MutableList<SearchEvent> = ArrayList()

    companion object {
        private const val SEARCH_LIST = "SEARCH_LIST"

        fun newInstance(searchList: MutableList<SearchEvent>): SearchFragment {
            val extras = Bundle()
            extras.putSerializable(SEARCH_LIST, ArrayList<SearchEvent>(searchList))
            val fragment = SearchFragment()
            fragment.arguments = extras
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onReceiveSearchResult(arguments?.getSerializable(SEARCH_LIST))
        presenter.onCreate()
    }

    @Suppress("UNCHECKED_CAST")
    override fun initList(list: MutableList<SearchEvent>) {
        searchList.addAll(list)
        searchEventAdapter?.searchEventList?.clear()
        searchEventAdapter?.searchEventList?.addAll(list)
        searchEventAdapter?.notifyDataSetChanged()
    }

    override fun configureRecyclerView(userLocation: UserLocation) {
        searchList.clear()
        searchEventAdapter?.searchEventList?.clear()
        searchEventAdapter = SearchEventAdapter(searchList)
        searchEventAdapter?.lat = userLocation.lat
        searchEventAdapter?.lng = userLocation.lng
        searchEventAdapter?.onClickListener = { onClickItem(it) }
        recyclerViewSearch?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        recyclerViewSearch?.adapter = searchEventAdapter
    }

    private fun onClickItem(searchEvent: SearchEvent) {
        when (searchEvent.categoryType) {
            SearchEventType.THEATER.name, SearchEventType.THEATER.name -> {
                navigationController?.theatherPlayMovieModule()?.goToTheaterDetail(context!!, searchEvent.id, searchEvent.name)
            }
            SearchEventType.MOVIE.name, SearchEventType.THEATER.name -> {
                navigationController?.theatherPlayMovieModule()?.goToMovieDetail(context!!, searchEvent.id, searchEvent.name)
            }
            SearchEventType.PLACE.name -> {
                navigationController?.placeModule()?.goToPlace(activity!!, searchEvent.id, searchEvent.categorization)
            }
            else -> {
                navigationController?.eventModule()?.goToEvent(activity!!, searchEvent.id)
            }
        }
    }
}