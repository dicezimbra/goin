package br.com.goin.feature.home.goin.adapter

import androidx.constraintlayout.widget.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.bumptech.glide.Glide

import br.com.goin.component.model.event.Banner
import br.com.goin.feature.home.R
import br.com.goin.feature.home.helper.ImageUtils
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade

class GoinPagerAdapter(private val list: List<Banner>) : androidx.viewpager.widget.PagerAdapter() {
    var onClickListener: ((banner: Banner) -> Unit)? = null

    override fun getCount(): Int {
        return if (list.size > 3) {
            3
        } else list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val context = container.context
        val itemView = LayoutInflater.from(context).inflate(R.layout.events_slider, container, false)
        val image = itemView.findViewById<ImageView>(R.id.imageViewWelcome)

        Glide.with(context)
                .load(list[position].url)
                .transition(withCrossFade())
                .into(image)

        ImageUtils.resizeImagesOnScreen(image, 0.49f)
        container.addView(itemView)

        itemView.setOnClickListener {
            onClickListener?.invoke(list[position])
        }

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}
