package br.com.goin.feature.search.category.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.goin.base.extensions.dpToPx
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.toUpperCaseNoSpace
import br.com.goin.base.extensions.visible
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.navigation.NavigationController
import br.com.goin.component.ui.kit.features.error.ErrorViewHelper
import br.com.goin.component.ui.kit.views.GridSpaceItemDecoration
import br.com.goin.feature.search.category.*
import br.com.goin.feature.search.category.helper.EndlessRecyclerViewScrollListener
import br.com.goin.feature.search.category.model.SearchFilter
import br.com.goin.feature.search.category.model.SearchFilterPageItem
import br.com.goin.feature.search.category.model.SearchFilterTimePeriod
import br.com.goin.feature.search.category.model.Type
import kotlinx.android.synthetic.main.fragment_search_by_category_tab.*

class SearchByCategoryTabFragment: Fragment(), SearchByCategoryTabView{

    companion object {
        private const val FILTER_PARAM = "FILTER_PARAM"
        private const val FILTER_VIEW_TYPE = "FILTER_VIEW_TYPE"
        private const val FILTER_PERIOD_TIME_PARAM = "FILTER_PERIOD_TIME_PARAM"

        fun newInstance(searchFilter: SearchFilter,
                        viewType: SearchByCategoryTabAdapter.VIEW_MODE,
                        searchFilterTimePeriod: SearchFilterTimePeriod): Fragment{
            val bundle = Bundle()
            bundle.putSerializable(FILTER_PARAM, searchFilter)
            bundle.putSerializable(FILTER_VIEW_TYPE, viewType)
            bundle.putSerializable(FILTER_PERIOD_TIME_PARAM, searchFilterTimePeriod)

            val fragment = SearchByCategoryTabFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val navigationController = NavigationController.instance
    private val presenter: SearchByCategoryTabPresenter = SearchByCategoryTabPresenterImpl(this)
    private var scrollListener :EndlessRecyclerViewScrollListener? = null
    private var adapter: SearchByCategoryTabAdapter? = null
    private val spaceItemDecoration = GridSpaceItemDecoration(8.dpToPx())
    private lateinit var viewType: SearchByCategoryTabAdapter.VIEW_MODE

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_by_category_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewType = arguments?.getSerializable(FILTER_VIEW_TYPE) as SearchByCategoryTabAdapter.VIEW_MODE

        presenter.onReceiveFilterRequest(arguments?.getSerializable(FILTER_PARAM))
        presenter.onReceiveSearchFilterTimePeriodRequest(arguments?.getSerializable(FILTER_PERIOD_TIME_PARAM))
        presenter.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun onFilterChanged(searchFilter: SearchFilter){
        presenter.refreshFilter(searchFilter)
    }

    override fun onViewTypeChange(type: SearchByCategoryTabAdapter.VIEW_MODE){
        configureLayoutManager(type)
        adapter?.notifyDataSetChanged()
    }

    override fun receiveItens(itens: MutableList<SearchFilterPageItem>, location: Pair<Double, Double>?) {
        if(recycler_view.adapter == null) {
            adapter = SearchByCategoryTabAdapter(itens, viewType, location)
            adapter?.onClickListener = {
                Analytics.instance.logClickEvent(getString(R.string.detail_category), it.name.toUpperCaseNoSpace())
                when (it.type) {
                    Type.EVENT -> navigationController?.eventModule()?.goToEvent(context!!, it.id)
                    Type.CLUB -> navigationController?.placeModule()?.goToPlace(context!!, it.id, it.subcategory ?: "")
                }
            }

            adapter?.onClickChangeViewType = {
                if (activity is SearchByCategoryView){
                    (activity as SearchByCategoryView).changeViewType()
                }
            }

            configureLayoutManager(viewType)
            recycler_view?.addItemDecoration(spaceItemDecoration)
            recycler_view.setHasFixedSize(true)
            recycler_view?.adapter = adapter

            scrollListener = object : EndlessRecyclerViewScrollListener(recycler_view?.layoutManager as LinearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    presenter.loadMore(page)
                }
            }
            recycler_view?.addOnScrollListener(scrollListener!!)
        }else{
            val adapter = recycler_view.adapter as SearchByCategoryTabAdapter
            adapter.itens = ArrayList(itens)
            adapter.notifyDataSetChanged()
        }
    }

    private fun configureLayoutManager(type: SearchByCategoryTabAdapter.VIEW_MODE) {
        when (type) {
            SearchByCategoryTabAdapter.VIEW_MODE.GRID -> {

                val layoutManager = GridLayoutManager(context, 2)
                layoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup(){
                    override fun getSpanSize(position: Int): Int {
                        return when(position){
                            0 -> 2
                            else -> 1
                        }
                    }
                }
                recycler_view?.layoutManager = layoutManager
                adapter?.type = SearchByCategoryTabAdapter.VIEW_MODE.GRID
            }
            else -> {
                recycler_view?.layoutManager = LinearLayoutManager(context)
                adapter?.type = SearchByCategoryTabAdapter.VIEW_MODE.LIST
            }
        }
    }

    override fun onLoadedMore(itens: List<SearchFilterPageItem>) {
        val adapter = recycler_view.adapter as SearchByCategoryTabAdapter
        adapter.itens.addAll(itens)
        adapter.notifyDataSetChanged()
    }

    override fun showWithoutNearbyResult() {
        //no_result_nearby.visible()
    }

    override fun hideWithoutNearbyResult() {
        //no_result_nearby.gone()
    }

    override fun resetEndlessRecyclerview(){
        scrollListener?.resetState()
    }

    override fun showLoading() {
        recycler_view?.gone()
        progress_bar?.visible()
    }

    override fun hideLoading() {
        recycler_view?.visible()
        progress_bar?.gone()
    }

    override fun handleError(throwable: Throwable) {
        container?.let {
            ErrorViewHelper.handleErrorView(container, throwable) {
                presenter.onCreate()
            }
        }
    }
}