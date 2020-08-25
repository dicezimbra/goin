package br.com.legacy.managers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import br.com.goin.component.ui.kit.dialog.DialogUtils;
import br.com.goin.component.session.user.UserModel;
import br.com.legacy.utils.ImageUploader;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by appsimples on 10/9/17.
 */

public class ProfilePictureManager extends BaseManager {

    public interface UploadPictureHandler {
        void onPictureUploadSuccess(String url);
        void onFailure(String error);
    }

    public interface FbPictureHandler {
        void onFbBitmapReceieved(Bitmap url);
        void onFbbUploadSuccess();
        void onFbUploadFailure(String error);
    }

    public UploadPictureHandler handler;
    public FbPictureHandler fbHandler;
    ProgressDialog progressDialog;

    public ProfilePictureManager(Activity activity) {
        super(activity);
        progressDialog = DialogUtils.INSTANCE.createProgressDialog(activity);
        progressDialog.hide();
    }


    public void uploadImage(Bitmap image) {

        progressDialog.show();

        ImageUploader.ImageUploadListener listener = new ImageUploader.ImageUploadListener() {
            @Override
            public void onComplete(String key) {
                if (handler != null) {
                    handler.onPictureUploadSuccess(key);
                }
                progressDialog.hide();
            }

            @Override
            public void onProgressChanged(int progress) {

            }

            @Override
            public void onError(String error) {
                if (handler != null) {
                    handler.onFailure(error);
                }
                progressDialog.hide();
            }
        };

        // try to upload the image
        try {
           // TransferUtility utility = ImageUploader.uploadToS3(activity, image, ImageUploader.buildUserProfilePictureKey(), listener);
        } catch (Exception e) {
            if (handler != null) {
                handler.onFailure(e.getMessage());
            }
            progressDialog.hide();
        }

    }

    public void chooseFbImage() {
        if (AccessToken.getCurrentAccessToken() == null) {
            fbHandler.onFbUploadFailure("Você não está conectado com o facebook");
            return;
        }
        progressDialog.show();
        final UserModel model = new UserModel();
        // request user eventSessionDetailInfo from facebook api
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String fbId = object.getString("id");
                            String pictureUrl = "http://graph.facebook.com/"+fbId+"/picture?type=large";
                            downloadFbPicture(pictureUrl);
                        } catch (JSONException e) {
                            fbHandler.onFbUploadFailure(e.getMessage());
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void downloadFbPicture(String url) {

        Glide.with(activity)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        if (fbHandler != null) {
                            fbHandler.onFbBitmapReceieved(resource);
                        }
                        progressDialog.hide();
                    }
                });
    }

    public void uploadFbPicture(Bitmap image) {

        ImageUploader.ImageUploadListener listener = new ImageUploader.ImageUploadListener() {
            @Override
            public void onComplete(String key) {
                if (handler != null) {
                    fbHandler.onFbbUploadSuccess();
                }
            }

            @Override
            public void onProgressChanged(int progress) {

            }

            @Override
            public void onError(String error) {
                if (handler != null) {
                    fbHandler.onFbUploadFailure(error);
                }
                progressDialog.hide();
            }
        };

        // try to upload the image
        try {
           // TransferUtility utility = ImageUploader.uploadToS3(activity, image, ImageUploader.buildUserProfilePictureKey(), listener);
        } catch (Exception e) {
            if (handler != null) {
                handler.onFailure(e.getMessage());
            }
            progressDialog.hide();
        }
    }

}
