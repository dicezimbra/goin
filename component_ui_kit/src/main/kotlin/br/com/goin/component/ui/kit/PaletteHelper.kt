package br.com.goin.component.ui.kit

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import android.view.ViewGroup
import android.widget.ImageView
import androidx.transition.TransitionManager

object PaletteHelper {

    fun setBackgroundDarkVibrantColor(bitmap: Bitmap, imageView: ImageView, context: Context) {
            androidx.palette.graphics.Palette.from(bitmap).generate { palette ->

                val parent = imageView.parent as? ViewGroup
                parent?.let {
                    TransitionManager.beginDelayedTransition(it)

                    val darkVibrantColor = palette?.getMutedColor(ContextCompat.getColor(context, R.color.black))
                    darkVibrantColor?.let {
                        imageView.setBackgroundColor(it)
                    }
                }

                parent?.let {
                    TransitionManager.endTransitions(it)
                }
        }
    }
}