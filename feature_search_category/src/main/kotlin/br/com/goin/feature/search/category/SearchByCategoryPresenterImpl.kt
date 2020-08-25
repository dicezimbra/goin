package br.com.goin.feature.search.category

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.extensions.useMemoryCache
import br.com.goin.component.session.user.location.UserLocation
import br.com.goin.component.session.user.location.UserLocationInteractor
import br.com.goin.feature.search.category.model.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class SearchByCategoryPresenterImpl(val view: SearchByCategoryView) : SearchByCategoryPresenter {

    private var categoryName: String? = ""
    private var categoryId: String? = ""
    private var categoryType: String? = ""
    private var categoryBanner: String? = ""
    private var searchFilterRequest: SearchFilter? = SearchFilter()
    private var responseSubcategories: ResponseSubcategories? = null
    private var searchFilterTimePeriod: List<SearchFilterTimePeriod> = listOf()

    private val interactor = SearchFilterInteractor.instance
    private val userLocationInteractor = UserLocationInteractor.instance
    private val disposable = CompositeDisposable()

    override fun onCreate() {
        view.configureToolbarFilter(hasFilterOn())

        val userLocation = userLocationInteractor.fetch()
        searchFilterRequest?.myLocation = arrayOf(userLocation.lat, userLocation.lng)
        searchFilterRequest?.categoryId = categoryId
        configureToolbar(userLocation)
        loadSubCategories()

        configureFilterTimePeriod()
        configureTabs()
    }

    override fun onDestroy() {
        disposable.dispose()
    }

    override fun onReceiveCategory(categoryType:String,categoryId: String, categoyName: String?,
                                   categoryBanner: String?) {
        this.categoryId = categoryId
        this.categoryName = categoyName
        this.categoryType = categoryType
        this.categoryBanner = categoryBanner
    }

    override fun logScreenName() {
        categoryName?.let {
            val category = it.toUpperCase().replace(" ", "_")
            view.logScreenView(category)
        }
    }

    private fun loadSubCategories() {
        interactor.getSubCategories(categoryId!!)
                .useMemoryCache("SUBCATEGORIES$categoryId", ResponseSubcategories::class.java)
                .ioThread()
                .subscribe({
                    this.responseSubcategories = it
                    view.receiveSubCategories(it)
                }, {
                    Log.e(javaClass.simpleName, it.printStackTrace().toString())
                })
                .addTo(disposable)
    }

    override fun onClickFilter() {
        val filterTimePeriod = if(searchFilterTimePeriod.size == 1) searchFilterTimePeriod.find { it.isCustomDate } else null
        view.goToFilterByCategory(categoryId?: "", searchFilterRequest!!,  filterTimePeriod, responseSubcategories, categoryName ?: "")
    }

    override fun onClickSubcategory(subcategoriesModel: SubcategoriesModel?) {
        searchFilterRequest?.subCategoriesId = subcategoriesModel?.subcategoryId
        onRefresh(null, null)
        view.onClickSubCategories(subcategoriesModel, hasFilterOn())
    }

    override fun onRefresh(searchFilterRequest: SearchFilter?, searchFilterTimePeriod: SearchFilterTimePeriod?) {
        val userLocation = userLocationInteractor.fetch()

        if(searchFilterRequest != null) {
            this.searchFilterRequest = searchFilterRequest
            view.configureSubCategories(searchFilterRequest)
        }

        this.searchFilterRequest?.myLocation = arrayOf(userLocation.lat, userLocation.lng)
        view.configureSearchbar(userLocation.cityName ?: "")

        if(searchFilterTimePeriod != null){
            this.searchFilterTimePeriod = listOf(searchFilterTimePeriod)
        }else{
            configureFilterTimePeriod()
        }

        configureTabs()
        view.configureToolbarFilter(hasFilterOn())
        view.onFilterChanged(this.searchFilterRequest!!)
    }

    private fun configureFilterTimePeriod() {
        searchFilterTimePeriod = when (categoryType) {
            "EVENT" -> {
                val all = SearchFilterTimePeriod().clearDates()
                val today = SearchFilterTimePeriod().onlyToday()
                val tomorrow = SearchFilterTimePeriod().onlyTomorrow()
                val week = SearchFilterTimePeriod().onlyThisWeek()
                val weekend = SearchFilterTimePeriod().onlyThisWeekend()
                val month = SearchFilterTimePeriod().onlyThisMonth()
                val nextMonth = SearchFilterTimePeriod().onlyNextMonth()
                listOf(all, today, tomorrow, weekend, week, month, nextMonth)
            }
            else -> {
                val all = SearchFilterTimePeriod().clearDates()
                listOf(all)
            }
        }
    }

    private fun configureTabs(){
        searchFilterRequest?.let {
            view.configureTabs(it, searchFilterTimePeriod, categoryName?: "")
            if(searchFilterTimePeriod.size > 1) view.showTabLayout() else view.hideTabLayout()
        }
    }

    private fun configureToolbar(userLocation: UserLocation) {
        val city = if (userLocation.uf == null) {
            "${userLocation.cityName}"
        } else {
            "${userLocation.cityName} - ${userLocation.uf}"
        }
        view.configureToolbar(city)
    }

    private fun hasFilterOn() = (searchFilterRequest?.hasFilterEnabled() ?: false
            || (searchFilterTimePeriod.filter { it.isCustomDate }.size > 1))
}