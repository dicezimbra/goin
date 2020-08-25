package br.goin.features.login.signUp.choosePicture

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Images
import androidx.core.app.ActivityCompat
import br.com.goin.base.BaseApplication
import br.com.goin.base.helpers.ActivityHelper
import br.com.goin.base.helpers.DialogPermissionHelper
import br.com.goin.base.utils.GpsUtils
import br.com.goin.component.session.user.UserSessionInteractor
import br.goin.feature.login.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

private val permissionsArray: Array<String> = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
private val requestLocationCode: Int = 1

object ChooseProfileHelper {


    private const val BUCKET_NAME = "goin-public-dev"
    private const val BUCKET_PATH = "https://goin-public-dev.s3-accelerate.amazonaws.com/"


    private val userInteractor = WeakReference<UserSessionInteractor>(UserSessionInteractor.instance)

    interface ImageUploadListener {
        fun onComplete(key: String)
        fun onError(error: String)
        fun onProgressChanged(progress: Int)
    }

 /*   @Throws(IOException::class)
    fun uploadToS3(image: Bitmap, key: String, listener: ImageUploadListener): TransferUtility {

        //create a file to write bitmap data
        val f = File(BaseApplication.instance.context?.cacheDir, UUID.randomUUID().toString())
        f.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, bos)
        val bitmapData = bos.toByteArray()

        //write the bytes in file
        val fos = FileOutputStream(f)
        fos.write(bitmapData)
        fos.flush()
        fos.close()

        val credentials = BasicAWSCredentials(AMAZON_S3_KEY, AMAZON_S3_SECRET)
        val s3Client = AmazonS3Client(credentials)

        val utility = TransferUtility(s3Client, BaseApplication.instance.context)
        val observer = utility.upload(BUCKET_NAME, key, f)


        observer.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state.compareTo(TransferState.COMPLETED) == 0) {
                    listener.onComplete(BUCKET_PATH + key)
                }
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                listener.onProgressChanged(Math.round((bytesCurrent * 100 / bytesTotal).toFloat()))
            }

            override fun onError(id: Int, ex: Exception) {
                listener.onError("Error: " + ex.message)
            }
        })

        return utility
    }

*/

    fun getLocationPermissions(activity: Activity, callbackLocation: (Location) -> Unit, errorCallback: (java.lang.Exception) -> Unit) {

        if (!ActivityHelper.hasPermissions(activity, permissionsArray)) {
            DialogPermissionHelper.permissionsMessage(activity, activity.getString(R.string.needed_permissions_check_in)) {
                ActivityCompat.requestPermissions(activity, permissionsArray, requestLocationCode)
            }
        } else {
            GpsUtils.getCurrentLocation(activity,
                    { callbackLocation(it) }, { errorCallback(it) })
        }

    }

    fun onRequestLocationResult(activity: Activity, requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
            GpsUtils.getCurrentLocation(activity, { print(it) }, { print(it) })
        }
    }

    fun getGalleryPermissions(activity: Activity, permissionsArray: Array<String>,
                              requestCode: Int, callback: () -> Unit) {
        if (!ActivityHelper.hasPermissions(activity, permissionsArray)) {
            ActivityCompat.requestPermissions(activity, permissionsArray, requestCode)
        } else {
            callback.invoke()
        }
    }

    fun onGalleryResult(grantResults: IntArray, callbackYes: () -> Unit, callbackNo: () -> Unit) {
        if (grantResults.isNotEmpty() && grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
            callbackYes.invoke()
        } else {
            callbackNo.invoke()
        }
    }

}
