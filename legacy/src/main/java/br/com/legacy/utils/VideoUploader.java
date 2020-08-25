package br.com.legacy.utils;

import android.app.Activity;


import java.io.File;
import java.lang.ref.WeakReference;

public class VideoUploader {

    public interface VideoUploaderListener {
        void onComplete(String key);
        void onError(String error);
    }

    public static String buildPostImageKey(Long timeStamp) {
        return Constants.POST_PATH + "/" + "everyone" + "/" + timeStamp.toString() + Constants.VIDEO_EXT;
    }
}
