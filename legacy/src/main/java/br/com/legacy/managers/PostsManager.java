package br.com.legacy.managers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.iceteck.silicompressorr.SiliCompressor;

import br.com.goin.component.ui.kit.dialog.DialogUtils;
import br.com.legacy.managers.dtos.ErrorResponse;
import br.com.legacy.managers.dtos.Post;
import br.com.legacy.managers.dtos.QueryResponse;
import br.com.legacy.models.FeedCardModel;
import br.com.legacy.utils.ImageUploader;
import br.com.legacy.utils.VideoUploader;

import java.io.File;
import java.util.List;

/**
 * Created by appsimples on 10/4/17.
 */

public class PostsManager extends BaseManager {

    ProgressDialog progressDialog;

    public PostCreationHandler creationHandler;

    public interface PostCreationHandler {
        void onCreatePostSuccess(Post createPost);

        void onCreatePostFailure(String error);
    }

    public PostsManager(Activity activity) {
        super(activity);
        progressDialog = DialogUtils.INSTANCE.createProgressDialog(activity);
    }

    public void requestCreatePost(final FeedCardModel post, final Bitmap postImage, final String postMediaPath) {

        progressDialog.show();

        final RequestResponseHandler handler = new RequestResponseHandler<QueryResponse>() {
            @Override
            public void onResponse(QueryResponse response) {
                // post was successfully created
                Long timeStamp = response.createPost.timeStamp;
                if (post.hasVideoPost) {
                    post.postPhotoUrl = postMediaPath;
                    uploadVideo(postMediaPath, timeStamp, response);
                } else if (post.hasImagePost)
                    uploadImage(postImage, timeStamp, response);
                else {
                    if (creationHandler != null) {
                        creationHandler.onCreatePostSuccess(response.createPost);
                    }
                    progressDialog.hide();
                }
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (creationHandler != null) {
                    creationHandler.onCreatePostFailure(error.message);
                }
                progressDialog.hide();
            }
        };

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.createPost(PostsManager.this, handler, mapToDTO(post));
            }
        });

    }

    public void requestCreatePost(final FeedCardModel post, final Bitmap postImage, final String postMediaPath, final String eventId) {

        progressDialog.show();

        final RequestResponseHandler handler = new RequestResponseHandler<QueryResponse>() {
            @Override
            public void onResponse(QueryResponse response) {
                // post was successfully created
                Long timeStamp = response.createPost.timeStamp;
                if (post.hasVideoPost) {
                    post.postPhotoUrl = postMediaPath;
                    uploadVideo(postMediaPath, timeStamp, response);
                } else if (post.hasImagePost)
                    uploadImage(postImage, timeStamp, response);
                else {
                    if (creationHandler != null) {
                        creationHandler.onCreatePostSuccess(response.createPost);
                    }
                    progressDialog.hide();
                }
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (creationHandler != null) {
                    creationHandler.onCreatePostFailure(error.message);
                }
                progressDialog.hide();
            }
        };

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.createPost(PostsManager.this, handler, mapToDTO(post), eventId);
            }
        });

    }

    public void uploadVideo(final String postVideoPath, final Long timeStamp, final QueryResponse response) {
        final VideoUploader.VideoUploaderListener listener = new VideoUploader.VideoUploaderListener() {
            @Override
            public void onComplete(String key) {
                if (creationHandler != null) {
                    Log.e("GOIN ","PostManager uploadVideo key = "+key);
                   // response.createPost.video = key;
                    response.createPost.image = postVideoPath;
                    creationHandler.onCreatePostSuccess(response.createPost);
                }
                progressDialog.hide();
            }

            @Override
            public void onError(String error) {
                if (creationHandler != null) {
                    creationHandler.onCreatePostFailure(error);
                }
                progressDialog.hide();
            }
        };


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    File dir = new File(postVideoPath).getParentFile();
                    MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
                    metaRetriever.setDataSource(postVideoPath);
                    double height = Double.valueOf(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                    double width = Double.valueOf(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                    double ratio = width / height;

                    metaRetriever.release();
                    final int videoWidth = 900;
                    int bitrate = 1500000;
                    String videoCompressed = null;
                    if (width < videoWidth)
                        videoCompressed = postVideoPath;
                    else
                        videoCompressed = SiliCompressor.with(activity).compressVideo(postVideoPath, dir.getAbsolutePath(), videoWidth, new Double(videoWidth / ratio).intValue(), bitrate);

                    String key = VideoUploader.buildPostImageKey(timeStamp);


                    List<String> paths = Uri.parse(response.createPost.image).getPathSegments();
                    String path = paths.get(1)+"/"+paths.get(2)+"/"+paths.get(3) + ".mp4";

                    //TransferUtility utility = VideoUploader.uploadToS3(activity, videoCompressed, path, listener);

                } catch (final Exception e) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (creationHandler != null) {
                                creationHandler.onCreatePostFailure(e.getMessage());
                            }
                            progressDialog.hide();
                        }
                    });
                }
            }
        });
    }

    public void uploadImage(Bitmap postImage, final Long timeStamp, final QueryResponse response) {

        ImageUploader.ImageUploadListener listener = new ImageUploader.ImageUploadListener() {
            @Override
            public void onComplete(String key) {
                if (creationHandler != null) {
                    creationHandler.onCreatePostSuccess(response.createPost);
                }
                progressDialog.hide();
            }

            @Override
            public void onProgressChanged(int progress) {

            }

            @Override
            public void onError(String error) {
                if (creationHandler != null) {
                    creationHandler.onCreatePostFailure(error);
                }
                progressDialog.hide();
            }
        };


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //ImageUploader.loginCognito();
                    String key = ImageUploader.buildPostImageKey(timeStamp);

                    List<String> paths = Uri.parse(response.createPost.image).getPathSegments();
                    String path = paths.get(1)+"/"+paths.get(2)+"/"+paths.get(3);

                   // ImageUploader.uploadToS3(activity, postImage, path, listener);
                } catch (Exception e) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (creationHandler != null) {
                                creationHandler.onCreatePostFailure(e.getMessage());
                            }
                            progressDialog.hide();
                        }
                    });
                }
            }
        });
    }

    public static Post mapToDTO(FeedCardModel model) {
        Post dto = new Post();
        dto.description = model.postDescriptionText;
        dto.image = model.hasImagePost ? "true" : null;
        dto.video = model.hasVideoPost ? "true" : null;
        if (model.feeling != null) {
            dto.feeling = model.feeling.name;
        }
        if (model.postLocationId != null) {
            dto.checkInEventId = model.postLocationId;
        }
        return dto;
    }

}
