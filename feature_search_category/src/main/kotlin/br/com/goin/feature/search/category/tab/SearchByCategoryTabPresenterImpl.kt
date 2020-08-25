package br.com.goin.feature.search.category.tab

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.extensions.useCache
import br.com.goin.base.extensions.useMemoryCache
import br.com.goin.component.session.user.location.UserLocation
import br.com.goin.component.session.user.location.UserLocationInteractor
import br.com.goin.feature.search.category.model.SearchFilter
import br.com.goin.feature.search.category.model.SearchFilterInteractor
import br.com.goin.feature.search.category.model.SearchFilterPage
import br.com.goin.feature.search.category.model.SearchFilterTimePeriod
import br.com.goin.feature.search.category.model.api.SearchFilterRequest
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.io.Serializable

class SearchByCategoryTabPresenterImpl(val view: SearchByCategoryTabView): SearchByCategoryTabPresenter{

    private var searchFilter : SearchFilter? = null
    private var searchFilterTimePeriod : SearchFilterTimePeriod? = null

    private val interactor = SearchFilterInteractor.instance
    private val userLocationInteractor = UserLocationInteractor.instance
    private val disposable = CompositeDisposable()

    override fun onCreate(){
        loadFilter(1)
    }

    override fun onDestroy(){
        disposable.dispose()
    }

    override fun refreshFilter(searchFilter : SearchFilter){
        this.searchFilter = searchFilter
        view.resetEndlessRecyclerview()
        loadFilter(1)
    }

    override fun loadMore(page: Int) {
        loadFilter(page)
    }

    override fun onReceiveSearchFilterTimePeriodRequest(serializable: Serializable?) {
        this.searchFilterTimePeriod = serializable as SearchFilterTimePeriod
    }

    override fun onReceiveFilterRequest(serializable: Serializable?) {
        this.searchFilter = serializable as SearchFilter
    }

    private fun loadFilter(page: Int) {
        if (searchFilter != null && searchFilterTimePeriod != null) {
            val observable = if(page == 1){
                interactor.search(searchFilter!!, searchFilterTimePeriod!!, page = page)
                        .useMemoryCache("FILTER$page$searchFilter$searchFilterTimePeriod", SearchFilterPage::class.java)
            }else{
                interactor.search(searchFilter!!, searchFilterTimePeriod!!, page = page)
            }
            observable
                .ioThread()
                .doOnSubscribe { if(page <= 1) view.showLoading() }
                .doOnTerminate { if(page <= 1) view.hideLoading() }
                .subscribe({
                    val userLocation = userLocationInteractor.fetch()
                    if(page <= 1){
                        view.receiveItens(it.itens, Pair(userLocation.lat, userLocation.lng))
                        checkResultLocation(it, userLocation)
                    }else{
                        view.onLoadedMore(it.itens)
                    }

                }, {
                    view.handleError(it)
                    Log.e(javaClass.simpleName, it.printStackTrace().toString())
                }).addTo(disposable)
        }
    }


    private fun checkResultLocation(searchFilterPage: SearchFilterPage, userLocation: UserLocation) {
        view.hideWithoutNearbyResult()
        searchFilterPage.itens.firstOrNull()?.let {
            if (it.fetchDistance(Pair(userLocation.lat, userLocation.lng)) > 30) {
                view.showWithoutNearbyResult()
            }
        }
    }
}