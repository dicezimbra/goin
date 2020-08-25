package br.com.goin.feature.theaters.plays.movies.theatherPlays.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import br.com.goin.feature.theaters.R
import br.com.goin.feature.theaters.plays.movies.model.PlayModel
import kotlinx.android.synthetic.main.view_event_session_component_list.view.*

class TheaterPlaysPagerAdapter(var inTheather: List<PlayModel> = arrayListOf(),
                               var soonInTheather: List<PlayModel> = arrayListOf()) : androidx.viewpager.widget.PagerAdapter() {

    var onClickListener: ((playModel: PlayModel) -> Unit)? = null

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return o === view
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.view_theaters_list,
                container, false)

        val adapter = TheaterPlaysAdapter()
        if (position == 0) {
            adapter.setList(inTheather)
        } else {
            adapter.setList(soonInTheather)
        }

        view.recycler_view.adapter = adapter
        view.recycler_view.isNestedScrollingEnabled = false
        view.recycler_view.layoutManager = GridLayoutManager(view.context, 2)

        adapter.onClickListener = { movie -> onClickListener?.invoke(movie) }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return 2
    }
}