package br.com.goin.feature.home.helper

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnPreDraw
import androidx.exifinterface.media.ExifInterface
import br.com.goin.component.ui.kit.features.error.ErrorConstraintLayout
import com.jakewharton.rxbinding3.view.preDraws
import com.theartofdev.edmodo.cropper.CropImage
import java.io.File

class ImageUtils {

    companion object {

        private val TAG = ImageUtils::class.java.simpleName

        fun resizeImagesOnScreen(view: View, scale: Float) {
            val viewTreeObserver = view.viewTreeObserver
            viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    view.viewTreeObserver.removeOnPreDrawListener(this)
                    val height = view.measuredWidth * scale

                    if (view.layoutParams is ConstraintLayout.LayoutParams) {
                        val layoutParams = view.layoutParams
                        layoutParams.height = height.toInt()
                        view.layoutParams = layoutParams
                    }
                    if (view.layoutParams is FrameLayout.LayoutParams) {
                        view.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height.toInt())
                    }
                    return false
                }
            })
        }
    }
}
