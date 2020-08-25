package br.com.goin.feature.search.event

import android.content.Context
import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.feature.search.event.model.CategorySearch
import br.com.goin.feature.search.event.model.SearchEventType
import br.com.goin.feature.search.event.model.SearchInteractor
import br.com.goin.feature.search.event.model.api.SearchByNameResponse
import br.com.goin.feature.search.event.model.api.SearchEvent
import br.com.goin.feature.search.event.model.api.SearchResponse
import br.com.goin.feature.search.event.model.api.SearchResponseByCategory
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.io.Serializable

class SearchPresenterImpl(val context: Context, val view: SearchView) : SearchPresenter {

    private val searchInteractor = SearchInteractor.instance
    private val disposables = CompositeDisposable()

    private val allEvents: MutableList<SearchEvent> = ArrayList()
    private var hash: LinkedHashMap<String, MutableList<SearchEvent>> = LinkedHashMap()

    private var categoryId: String? = null

    override fun onCreate() {
        view.initUI()
        view.hideLoading()
    }

    override fun onDestroy() {
        disposables.dispose()
    }

    override fun onReceivedCategory(categoryId: Serializable?) {
        this.categoryId = categoryId as? String
    }

    override fun onReceiveData(data: SearchResponseByCategory?) {
        allEvents.clear()

        data!!.categories.forEach { allEvents.addAll(it.items) }
        hash[context.getString(R.string.text_tab_search_all)] = allEvents
        data.categories.forEach { hash[it.category] = it.items }

        if (data.categories.size == 1) {
            view.hideTabLayout()
        } else {
            view.showTabLayout()
        }

        view.onReceiveSuccess(hash)
    }

    override fun onSearch(querySearch: String) {
        categoryId?.let { categoryId ->
            if (categoryId.isNotBlank()) {
                onMoviesSearch(querySearch)
            } else {
                onCallSearch(querySearch)
            }
        }
    }

    private fun onCallSearch(querySearch: String) {
        searchInteractor.onSearchCall(querySearch)
                .ioThread()
                .subscribe({
                    if (it.categories.isNotEmpty()) {
                        onReceiveData(it)
                    } else {
                        view.hideLoading()
                        view.configureEventsEmpty()
                    }
                    view.hideLoading()
                }, { t ->
                    view.handleError(t, querySearch)
                    view.hideLoading()
                    Log.e("SearchEventsPresenter", t.message, t)
                }).addTo(disposables)
    }

    private fun onMoviesSearch(querySearch: String) {
        searchInteractor.onSearchMovies(querySearch, categoryId)
                .ioThread()
                .subscribe({
                    if (it.getEventByName?.isNotEmpty() == true) {
                        onReceiveData(SearchResponseByCategory(categories = mutableListOf(CategorySearch("",  it.getEventByName))))
                    } else {
                        view.hideLoading()
                        view.configureEventsEmpty()
                    }
                }, { t ->
                    view.handleError(t, querySearch)
                    view.hideLoading()
                    Log.e("SearchEventsPresenter'", t.message, t)
                }).addTo(disposables)
    }
}