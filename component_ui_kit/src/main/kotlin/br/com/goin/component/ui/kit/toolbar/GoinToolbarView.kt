package br.com.goin.component.ui.kit.toolbar

import android.annotation.SuppressLint
import android.util.AttributeSet
import androidx.appcompat.widget.Toolbar


interface GoinToolbarView {
    fun createView()
    fun initAttributes(attrs: AttributeSet?)
}
