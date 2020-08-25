package br.com.goin.feature.search.event

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import br.com.goin.feature.search.event.model.api.SearchEvent


class SearchPagerAdapter(private val context: Context?, var searchResponse: LinkedHashMap<String, MutableList<SearchEvent>>,
                         fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

    var indexes: List<String> = ArrayList<String>(searchResponse.keys)

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return SearchFragment.newInstance(searchResponse[indexes[position]]!!)
    }

    override fun getCount(): Int {
        return searchResponse.size
    }

    override fun getItemPosition(any: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return indexes[position]
    }

}