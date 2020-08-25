package br.com.goin.feature.theaters.plays.movies.theatherPlays.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter

import com.bumptech.glide.load.resource.bitmap.RoundedCorners

import br.com.goin.feature.theaters.R
import br.com.goin.feature.theaters.plays.movies.model.PlayModel
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions


class TheaterPlaysHighlightAdapter(private var plays: List<PlayModel>, var context: Context) : PagerAdapter() {

    var onClickListener: ((playModel: PlayModel) -> Unit)? = null

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
       return view == `object`
    }

    override fun getCount(): Int {
        return plays.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val playModel = plays[position]
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.view_theaters_item, container, false) as ImageView

        Glide.with(container.context)
                .load(playModel.image)
                .transition(withCrossFade())
                .thumbnail(0.5f)
                .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(16)).placeholder(R.drawable.background_placeholder_landscape))
                .into(layout)

        layout.setOnClickListener{
            onClickListener?.invoke(playModel)
        }

        container.addView(layout)
        return layout
    }

    override fun getItemPosition(`object`: Any): Int {
        return super.getItemPosition(`object`)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }
}
