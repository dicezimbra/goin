package br.com.goin.feature.feed.post

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.work.Data
import br.com.goin.base.BaseApplication
import com.google.gson.Gson
import java.io.ByteArrayOutputStream


private const val DIRECTORY_OUTPUTS = "outputs"

object PostActivityHelper {

    fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(columnIndex)
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
    }

    fun getPhotoUriRealPath(contentURI: Uri?): String? {
        var path: String? = ""
        if (contentURI == null) {
            return path
        }
        val cursor = BaseApplication.instance.context?.contentResolver?.query(contentURI, null, null, null, null)

        if (cursor == null) {
            path = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            path = cursor.getString(idx)

        }
        cursor?.close()
        return path
    }


    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, null, null)
        return Uri.parse(path)
    }

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

    fun createData(post: CreatePost, mediaImage: String?, mediaFilePath: String?, id: String?, idClub: String?): Data {

        val postData = Gson().toJson(post)

        return Data.Builder()
                .putString("postData", postData)
                .putString("mediaImageData", mediaImage)
                .putString("mediaFilePathData", mediaFilePath)
                .putString("idData", id)
                .putString("idClub", idClub)
                .build()

    }


}