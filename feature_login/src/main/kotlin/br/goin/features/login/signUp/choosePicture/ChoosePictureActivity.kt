package br.goin.features.login.signUp.choosePicture

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.navigation.NavigationController
import br.goin.feature.login.R
import br.goin.features.login.helper.ImageUtils
import com.outsmart.outsmartpicker.MediaPicker
import com.outsmart.outsmartpicker.MediaType
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.choose_profile_picture.*
import java.io.File

private const val requestGalleryCode = 1
private const val PICK_PHOTO = 2

class ChoosePictureActivity : AppCompatActivity(), ChoosePictureView {

    private val permissionsArray: Array<String> = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA)
    private var mediaPicker: MediaPicker? = null
    private var bitmap: Bitmap? = null
    private val presenter: ChoosePicturePresenter = ChoosePicturePresenterImpl(this)

    companion object {
        fun starter(context: Context) {
            val intent = Intent(context, ChoosePictureActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choose_profile_picture)

        presenter.onCreate()

        val transaction = supportFragmentManager.beginTransaction()
        mediaPicker = MediaPicker()
        transaction.add(mediaPicker!!, "mediaPicker")
        transaction.commit()

        registerReceiver(pickerChoose, IntentFilter(MediaPicker.PICKER_RESPONSE_FILTER))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(pickerChoose)
    }

    override fun onResume() {
        super.onResume()
        Analytics.instance.screenView(this, getString(R.string.register_choose_picture_screen_name))
    }

    override fun showLoading() {
        group_views.visibility = View.GONE
        progress_bar_preview.isIndeterminate = true
        progress_bar_preview.visibility = View.VISIBLE
        btn_save_photo.visibility = View.GONE
    }

    override fun configureClickListeners() {
        profile_picture_field.setOnClickListener { askForGalleryPermissions() }
        btn_choose_photo.setOnClickListener { askForGalleryPermissions() }
        btn_choose_not_now.setOnClickListener { NavigationController.instance?.homeModule()?.goToHome(this) }
        btn_save_photo.setOnClickListener { presenter.onSavePhoto(this@ChoosePictureActivity, bitmap) }
    }

    private var pickerChoose: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val filePath = intent.getStringExtra(MediaPicker.PICKER_INTENT_FILE)
            if (filePath != null) {
                val file = File(filePath)
                ImageUtils.getImageContentUri(this@ChoosePictureActivity, file)?.let { ImageUtils.cropImage(this@ChoosePictureActivity, it) }
            } else {
                Toast.makeText(this@ChoosePictureActivity, getString(R.string.error_uploading_image), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onErrorUploadPhoto(e: Exception) {
        Toast.makeText(this@ChoosePictureActivity, getString(R.string.error_uploading_image), Toast.LENGTH_LONG).show()
    }

    override fun openGallery() {
        mediaPicker?.pickMediaWithPermissions(MediaType.IMAGE)
    }

    override fun onPhotoSendSuccess() {
        NavigationController.instance?.homeModule()?.goToHome(this)
    }

    override fun askForGalleryPermissions() {
        ChooseProfileHelper.getGalleryPermissions(this, permissionsArray, requestGalleryCode) {
            openGallery()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        ChooseProfileHelper.onGalleryResult(grantResults, {
            openGallery()
        }, {
            Toast.makeText(this, getString(R.string.permissions_gallery_needed), Toast.LENGTH_SHORT).show()
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                bitmap = ImageUtils.getMediumImage(this@ChoosePictureActivity, resultUri)
                profile_picture_field.setImageBitmap(bitmap)
                btn_save_photo.visibility = View.VISIBLE
                btn_choose_photo.background = ContextCompat.getDrawable(this, R.drawable.button_preview)
                btn_choose_photo.setTextColor(ContextCompat.getColor(this, R.color.grapefruit2))
                btn_choose_not_now.visibility = View.GONE
                icon_profile.visibility = View.GONE


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.e("ChooseProfileException", result.error.toString())
                icon_profile.visibility = View.VISIBLE
            }
        }
    }

}