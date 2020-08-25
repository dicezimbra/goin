package br.com.legacy.managers;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import br.com.legacy.managers.dtos.ErrorResponse;
import br.com.legacy.managers.dtos.Feed;
import br.com.legacy.managers.dtos.Post;
import br.com.legacy.managers.dtos.QueryResponse;
import br.com.legacy.models.FeedCardModel;
import br.com.legacy.models.FeelingModel;
import br.com.R;
import br.com.legacy.utils.Constants;
import br.com.legacy.utils.SharedPreferencesControl;
import br.com.legacy.utils.SnackBarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by ivo on 13/09/2017.
 */

public class FeedManager extends BaseManager {

    public static final PublishSubject<Bundle> commentSubject = PublishSubject.create();
    public static final PublishSubject<FeedCardModel> likeSubject = PublishSubject.create();
    public static final PublishSubject<FeedCardModel> createPostSubject = PublishSubject.create();
    public static final PublishSubject<String> postDeletedSubject = PublishSubject.create();
    public static Observable<Boolean> onPost = Observable.just(false);

    public PostRequestHandler postRequestHandler;
    public PostRequestHandler postUpdatedRequestHandler;

    public interface PostRequestHandler {
        void onReceivePosts(List<FeedCardModel> posts, String lastPostId, String lastMasterPostId, Boolean canGetMorePosts);

        void onError(String error);
    }

    public FeedManager(Activity activity) {
        super(activity);
    }

    public void requestFeed(final String lastPostId, final String lastMasterPostId, final boolean useCache, boolean onRefresh) {

        final RequestResponseHandler handler = new RequestResponseHandler<HashMap<String, Feed>>() {

            @Override
            public void onResponse(HashMap<String, Feed> response) {
                if (response != null) {
                    List<FeedCardModel> models = mapFeedDtoListToModelList(response.get("feed"), activity);
                    if (postRequestHandler != null) {
                        Boolean getMorePosts = false;
                        if (response.get("feed").morePostsToGet != null)
                            getMorePosts = response.get("feed").morePostsToGet;

                        postRequestHandler.onReceivePosts(models, response.get("feed").lastPostId, response.get("feed").lastMasterPostId, getMorePosts);
                    }
                }else{
                    postRequestHandler.onReceivePosts(null, null, null, null);
                }
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (postRequestHandler != null) {
                    postRequestHandler.onError(error.message);
                }
            }
        };

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                if (onRefresh) {
                    serviceManager.getFeed(FeedManager.this, handler, lastPostId, lastMasterPostId);
                } else {
                    if (useCache) {
                        cacheServiceManager.getFeed(new RequestResponseHandler<HashMap<String, Feed>>() {
                            @Override
                            public void onResponse(HashMap<String, Feed> response) {
                                if (response != null) {
                                    onFeedLoaded(response);
                                }
                                serviceManager.getFeed(FeedManager.this, handler, lastPostId, lastMasterPostId);
                            }

                            @Override
                            public void onFailure(ErrorResponse error) {
                                serviceManager.getFeed(FeedManager.this, handler, lastPostId, lastMasterPostId);
                            }
                        }, sharedPreferencesControl);
                    } else {
                        serviceManager.getFeed(FeedManager.this, handler, lastPostId, lastMasterPostId);
                    }
                }
            }
        });
    }

    private void onFeedLoaded(HashMap<String, Feed> response) {
        List<FeedCardModel> models = mapFeedDtoListToModelList(response.get("feed"), activity);
        if (postRequestHandler != null) {
            Boolean getMorePosts = false;
            if (response.get("feed").morePostsToGet != null)
                getMorePosts = response.get("feed").morePostsToGet;

            postRequestHandler.onReceivePosts(models, response.get("feed").lastPostId, response.get("feed").lastMasterPostId, getMorePosts);
        }
    }

    public void requestPosts(final Integer postCount, final String lastPostId, final String clubId, final String userId) {

        final RequestResponseHandler handler = new RequestResponseHandler<HashMap<String, List<Post>>>() {
            @Override
            public void onResponse(HashMap<String, List<Post>> response) {
                onPostLoaded(response);
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (postRequestHandler != null) {
                    postRequestHandler.onError(error.message);
                }
            }
        };

        makeAuthenticatedRequest(() ->
                onPost.subscribe(item -> {

                    if (item) {
                        serviceManager.getPosts(FeedManager.this, handler, lastPostId, userId, clubId, postCount);
                        onPost = Observable.just(false);
                    } else {

                        cacheServiceManager.getPosts(clubId, userId, new RequestResponseHandler<HashMap<String, List<Post>>>() {
                            @Override
                            public void onResponse(HashMap<String, List<Post>> response) {
                                if (response != null) {
                                    onPostLoaded(response);
                                }
                                serviceManager.getPosts(FeedManager.this, handler, lastPostId, userId, clubId, postCount);
                            }

                            @Override
                            public void onFailure(ErrorResponse error) {
                                serviceManager.getPosts(FeedManager.this, handler, lastPostId, userId, clubId, postCount);
                            }
                        }, sharedPreferencesControl);
                    }
                }));
    }

    private void onPostLoaded(HashMap<String, List<Post>> response) {
        if (postRequestHandler != null && response != null) {
            Disposable disposable = Observable.create((ObservableOnSubscribe<List<FeedCardModel>>) emitter -> {
                try {
                    List<FeedCardModel> models = mapDtoListToModelList(response.get("posts"), activity);
                    emitter.onNext(models);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(models -> {
                        postRequestHandler.onReceivePosts(models, null, null, null);
                    }, throwable -> {
                        postRequestHandler.onReceivePosts(null, null, null, null);
                    });
        }
    }

    public void likePost(final String postId) {

        final RequestResponseHandler handler = new RequestResponseHandler() {
            @Override
            public void onResponse(Object response) {

            }

            @Override
            public void onFailure(ErrorResponse error) {

            }
        };

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.likePost(FeedManager.this, handler, postId);
            }
        });
    }

    public void dislikePost(final String postId) {

        final RequestResponseHandler handler = new RequestResponseHandler() {
            @Override
            public void onResponse(Object response) {

            }

            @Override
            public void onFailure(ErrorResponse error) {

            }
        };

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.dislikePost(FeedManager.this, handler, postId);
            }
        });

    }

    public void reportPost(final String postId) {
//      TODO endpoint reportPost
    }

    public void deletePost(final String postId) {
        final RequestResponseHandler handler = new RequestResponseHandler() {
            @Override
            public void onResponse(Object response) {

            }

            @Override
            public void onFailure(ErrorResponse error) {

            }
        };

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.deletePost(FeedManager.this, handler, postId);
            }
        });
    }

    public void getPost(final String postId) {
        final RequestResponseHandler<QueryResponse> handler = new RequestResponseHandler<QueryResponse>() {
            @Override
            public void onResponse(QueryResponse response) {
                if (response != null && response.getPost != null && postRequestHandler != null) {
                    List<FeedCardModel> post = new ArrayList<>(1);
                    post.add(mapDtoToModel(response.getPost, activity));
                    postRequestHandler.onReceivePosts(post, null, null, null);
                }
            }

            @Override
            public void onFailure(ErrorResponse error) {

            }
        };

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.getPost(FeedManager.this, handler, postId);
            }
        });
    }

    public void sharePost(final String postId, final View parentLayout) {
        final RequestResponseHandler handler = new RequestResponseHandler() {
            @Override
            public void onResponse(Object response) {
                SnackBarUtils.showSnackBar(activity, parentLayout, activity.getResources().getString(R.string.share_success));
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (activity != null && !activity.isFinishing()) {
                    SnackBarUtils.showSnackBar(activity, parentLayout, activity.getResources().getString(R.string.error_share));
                }
            }
        };

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.sharePost(FeedManager.this, handler, postId);
            }
        });
    }

    public void requestEventFeed(final String eventId, final String lastPostId) {

        loadPostsFromCache(postRequestHandler);

        final RequestResponseHandler handler = new RequestResponseHandler<HashMap<String, Feed>>() {
            @Override
            public void onResponse(HashMap<String, Feed> response) {
                onFeedLoaded(response);
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (postRequestHandler != null) {
                    postRequestHandler.onError(error.message);
                }
            }
        };

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.getEventFeed(FeedManager.this, handler, lastPostId, eventId);
            }
        });
    }

    private void loadPostsFromCache(PostRequestHandler postRequestHandler) {
        sharedPreferencesControl = new SharedPreferencesControl(activity, Constants.SHARED_PREFERENCES);
        // if(sharedPreferencesControl.hasPostsInCache()){

        // }

    }

    public static List<FeedCardModel> mapFeedDtoListToModelList(Feed feed, Activity activity) {
        List<FeedCardModel> models = new ArrayList<>();
        if (feed != null)
            if (feed.post != null)
                for (Post card : feed.post)
                    models.add(mapDtoToModel(card, activity));

        return models;

    }

    public static List<FeedCardModel> mapDtoListToModelList(List<Post> posts, Activity activity) {
        List<FeedCardModel> models = new ArrayList<>();
        if (posts != null) {
            for (Post post : posts) {
                models.add(mapDtoToModel(post, activity));
            }
        }
        return models;
    }

    public static FeedCardModel mapDtoToModel(Post post, Activity activity) {
        if (post.raffle != null) {
            post.raffle.description = post.description;
            post.raffle.image = post.image;

            return RafflesManager.mapDtoToModel(post.raffle, activity);
        }

        FeedCardModel model = new FeedCardModel(activity);
        if (post.image != null && !post.image.equals("false")) {
            model.postPhotoUrl = post.image;
            model.hasImagePost = true;
        } else {
            model.hasImagePost = false;
        }

        if (post.video != null && !post.video.equals("false")) {
            model.postVideoUrl = post.video;
            model.hasVideoPost = true;
        } else {
            model.hasVideoPost = false;
        }

        if (post.feeling != null) {
            model.feeling = new FeelingModel();
            model.feeling.name = post.feeling;
            model.feeling.setDisplayName(post.feeling, activity);
        }

        if (post.sharedBy != null) {
            model.sharerId = post.sharedBy.id;
            model.sharerName = post.sharedBy.name;
            model.sharerProfilePicture = post.sharedBy.avatar;
        }

        model.postLocation = post.location;
        model.eventName = post.eventName;
        model.checkInEventId = post.checkInEventId;
        model.commentsCounter = post.commentsCount != null ? post.commentsCount : 0;
        model.profilePhotoUrl = post.avatar;
        model.likesCounter = post.likesCount != null ? post.likesCount : 0;
        model.idPost = post.postId;
        model.postDescriptionText = post.description;
        model.userName = post.name;
        model.timeStamp = post.timeStamp;
        model.commentText = post.comment;
        model.commentId = post.commentId;
        model.commentUserName = post.commentAuthor;
        model.posterId = post.posterId;
        model.likedByMe = post.likedByMe != null ? post.likedByMe : false;

        return model;
    }

}
