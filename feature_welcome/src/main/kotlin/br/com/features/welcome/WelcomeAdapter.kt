package br.com.features.welcome

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.feature_welcome.R
import kotlinx.android.synthetic.main.welcome_slider.view.*

class WelcomeAdapter(private val context: Context) : androidx.viewpager.widget.PagerAdapter() {

    private val arrayPin: Array<Int> = arrayOf(
            R.drawable.pin_icon_1,
            R.drawable.pin_icon_2,
            R.drawable.pin_icon_3,
            R.drawable.pin_icon_4)

    private var arrayDescription: Array<String>? = null
    private var arrayTitle: Array<String>? = null
    private val arrayImages = intArrayOf(R.drawable.social, R.drawable.facilitador, R.drawable.organizador, R.drawable.guia)

    var signUpClickListener: (()-> Unit)? = null
    var notNowClickListener: (()-> Unit)? = null

    init {
        arrayDescription = context.resources.getStringArray(R.array.welcome_activity_array)
        arrayTitle = context.resources.getStringArray(R.array.welcome_title_activity_array)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return arrayImages.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.welcome_slider, container, false)

        view.imageViewPin.setImageResource(arrayPin[position])
        view.imageViewWelcome.setImageResource(arrayImages[position])
        view.title_info_welcome.text = arrayTitle?.get(position)
        view.info_welcome.text = arrayDescription?.get(position)

        view.btn_sign_up_welcome.setOnClickListener {
            signUpClickListener?.invoke()
        }
        view.btn_not_now_welcome.setOnClickListener {
            notNowClickListener?.invoke()
        }

        container.addView(view)

        return view
    }

    interface IClickAdapter {
        fun onStartActivity(view: View)
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }

}