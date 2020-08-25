package br.com.goin.feature.search.category.filter.component

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import br.com.goin.feature.search.category.R


internal class TagView(context: Context) : FrameLayout(context) {

    var textView: TextView? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.tag_view, this)
        textView = rootView.findViewById<View>(R.id.textView) as TextView
    }

    fun display(text: String, isSelected: Boolean) {
        textView!!.text = text
        display(isSelected)
    }

    fun display(isSelected: Boolean) {
        textView!!.setBackgroundColor(if (isSelected) Color.RED else Color.LTGRAY)
    }
}