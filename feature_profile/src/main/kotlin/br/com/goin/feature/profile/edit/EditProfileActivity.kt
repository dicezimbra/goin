package br.com.goin.feature.profile.edit

import android.app.Activity
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.session.user.UserModel
import br.com.goin.component.ui.kit.dialog.DialogUtils
import br.com.goin.component.ui.kit.features.error.ErrorDialog
import br.com.goin.component.ui.kit.features.error.ErrorViewHelper
import br.com.goin.feature.profile.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.outsmart.outsmartpicker.MediaPicker
import com.outsmart.outsmartpicker.MediaType
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.edit_profile_activity.*
import java.io.File

class EditProfileActivity : AppCompatActivity(), EditProfileView {


    private val presenter: EditProfilePresenter = EditProfilePresenterImpl(this)
    private var bitmap: Bitmap? = null
    private var mediaPicker = MediaPicker()
    private var progressDialog: ProgressDialog? = null
    companion object {
        fun starter(activity: Activity) {
            val intent = Intent(activity, EditProfileActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile_activity)

        presenter.onCreate()

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(mediaPicker, "mediaPicker")
        transaction.commit()

        registerReceiver(pickerChoose, IntentFilter(MediaPicker.PICKER_RESPONSE_FILTER))
    }

    override fun onResume() {
        super.onResume()
        Analytics.instance.screenView(this, getString(R.string.edit_profile_screen_name))
    }

    override fun configureClickListeners() {
        edit_profile_camera.setOnClickListener { mediaPicker.pickMediaWithPermissions(MediaType.IMAGE) }
    }

    private var pickerChoose: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val filePath = intent.getStringExtra(MediaPicker.PICKER_INTENT_FILE)
            if (filePath != null) {
                val file = File(filePath)
                ProfileHelper.getImageContentUri(this@EditProfileActivity, file)?.let { ProfileHelper.cropImage(this@EditProfileActivity, it) }
            } else {
                Toast.makeText(this@EditProfileActivity, getString(R.string.error_uploading_image), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(pickerChoose)

    }

    override fun loadUserInfo(it: UserModel) {
        Glide.with(this)
                .load(it.profilePicture)
                .apply(RequestOptions().placeholder(R.drawable.background_circle_placeholder))
                .apply(RequestOptions.circleCropTransform())
                .into(edit_profile_img)
        edit_profile_name_field.setText(it.name)
        edit_profile_email_field.setText(it.email)
    }

    override fun configureToolbar() {
        toolbar_edit_profile.setRightButton(R.drawable.outline_done_white_24dp) { presenter.updateProfile(edit_profile_name_field.text.toString(), bitmap) }
        toolbar_edit_profile.title = getString(R.string.edit_profile)
        toolbar_edit_profile.setOnBackButtonClicked { onBackPressed() }
    }

    override fun onSuccessUpdate() {
        progressDialog?.let {
            it.hide()
            progressDialog = null
        }
        Toast.makeText(this@EditProfileActivity, getString(R.string.profile_updated_successfully), Toast.LENGTH_SHORT).show()
//        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                bitmap = ProfileHelper.getMediumImage(this@EditProfileActivity, resultUri)
                Glide.with(this)
                        .load(bitmap)
                        .apply(RequestOptions().placeholder(R.drawable.background_circle_placeholder))
                        .apply(RequestOptions.circleCropTransform())
                        .into(edit_profile_img)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.e("EditProfileException", result.error.toString())
            }
        }

    }


    override fun showLoading() {
        progressDialog?.let {
            it.dismiss()
        }

        progressDialog = DialogUtils.createProgressDialog(this)
        progressDialog?.show()
    }

    override fun showError(e: Throwable?) {
        progressDialog?.let {
            it.hide()
            progressDialog = null
        }
        e?.let {
            ErrorViewHelper.showErrorModal(this, it) {
                presenter.updateProfile(edit_profile_name_field.text.toString(), bitmap)
            }
        }

    }


    override fun showMessageError(id: Int) {
        progressDialog?.let {
            it.hide()
            progressDialog = null
        }

        ErrorViewHelper.showErrorModal(this, getString(id)) {
            presenter.updateProfile(edit_profile_name_field.text.toString(), bitmap)
        }

    }
}