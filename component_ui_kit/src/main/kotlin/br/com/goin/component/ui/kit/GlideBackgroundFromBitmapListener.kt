package br.com.goin.component.ui.kit

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class GlideBackgroundFromBitmapListener(val imageView: ImageView) : RequestListener<Drawable> {

    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
        return false
    }

    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
        if (resource is BitmapDrawable) {
            val bitmap = resource.bitmap
            PaletteHelper.setBackgroundDarkVibrantColor(bitmap, imageView, imageView.context)
        }

        return false
    }
}