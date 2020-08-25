package br.com.goin.feature.profile.edit

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
import androidx.exifinterface.media.ExifInterface
import br.com.goin.base.BaseApplication
import com.theartofdev.edmodo.cropper.CropImage
import java.io.ByteArrayOutputStream
import java.io.File

object ProfileHelper {

    private val TAG = ProfileHelper::class.java.simpleName

    fun getRealPathFromURI(uri: Uri): String {
        var path = ""
        if (BaseApplication.instance.context != null) {
            val cursor = BaseApplication.instance.context?.contentResolver?.query(uri, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, null, null)
        return Uri.parse(path)
    }


    fun getBitmap(context: Context, uri: Uri?, with: Int, height: Int): Bitmap? {
        return if (uri != null) decodeUriAsBitmap(context, uri, with, height) else null
    }

    fun getMediumImage(context: Context, uri: Uri): Bitmap? {
        return getBitmap(context, uri, 600, 600)
    }

    fun decodeUriAsBitmap(context: Context, uri: Uri, targetWidth: Int, targetHeight: Int): Bitmap? {
        var targetWidth = targetWidth
        var targetHeight = targetHeight
        var bitmap: Bitmap? = null
        val rotationInDegrees: Int
        try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true

            BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri), null, options)

            if (targetHeight == 0) {
                targetHeight = (options.outHeight * targetWidth / options.outWidth.toDouble()).toInt()
            }

            if (targetWidth == 0) {
                targetWidth = (options.outWidth * targetHeight / options.outHeight.toDouble()).toInt()
            }

            options.inSampleSize = calculateInSampleSize(options, targetWidth, targetHeight)

            options.inJustDecodeBounds = false
            bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri), null, options)

            val exif = getExifFromImageUri(uri, context)

            rotationInDegrees = exifToDegrees(exif)
            if (rotationInDegrees > 0) {
                val matrix = Matrix()
                matrix.preRotate(rotationInDegrees.toFloat())
                bitmap = Bitmap.createBitmap(bitmap!!, 0, 0, bitmap.width, bitmap.height, matrix, true)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Decoding from Uri")
        }

        return bitmap
    }

    fun cropImage(activity: Activity, uri: Uri) {
        CropImage.activity(uri).start(activity)
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    fun exifToDegrees(exifInterface: ExifInterface?): Int {
        var rotation = 0
        if (exifInterface == null) return rotation
        val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotation = 90
            ExifInterface.ORIENTATION_ROTATE_180 -> rotation = 180
            ExifInterface.ORIENTATION_ROTATE_270 -> rotation = 270
        }
        return rotation
    }

    fun getExifFromImageUri(uri: Uri, context: Context): ExifInterface? {
        var exifInterface: ExifInterface? = null
        try {
            exifInterface = ExifInterface(context.contentResolver.openInputStream(uri)!!)
        } catch (ex: Exception) {
            Log.d(TAG, ex.message, ex)
        }

        return exifInterface
    }

    fun getImageContentUri(context: Context, imageFile: File): Uri? {
        val filePath = imageFile.absolutePath
        val cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Images.Media._ID),
                MediaStore.Images.Media.DATA + "=? ",
                arrayOf(filePath), null)
        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
            cursor.close()
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id)
        } else {
            if (imageFile.exists()) {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.DATA, filePath)
                return context.contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                return null
            }
        }
    }

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