package br.com.legacy.utils;



/**
 * Created by appsimples on 10/4/17.
 */

public class ImageUploader {





    public enum ImageTypes {
        Posts,
        Users,
    }

    public interface ImageUploadListener {
        void onComplete(String key);
        void onError(String error);
        void onProgressChanged(int progress);
    }

    public static String buildUserProfilePictureKey() {

        return Constants.USERS_PATH + "/" + "everyone";
    }

    public static String buildPostImageKey(Long timeStamp) {

        return Constants.POST_PATH + "/" + "everyone" + "/" + timeStamp.toString() + Constants.IMAGE_EXT;
    }
}
