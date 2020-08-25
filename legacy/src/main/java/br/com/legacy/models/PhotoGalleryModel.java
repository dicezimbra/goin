package br.com.legacy.models;

import android.graphics.drawable.Drawable;

/**
 * Created by kalunga on 03/10/2017.
 */

public class PhotoGalleryModel {
    public String photoId;
    public Drawable photoImage;
    public String photoUrl;

    public PhotoGalleryModel(String photoId, String photoUrl) {
        this.photoId = photoId;
        this.photoUrl = photoUrl;
    }
}
