package br.com.goin.component.rest.api.helpers

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import br.com.goin.base.BaseApplication
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


object ImageHelper {

    fun getImageUri( inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(BaseApplication.instance.context?.contentResolver, inImage, null, null)
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

    fun getFile(picture: Bitmap): File {
        val tempUri: Uri = ImageHelper.getImageUri( picture)
        val fileFinal = File(getRealPathFromURI(tempUri))

        val bos = ByteArrayOutputStream()
        picture.compress(Bitmap.CompressFormat.PNG, 100, bos)
        val bitmapData = bos.toByteArray()

        val fos = FileOutputStream(fileFinal)
        fos.write(bitmapData)
        fos.flush()
        fos.close()

        return fileFinal
    }
}
