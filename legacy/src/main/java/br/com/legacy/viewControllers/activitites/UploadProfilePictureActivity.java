package br.com.legacy.viewControllers.activitites;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.FragmentTransaction;
import br.com.goin.component.ui.kit.dialog.DialogUtils;
import br.com.legacy.managers.ProfilePictureManager;
import br.com.goin.component.analytics.adjust.TagManager;
import br.com.legacy.navigation.Coordinator;
import br.com.R;
import br.com.legacy.utils.ImageUtils;
import br.com.legacy.utils.ToolbarConfigurator;
import com.outsmart.outsmartpicker.MediaPicker;
import com.outsmart.outsmartpicker.MediaType;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

import br.com.legacy.utils.TypefaceUtil;
import br.com.legacy.viewControllers.activitites.base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadProfilePictureActivity extends BaseActivity implements ProfilePictureManager.UploadPictureHandler, ProfilePictureManager.FbPictureHandler {

    @BindView(br.com.R2.id.btn_choose_picture)
    Button choosePicture;
//    @BindView(br.com.goin.R2.id.btn_choose_fb_picture)
//    Button chooseFbPicture;
    @BindView(br.com.R2.id.btn_not_now)
    Button notNow;
    @BindView(br.com.R2.id.profile_user_image)
    ImageView profileImage;
    @BindView(br.com.R2.id.btn_save_picture)
    Button savePicture;
    @BindView(br.com.R2.id.toolbar)
    View toolbar;
    @BindView(br.com.R2.id.toolbar_title)
    View toolbarTitle;

    MediaPicker mediaPicker;
    ProfilePictureManager manager;
    Bitmap newPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_picture);
        ButterKnife.bind(this);
        Coordinator.setStatusBarColor(this,null);

        configFont();

        mediaPicker = new MediaPicker();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mediaPicker = new MediaPicker();
        transaction.add(mediaPicker, "mediaPicker");
        transaction.commit();

        registerReceiver(pickerChoose, new IntentFilter(MediaPicker.PICKER_RESPONSE_FILTER));

        manager = new ProfilePictureManager(this);
        manager.handler = this;
        manager.fbHandler = this;

        // check if user is connected to facebook
//        if (SessionManager.getInstance().facebookConnected) {
//            chooseFbPicture.setVisibility(View.VISIBLE);
//        }

        ToolbarConfigurator.INSTANCE.configToolbar(toolbar, getString(R.string.choose_your_profile_picture), R.drawable.ic_arrow_back, 0);
    }

    BroadcastReceiver pickerChoose = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String filePath = intent.getStringExtra(MediaPicker.PICKER_INTENT_FILE);
            if(filePath != null) {
                File file = new File(filePath);
                ImageUtils.cropImage(UploadProfilePictureActivity.this, ImageUtils.getImageContentUri(UploadProfilePictureActivity.this, file));
            } else {
                DialogUtils.INSTANCE.show(UploadProfilePictureActivity.this,getString(R.string.error_uploading_image));
            }


            tagManager.trackAdjustEvent(TagManager.TOKEN_WELCOME);
        }
    };

    public void configFont(){
        TypefaceUtil.regularFont(choosePicture, notNow, toolbar, toolbarTitle, savePicture);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(pickerChoose);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                newPhoto = ImageUtils.getMediumImage(UploadProfilePictureActivity.this, resultUri);
                profileImage.setImageBitmap(newPhoto);
                choosePicture.setText(R.string.choose_another_picture);
                savePicture.setVisibility(View.VISIBLE);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void onPictureUploadSuccess(String url) {
        Coordinator.goToTabsMain(this);
        finish();
    }

    @Override
    public void onFailure(String error) {
        DialogUtils.INSTANCE.show(this, error);
    }

    @Override
    public void onFbBitmapReceieved(Bitmap image) {
        profileImage.setImageBitmap(image);
        newPhoto = image;
        savePicture.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFbbUploadSuccess() {

    }

    @Override
    public void onFbUploadFailure(String error) {

    }

    @OnClick({br.com.R2.id.btn_choose_picture})
    public void onChoosePicture() {
        try {
            mediaPicker.pickMediaWithPermissions(MediaType.IMAGE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    @OnClick({br.com.goin.R2.id.btn_choose_fb_picture})
//    public void onChooseFbPicture() {
//        manager.chooseFbImage();
//    }

    @OnClick({br.com.R2.id.btn_not_now})
    public void onChooseNotNow() {
        Coordinator.goToTabsMain(this);
        finish();
    }

    @OnClick({br.com.R2.id.btn_save_picture})
    public void onSavePicture() {
        manager.uploadImage(newPhoto);
    }


}
