package br.com.goin.feature.search.category

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import br.com.goin.feature.search.category.model.SearchFilter
import br.com.goin.feature.search.category.model.SearchFilterTimePeriod
import br.com.goin.feature.search.category.tab.SearchByCategoryTabAdapter
import br.com.goin.feature.search.category.tab.SearchByCategoryTabFragment

class SearchByCategoryAdapter(fm: FragmentManager,
                              val context: Context,
                              var viewType: SearchByCategoryTabAdapter.VIEW_MODE,
                              val searchFilter: SearchFilter,
                              val searchFilterTimePeriod: List<SearchFilterTimePeriod>) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val period = searchFilterTimePeriod[position]
        return SearchByCategoryTabFragment.newInstance(searchFilter, viewType, period)
    }

    override fun getCount(): Int{
        return searchFilterTimePeriod.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> context.getString(R.string.all)
            1 ->  context.getString(R.string.today)
            2 ->  context.getString(R.string.tomorrow)
            3 ->  context.getString(R.string.weekend)
            4 ->  context.getString(R.string.week)
            5 ->  context.getString(R.string.month)
            6 ->  context.getString(R.string.next_month)
            else -> ""
        }
    }

    override fun getItemPosition(`object`: Any): Int {
        return FragmentStatePagerAdapter.POSITION_NONE
    }
}
