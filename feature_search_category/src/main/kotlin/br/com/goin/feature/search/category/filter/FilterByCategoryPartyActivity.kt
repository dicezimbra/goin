package br.com.goin.feature.search.category.filter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import androidx.recyclerview.widget.GridLayoutManager
import br.com.goin.base.extensions.formatFloatTwoDigits
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.visible
import br.com.goin.base.utils.Constants
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.ui.kit.datepicker.DatePickerHelper
import br.com.goin.feature.search.category.R
import br.com.goin.feature.search.category.filter.adapter.ChipTagAdapter
import br.com.goin.feature.search.category.filter.adapter.SubCategoriesFilterPartyAdapter
import br.com.goin.feature.search.category.filter.model.ChipTagResponse
import br.com.goin.feature.search.category.model.ResponseSubcategories
import br.com.goin.feature.search.category.model.SearchFilter
import br.com.goin.feature.search.category.model.SearchFilterTimePeriod
import kotlinx.android.synthetic.main.activity_filter_by_category_party.*
import kotlin.collections.ArrayList
import androidx.constraintlayout.widget.ConstraintSet
import com.appyvet.materialrangebar.RangeBar

private const val CLUBS = "4ad3387e-99d8-48e0-8b3d-fb5e251ceaa1"
private const val RESTAURANTS = "8ce6e7bc-5eb4-4a04-83a5-d0a84d732b3f"

class FilterByCategoryPartyActivity : AppCompatActivity(), FilterByCategoryPartyView {

    private val presenter: FilterByCategoryPartyPresenter = FilterByCategoryPartyPresenterImpl(this)
    private var chipTagAdapter: ChipTagAdapter? = null
    private var searchFilterRequest: SearchFilter? = null
    private var searchFilterTimePeriod: SearchFilterTimePeriod? = null
    private var subCategoriesAdapter: SubCategoriesFilterPartyAdapter? = null
    private var categoryId = ""

    companion object {
        fun starter(activity: Activity,
                    categoryId: String,
                    categoryName: String,
                    searchFilterRequest: SearchFilter,
                    subCategories: ResponseSubcategories?,
                    searchFilterTimePeriod: SearchFilterTimePeriod?,
                    subCategoryPosition: Int?) {
            val intent = Intent(activity, FilterByCategoryPartyActivity::class.java)
            intent.putExtra(Constants.CATEGORY_ID, categoryId)
            intent.putExtra(Constants.CATEGORY_NAME, categoryName)
            intent.putExtra(Constants.FILTER, searchFilterRequest)
            intent.putExtra(Constants.FILTER_PARTY, subCategories)
            intent.putExtra(Constants.FILTER_TIME_PERIOD, searchFilterTimePeriod)
            intent.putExtra(Constants.SELECTED_SUB_CATEGORY, subCategoryPosition)
            activity.startActivityForResult(intent, Constants.UPDATE_FILTER)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_by_category_party)
        presenter.onCreate()

        configureFilter(hasFilterOn())
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun configureToolbar() {
        toolbarFilter.setTitle(R.string.filter_search)
        toolbarFilter.setOnBackButtonClicked(R.drawable.ic_close_orange_24dp) {
            onBackPressed()
        }
    }

    override fun configureView() {
        filter_button.setOnClickListener {
            Analytics.instance.logClickEvent(getString(R.string.filter_value))

            chipTagAdapter?.tagList?.forEach {
                if (it.isSelected) {
                    searchFilterRequest?.tags?.add(it.tag)
                }
            }

            val filtersBundle = Bundle()
            filtersBundle.putSerializable(Constants.FILTER, searchFilterRequest)
            filtersBundle.putSerializable(Constants.FILTER_TIME_PERIOD, searchFilterTimePeriod)

            val data = Intent()
            data.putExtras(filtersBundle)
            setResult(RESULT_OK, data)
            finish()
        }

        buttonClear.setOnClickListener {
            Analytics.instance.logClickEvent(getString(R.string.clean_value))
            clearFilter()

            val filtersBundle = Bundle()
            filtersBundle.putSerializable(Constants.FILTER, searchFilterRequest)
            filtersBundle.putSerializable(Constants.FILTER_TIME_PERIOD, searchFilterTimePeriod)

            val data = Intent()
            data.putExtras(filtersBundle)
            setResult(RESULT_OK, data)
            finish()
        }

        txtDateFrom.setOnClickListener {
            FilterByCategoryHelper.openCalendar(this) { calendarDateLong, calendarDate ->
                if (searchFilterTimePeriod == null) {
                    searchFilterTimePeriod = SearchFilterTimePeriod()
                }
                searchFilterTimePeriod?.fromDate = calendarDateLong
                searchFilterTimePeriod?.isCustomDate = true
                txtDateFrom.text = calendarDate
                txtDateFrom.isSelected = true
                configureFilter(true)
            }
        }

        txtDateTo.setOnClickListener {
            FilterByCategoryHelper.openCalendar(this, searchFilterTimePeriod?.toDate) { calendarDateLong, calendarDate ->
                if (searchFilterTimePeriod == null) {
                    searchFilterTimePeriod = SearchFilterTimePeriod()
                }
                searchFilterTimePeriod?.toDate = calendarDateLong
                searchFilterTimePeriod?.isCustomDate = true
                txtDateTo.text = calendarDate
                txtDateTo.isSelected = true
                configureFilter(true)
            }
        }
    }

    override fun configureExtras() {
        if (intent.extras.get(Constants.FILTER) != null) {
            searchFilterRequest = intent.extras.get(Constants.FILTER) as SearchFilter
            categoryId = searchFilterRequest!!.categoryId!!
        }

        if (searchFilterRequest?.priceRating != null) {

            rangebar.setRangePinsByValue(searchFilterRequest!!.priceRating!!.get(0).toFloat(),
                    searchFilterRequest!!.priceRating!!.get(1).toFloat())
            configureFilter(hasFilterOn())
        }

        if (intent.extras.get(Constants.FILTER_TIME_PERIOD) != null) {
            searchFilterTimePeriod = intent.extras.get(Constants.FILTER_TIME_PERIOD) as SearchFilterTimePeriod

            if (searchFilterTimePeriod?.fromDate != null) {
                txtDateFrom.setText(DatePickerHelper.getDate(searchFilterTimePeriod!!.fromDate!!))
                txtDateFrom.isSelected = true
                configureFilter(hasFilterOn())
            }

            if (searchFilterTimePeriod?.toDate != null) {
                txtDateTo.setText(DatePickerHelper.getDate(searchFilterTimePeriod!!.toDate!!))
                txtDateTo.isSelected = true
                configureFilter(hasFilterOn())
            }
        }

        presenter.receiveSelectedSubCategories(intent?.extras?.get(Constants.SELECTED_SUB_CATEGORY))
        if (intent.extras.get(Constants.FILTER_PARTY) != null) {
            presenter.receiveSubCategories(intent.extras.get(Constants.FILTER_PARTY) as ResponseSubcategories)
            configureFilter(hasFilterOn())
        }
    }

    override fun receiveSubCategories(subcategories: ResponseSubcategories, selectedSubCategory: Int?) {
        subCategoriesAdapter = SubCategoriesFilterPartyAdapter(subcategories.subcategories)
        selectedSubCategory?.let {
            subCategoriesAdapter?.selectedPosition = selectedSubCategory
        }

        subCategoriesAdapter?.onClickListener = {
            searchFilterRequest?.subCategoriesId = it?.subcategoryId
            configureFilter(hasFilterOn())
        }

        recycler_view_subcategories.layoutManager = GridLayoutManager(this, 2)
        recycler_view_subcategories.adapter = subCategoriesAdapter
        recycler_view_subcategories.isNestedScrollingEnabled = false

        if (subcategories.subcategories.size == 1) {
            recycler_view_subcategories.gone()
        } else {
            recycler_view_subcategories.visible()
        }
    }

    override fun receiveChipTags(chiptagsList: ChipTagResponse) {
        chipTagAdapter = ChipTagAdapter(chiptagsList.chitTagsList!!.toMutableList())
        recycler_view_tags.layoutManager = GridLayoutManager(this, 2)
        recycler_view_tags.adapter = chipTagAdapter
        recycler_view_tags.visible()
        textViewConveniece.visible()
    }

    @SuppressLint("SetTextI18n")
    override fun configureRangeBar() {
        when (searchFilterRequest?.categoryId) {
            CLUBS -> { configurePlaces(rangebar) }
            RESTAURANTS -> { configurePlaces(rangebar) }
            else -> { configureEvents(search_price_rangebar) }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun configureEvents(rangeBarPrice: SeekBar) {
        dateLabelFrom.visible()
        txtDateFrom.visible()
        search_price_rangebar.visible()
        dateLabelTo.visible()
        txtDateTo.visible()
        rangebar.gone()
        recycler_view_tags.gone()
        textViewConveniece.gone()

        val constraintSet = ConstraintSet()
        constraintSet.connect(R.id.textViewConveniece, ConstraintSet.TOP, R.id.search_price_rangebar, ConstraintSet.BOTTOM, 0)
        constraintSet.applyTo(constraint_filter)

        if (searchFilterRequest?.highestPrice == null) {
            textViewLabelDollar.text = "${getString(R.string.value_up_to)} R$ ${rangeBarPrice.progress.formatFloatTwoDigits()}"
        }else{
            rangeBarPrice.progress = searchFilterRequest?.highestPrice?.toInt() ?: 0
            textViewLabelDollar.text = "${getString(R.string.value_up_to)} R$ ${searchFilterRequest?.highestPrice}"
        }

        rangeBarPrice.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                textViewLabelDollar.text = "${getString(R.string.value_up_to)} R$ ${progress.formatFloatTwoDigits()}"
                searchFilterRequest?.lowestPrice = "0"
                searchFilterRequest?.highestPrice = progress.toString()
                configureFilter(hasFilterOn())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    @SuppressLint("SetTextI18n")
    private fun configurePlaces(rangeBarPrice: RangeBar) {
        dateLabelFrom.gone()
        txtDateFrom.gone()
        dateLabelTo.gone()
        txtDateTo.gone()
        rangebar.visible()
        search_price_rangebar.gone()

        rangeBarPrice.setOnRangeBarChangeListener { rangeBar, leftPinIndex, rightPinIndex, leftPinValue, rightPinValue ->
            var startValue = ""
            var endValue = ""

            for (i in 0..leftPinIndex) startValue = "$startValue$"
            for (i in 0..rightPinIndex) endValue = "$endValue$"
            textViewLabelDollar.text = "$startValue ${getString(R.string.range_dollar)} $endValue"

            searchFilterRequest?.priceRating = ArrayList()
            searchFilterRequest!!.priceRating!!.add(leftPinValue.toInt())
            searchFilterRequest!!.priceRating!!.add(rightPinValue.toInt())
            configureFilter(true)
        }
    }

    override fun configureFilter(isActivated: Boolean) {
        if (isActivated) {
            buttonClear.visible()
        }else{
            buttonClear.gone()
        }
    }

    private fun clearFilter() {
        searchFilterRequest = SearchFilter()
        searchFilterRequest?.categoryId = categoryId
        searchFilterTimePeriod = null
        searchFilterRequest?.tags?.clear()
    }

    private fun hasFilterOn() =
            searchFilterRequest?.hasFilterEnabled() ?: false || searchFilterTimePeriod != null
}
