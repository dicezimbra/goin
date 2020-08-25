package br.com.legacy.managers;

import android.app.Activity;
import android.app.ProgressDialog;

import br.com.goin.component.ui.kit.dialog.DialogUtils;
import br.com.legacy.models.ClubModel;
import br.com.legacy.models.PhotoGalleryModel;
import br.com.legacy.managers.dtos.Club;
import br.com.legacy.managers.dtos.ErrorResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kalunga on 04/10/2017.
 */

public class PhotoGalleryManager extends BaseManager {

    PhotoGalleryRequestHandler viewControllerHandler;
    Activity activity;
    ProgressDialog progressDialog;

    public interface PhotoGalleryRequestHandler{
        void onReceiveGallery(List<PhotoGalleryModel> photoGalleryModel);
        void onFailure(String error);
    }

    public PhotoGalleryRequestHandler photoGalleryRequestHandler;

    public PhotoGalleryManager(Activity activity) {
        super(activity);
        this.viewControllerHandler = (PhotoGalleryManager.PhotoGalleryRequestHandler) activity;
        this.activity = activity;
        progressDialog = DialogUtils.INSTANCE.createProgressDialog(activity);
        progressDialog.hide();
    }

    public void requestPhotoGallery(final String clubId){
        progressDialog.show();
        final RequestResponseHandler handler = new RequestResponseHandler<Club>() {
            @Override
            public void onResponse(Club response) {
                if (photoGalleryRequestHandler != null)
                    photoGalleryRequestHandler.onReceiveGallery(mapDtoToModel(response.club));
                    progressDialog.hide();
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (photoGalleryRequestHandler != null) {
                    photoGalleryRequestHandler.onFailure(error.message);
                }
                progressDialog.hide();
            }
        };

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.getClub(PhotoGalleryManager.this, handler, clubId);
            }
        });
    }

    private List<PhotoGalleryModel> mapDtoToModel(Club club) {
        List<PhotoGalleryModel> list = new ArrayList<>();
        ClubModel model = new ClubModel(activity);
        model.clubId = club.id;

        model.galleryUrls = new ArrayList<>();
        Integer i =0;
        for (Club.PhotoGalleryItem item: club.photoGallery) {
            list.add(new PhotoGalleryModel(i.toString(),item.url));
        }
        return list;
    }
}
