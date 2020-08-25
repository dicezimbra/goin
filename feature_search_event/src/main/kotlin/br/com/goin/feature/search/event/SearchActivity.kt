package br.com.goin.feature.search.event

import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_search_events.*
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import br.com.goin.component.analytics.analytics.Analytics
import androidx.viewpager.widget.ViewPager
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.visible
import br.com.goin.component.ui.kit.features.error.ErrorViewHelper
import br.com.goin.feature.search.event.model.api.SearchEvent
import com.google.android.material.tabs.TabLayout
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.TimeUnit

class SearchActivity : AppCompatActivity(), SearchView {

    private val presenter: SearchPresenter = SearchPresenterImpl(this, this)
    private var searchAdapter: SearchPagerAdapter? = null

    private var queryNoResult = ""
    private var disposable = CompositeDisposable()

    companion object {
        const val SEARCH_VIEW = "SEARCH_VIEW"
        const val CATEGORY_ID = "CATEGORY_ID"

        fun starter(context: Context, categoryId: String?) {
            val intent = Intent(context, SearchActivity::class.java)
            intent.putExtra(CATEGORY_ID, categoryId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_events)

        presenter.onReceivedCategory(intent?.extras?.getSerializable(CATEGORY_ID))
        presenter.onCreate()
    }

    override fun onResume() {
        super.onResume()
        Analytics.instance.screenView(this, getString(R.string.search_all_screen_name))
    }

    override fun initUI() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        ViewCompat.setTransitionName(searchView, SEARCH_VIEW)
        searchView.onActionViewExpanded()

        viewPager.offscreenPageLimit = 3
        tabLayout.setupWithViewPager(viewPager)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                searchAdapter?.let { adapter ->
                    val response = adapter.indexes[position]
                    Analytics.instance.screenView(this@SearchActivity, "BUSCA_NOME_$response")
                }
            }
        })

        toolbar.setNavigationOnClickListener {
            logOnAnalytics(getString(R.string.analytics_search_back_action), getString(R.string.analytics_back_label_identifier))
            onBackPressed()
        }

        searchView.queryTextChanges()
                .skipInitialValue()
                .doOnNext {
                    showLoading(it.toString())
                }
                .debounce(700, TimeUnit.MILLISECONDS)
                .subscribe {
                    if (it.length >= 3) {
                        queryNoResult = it.toString()
                        runOnUiThread {
                            if (show_loading_msg != null) {
                                show_loading_msg.visible()
                                progressBar.visible()
                            }
                            logOnAnalytics(getString(R.string.analytics_search_search_action), getString(R.string.analytics_search_term_label_identifier, it))
                            presenter.onSearch(it.toString())
                        }
                    } else if (it.isNullOrEmpty()) {
                        runOnUiThread {
                            if (show_loading_msg != null) {
                                show_loading_msg.gone()
                                progressBar.gone()
                            }
                        }
                    }
                }.addTo(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
        disposable.dispose()
    }

    override fun onReceiveSuccess(hash: LinkedHashMap<String, MutableList<SearchEvent>>) {
        progressBar.isIndeterminate = false
        progressBar.visibility = View.GONE
        show_loading_msg.visibility = View.GONE

        searchAdapter = SearchPagerAdapter(this, hash, supportFragmentManager)
        viewPager.adapter = searchAdapter

        frameLayoutEmptyList.visibility = View.GONE
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        viewPager.visibility = View.VISIBLE
    }

    override fun hideTabLayout() {
        tabLayout.visibility = View.GONE
    }

    override fun showTabLayout() {
        tabLayout.visibility = View.VISIBLE
    }

    override fun handleError(throwable: Throwable, query: String) {
        ErrorViewHelper.handleErrorView(container, throwable) {
            presenter.onSearch(query)
        }
    }

    override fun hideLoading() {
        progressBar.isIndeterminate = false
        progressBar.visibility = View.GONE
        show_loading_msg.visibility = View.GONE
    }

    override fun showLoading(msg: String?) {
        progressBar.isIndeterminate = true
        progressBar.visibility = View.VISIBLE
        frameLayoutEmptyList.visibility = View.GONE

        show_loading_msg.visibility = View.VISIBLE
        show_loading_msg.text = getString(R.string.searching)

        when (msg) {
            null -> show_loading_msg.text = getString(R.string.searching)
            "" -> show_loading_msg.text = getString(R.string.searching)
            else -> show_loading_msg.text = getString(R.string.searching_city, msg)
        }
    }

    override fun configureEventsEmpty() {
        empty_list.text = "${getString(R.string.no_result_search)} \"$queryNoResult\""
        tabLayout.visibility = View.GONE
        viewPager.visibility = View.GONE
        frameLayoutEmptyList.visibility = View.VISIBLE
    }

    private fun logOnAnalytics(action: String, label: String) {

    }
}
