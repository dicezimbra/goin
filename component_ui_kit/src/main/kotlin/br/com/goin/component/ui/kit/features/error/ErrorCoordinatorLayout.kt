package br.com.goin.component.ui.kit.features.error

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.children
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.invisible
import br.com.goin.base.extensions.visible
import br.com.goin.component.ui.kit.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.view_error.view.*

class ErrorCoordinatorLayout: CoordinatorLayout{

    private var errorView: View? = null

    var onClickRetry: () -> Unit = {}

    init {
        if(!this.isInEditMode){
            initializeErrorView(context)
        }
    }

    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int ): super(context, attrs, defStyleAttr)

    fun hideError(){
        children.filterNot{ it is Toolbar }
                .filter { it.visibility == View.INVISIBLE }
                .forEach { it.visible() }
        errorView?.gone()
    }

    fun showError(){
        children.filterNot { it is Toolbar }.forEach { it.invisible() }
        errorView?.visible()
    }

    fun configureInternetError(){
        errorView?.apply {
            Glide.with(this).load(R.drawable.image_internet_error).into(image)
            message?.text = context.getString(R.string.error_not_internet)
        }
    }

    fun configureUnknownError(){
        errorView?.apply {
            Glide.with(this).load(R.drawable.image_cloud_error).into(image)
            message?.text = context.getString(R.string.unknow_error)
        }
    }

    private fun initializeErrorView(context: Context) {
        errorView = LayoutInflater.from(context).inflate(R.layout.view_error, this, false)
        errorView?.gone()
        addView(errorView, childCount - 1)

        retry_button.setOnClickListener {
            hideError()
            onClickRetry()
        }
    }
}