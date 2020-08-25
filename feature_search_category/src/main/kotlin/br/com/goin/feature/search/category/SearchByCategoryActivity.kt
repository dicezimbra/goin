package br.com.goin.feature.search.category

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ViewOutlineProvider
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.goin.base.extensions.*
import br.com.goin.base.utils.Constants
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.navigation.NavigationController
import br.com.goin.component.ui.kit.views.SpaceItemDecoration
import br.com.goin.feature.search.category.adapter.SubCategoriesAdapter
import br.com.goin.feature.search.category.filter.FilterByCategoryPartyActivity
import br.com.goin.feature.search.category.helper.EndlessRecyclerViewScrollListener
import br.com.goin.feature.search.category.model.ResponseSubcategories
import br.com.goin.feature.search.category.model.SearchFilter
import br.com.goin.feature.search.category.model.SearchFilterTimePeriod
import br.com.goin.feature.search.category.model.SubcategoriesModel
import br.com.goin.feature.search.category.tab.SearchByCategoryTabAdapter
import br.com.goin.feature.search.category.tab.SearchByCategoryTabView
import kotlinx.android.synthetic.main.activity_search_by_category.*
import com.google.android.material.appbar.AppBarLayout

class SearchByCategoryActivity : AppCompatActivity(), SearchByCategoryView {

    companion object {
        private const val CATEGORY_NAME = "CATEGORY_NAME"
        private const val CATEGORY_ID = "CATEGORY_ID"
        private const val CATEGORY_TYPE = "CATEGORY_TYPE"
        private const val CATEGORY_BANNER = "CATEGORY_BANNER"

        fun start(context: Context, categoryType: String, categoryId: String, categoryName: String, categoryBanner: String? = "") {
            val intent = Intent(context, SearchByCategoryActivity::class.java)
            intent.putExtra(CATEGORY_NAME, categoryName)
            intent.putExtra(CATEGORY_ID, categoryId)
            intent.putExtra(CATEGORY_TYPE, categoryType)
            intent.putExtra(CATEGORY_BANNER, categoryBanner)
            context.startActivity(intent)
        }
    }

    private val presenter: SearchByCategoryPresenter = SearchByCategoryPresenterImpl(this)
    private val navigationController = NavigationController.instance
    private var subCategoriesAdapter: SubCategoriesAdapter? = null
    private var scrollListener: EndlessRecyclerViewScrollListener? = null
    private var viewType = SearchByCategoryTabAdapter.VIEW_MODE.GRID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_by_category)

        val categoryId = intent.getStringExtra(CATEGORY_ID)
        val categoryType = intent.getStringExtra(CATEGORY_TYPE)
        val categoryName = intent.getStringExtra(CATEGORY_NAME)
        val categoryBanner = intent.getStringExtra(CATEGORY_BANNER)

        presenter.onReceiveCategory(categoryType, categoryId, categoryName, categoryBanner)
        presenter.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        subCategoriesAdapter?.selectedPos = RecyclerView.NO_POSITION
        presenter.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        presenter.logScreenName()
    }

    override fun configureToolbar(location: String) {
        setSupportActionBar(toolbar)
        supportActionBar?.title = location

        toolbar.setTitleColor(R.color.search_gray)
        toolbar.setRightButton(R.drawable.icon_filter_3x, onClickListener = {
            Analytics.instance.logClickEvent(getString(R.string.filter_category), getString(R.string.filter_value))
            presenter.onClickFilter()
        })

        toolbar.setTitleImage(R.drawable.icon_pin_3x){
            navigationController?.searchLocationModule()?.goToSearch(this)
        }
        toolbar.setOnBackButtonClicked(R.drawable.ic_arrow_white_24dp) { finish() }
    }

    override fun configureSearchbar(cityName: String) {
        supportActionBar?.title = cityName
    }

    override fun receiveSubCategories(subcategories: ResponseSubcategories) {
        subCategoriesAdapter = SubCategoriesAdapter(subcategories.subcategories, "")
        subCategoriesAdapter?.onClickSubCategories = {
            presenter.onClickSubcategory(it)
        }

        recycler_view_subcategories.addItemDecoration(SpaceItemDecoration(5.dpToPx()))
        recycler_view_subcategories.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recycler_view_subcategories.adapter = subCategoriesAdapter
        if (subcategories.subcategories.size == 1) {
            recycler_view_subcategories.gone()
        } else {
            recycler_view_subcategories.visible()
        }
    }

    override fun configureTabs(searchFilter: SearchFilter, searchFilterTimePeriod: List<SearchFilterTimePeriod>, categoryName: String) {
        view_pager.adapter = SearchByCategoryAdapter(supportFragmentManager, this, viewType, searchFilter, searchFilterTimePeriod)
        tabLayout.setupWithViewPager(view_pager)
    }

    override fun showTabLayout() {
        tabLayout.visible()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            app_bar.outlineProvider = ViewOutlineProvider.BOUNDS
        }
        val params = toolbar.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
    }

    override fun hideTabLayout() {
        tabLayout.gone()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            app_bar.outlineProvider = null
        }
        val params = toolbar.layoutParams as AppBarLayout.LayoutParams
        params.scrollFlags = 0
    }

    override fun configureToolbarFilter(isFilterActive: Boolean) {
        toolbar.getRightButton().isSelected = isFilterActive

        if (isFilterActive) {
            toolbar.getRightButton().setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_filter_selected_3x))
        } else {
            toolbar.getRightButton().setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_filter_3x))
        }
    }

    override fun logScreenView(category: String) {
        Analytics.instance.screenView(this, getString(R.string.list_screen_name, category))
    }

    override fun configureSubCategories(searchFilter: SearchFilter){
        subCategoriesAdapter?.selectedPos = subCategoriesAdapter?.subCategories?.withIndex()?.find { it.value.subcategoryId == searchFilter.subCategoriesId }?.index ?: -1
        subCategoriesAdapter?.notifyDataSetChanged()
    }

    override fun onClickSubCategories(it: SubcategoriesModel?, hasFilterOn: Boolean) {
        if(it == null){
            scrollListener?.resetState()
            toolbar.hideRightSecondButton()
        }else{
            Analytics.instance.logClickEvent(getString(R.string.subcategory_category), it.name.toUpperCaseNoSpace())
            scrollListener?.resetState()
        }
    }

    override fun goToFilterByCategory(categoryId: String,
                                      searchFilterRequest: SearchFilter,
                                      searchFilterTimePeriod: SearchFilterTimePeriod?,
                                      subCategories: ResponseSubcategories?,
                                      categoryName: String?) {
        FilterByCategoryPartyActivity.starter(this, categoryId, categoryName ?: "",
                searchFilterRequest, subCategories, searchFilterTimePeriod, subCategoriesAdapter?.selectedPos)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                Constants.UPDATE_FILTER -> {
                    val searchFilterRequest = data?.extras?.getSerializable(Constants.FILTER) as? SearchFilter
                    val searchFilterTimePeriod = data?.extras?.getSerializable(Constants.FILTER_TIME_PERIOD) as? SearchFilterTimePeriod
                    presenter.onRefresh(searchFilterRequest, searchFilterTimePeriod)
                }
                Constants.SEARCH_LOCATION -> {
                    presenter.onRefresh(null,null)
                }
            }
        }
    }

    override fun onFilterChanged(searchFilter: SearchFilter) {
        val fragments = supportFragmentManager.fragments
        fragments.forEach {
            val fragment = it as? SearchByCategoryTabView
            fragment?.onFilterChanged(searchFilter)
        }
    }

    override fun changeViewType() {
        viewType = if (viewType == SearchByCategoryTabAdapter.VIEW_MODE.LIST){
            SearchByCategoryTabAdapter.VIEW_MODE.GRID
        } else {
            SearchByCategoryTabAdapter.VIEW_MODE.LIST
        }

        (view_pager.adapter as SearchByCategoryAdapter).viewType = viewType
        val fragments = supportFragmentManager.fragments
        fragments.forEach {
            val fragment = it as? SearchByCategoryTabView
            fragment?.onViewTypeChange(viewType)
        }
    }
}