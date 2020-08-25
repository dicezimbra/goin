package br.com.goin.component.ui.kit.toolbar

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.text.TextUtils
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.appcompat.widget.Toolbar
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import androidx.annotation.ColorRes
import br.com.goin.component.ui.kit.R
import kotlinx.android.synthetic.main.view_toolbar.view.*

class GoinToolbar : Toolbar, GoinToolbarView {

    private val presenter: GoinToolbarPresenter = GoinToolbarPresenterImpl(this)

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if(!isInEditMode) {
            presenter.onCreate()
            initAttributes(attrs)
        }
    }

    @SuppressLint("CustomViewStyleable")
    override fun initAttributes(attrs: AttributeSet?) {
        val styleAttribute = context.obtainStyledAttributes(attrs, R.styleable.Toolbar)

        val title = styleAttribute.getText(androidx.appcompat.R.styleable.Toolbar_title)
        if (!TextUtils.isEmpty(title)) {
            setTitle(title)
        }

        styleAttribute.recycle()
    }

    override fun createView() {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.view_toolbar, this, true)
    }

    fun setOnBackButtonClicked(@DrawableRes drawableRes: Int? = null, listener: () -> Unit) {
        drawableRes?.let{
            home.setImageDrawable(ContextCompat.getDrawable(context, drawableRes))
        }
        home.setOnClickListener { listener() }
    }

    fun setTitleImage(@DrawableRes drawableRes: Int, onClickListener: () -> Unit) {
        title_image.visibility = View.VISIBLE
        title_image.setImageDrawable(ContextCompat.getDrawable(context, drawableRes))
        title_image.setOnClickListener { onClickListener() }
        tv_title.setOnClickListener { onClickListener() }
    }

    fun setBackButtonWhite() {
        home.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_arrow_white_24dp))
    }

    fun setRightButton(@DrawableRes drawableRes: Int, onClickListener: () -> Unit) {
        button_right.visibility = View.VISIBLE
        button_right.setImageDrawable(ContextCompat.getDrawable(context, drawableRes))
        button_right.setOnClickListener { onClickListener() }
    }

    fun getRightButton(): ImageButton {
        return button_right
    }

    fun showRightButton() {
        button_right.visibility = View.VISIBLE
    }

    fun setRightSecondButton(@DrawableRes drawableRes: Int, onClickListener: () -> Unit) {
        button_right_second.visibility = View.VISIBLE
        button_right_second.setImageDrawable(ContextCompat.getDrawable(context, drawableRes))
        button_right_second.setOnClickListener { onClickListener() }
    }

    fun getRightSecondButton(): ImageButton {
        return button_right_second
    }

    override fun setTitle(resId: Int) {
        tv_title?.text = context.getText(resId)
    }

    fun setTitleColor(@ColorRes color: Int) {
        tv_title?.setTextColor(ContextCompat.getColor(context, color))
    }

    override fun setTitle(title: CharSequence?) {
        title?.let {
            super.setTitle(title)
            tv_title?.text = title
        }
    }

    fun hideBackButton() {
        home.visibility = View.INVISIBLE
    }

    fun hideRightSecondButton() {
        button_right_second.visibility = View.GONE
    }

    fun hideRightButton() {
        button_right.visibility = View.INVISIBLE
    }
}