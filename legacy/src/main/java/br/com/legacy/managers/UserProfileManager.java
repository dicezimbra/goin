package br.com.legacy.managers;

import android.app.Activity;

import br.com.goin.component.session.user.UserModel;
import br.com.legacy.managers.dtos.ErrorResponse;
import br.com.legacy.managers.dtos.QueryReceiver;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by appsimples on 10/5/17.
 */

public class UserProfileManager extends BaseManager {

    public ProfileRequestHandler profileRequestHandler;

    public interface ProfileRequestHandler {
        void onProfileReceived(UserModel userModel);
        void onError(String error);
    }

    public UserProfileManager(Activity activity) {
        super(activity);
    }

    public void requestUserProfile() {
        final RequestResponseHandler handler = new RequestResponseHandler<UserModel>() {
            @Override
            public void onResponse(UserModel response) {
                onProfileLoaded(response);
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (profileRequestHandler != null) {
                    profileRequestHandler.onError(error.message);
                }
            }
        };

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                cacheServiceManager.getProfile(new RequestResponseHandler<UserModel>() {
                    @Override
                    public void onResponse(UserModel response) {
                        if(response != null) {
                            onProfileLoaded(response);
                        }
                        serviceManager.getUser(UserProfileManager.this, handler);
                    }

                    @Override
                    public void onFailure(ErrorResponse error) {
                        serviceManager.getUser(UserProfileManager.this, handler);
                    }

                }, sharedPreferencesControl);
            }
        });
    }

    private void onProfileLoaded(UserModel response) {
        if (profileRequestHandler != null && response != null) {
            profileRequestHandler.onProfileReceived(response.getMyUser());
        }
    }

    private void onProfileLoaded(QueryReceiver response) {
        if (profileRequestHandler != null) {
            Disposable disposable = Observable.create((ObservableOnSubscribe<UserModel>) emitter -> {
                try {
                    emitter.onNext(mapToModel(response));
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(profileRequestHandler::onProfileReceived, throwable -> {
                        profileRequestHandler.onProfileReceived(null);
                    });
        }
    }

    public void requestUserProfile(final String userId) {
        final RequestResponseHandler handler = new RequestResponseHandler<QueryReceiver>() {
            @Override
            public void onResponse(QueryReceiver response) {
                onProfileLoaded(response);
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (profileRequestHandler != null) {
                    profileRequestHandler.onError(error.message);
                }
            }
        };

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                cacheServiceManager.getProfileById(userId, new RequestResponseHandler<QueryReceiver>() {
                    @Override
                    public void onResponse(QueryReceiver response) {
                        if(response != null) {
                            onProfileLoaded(response);
                        }
                        serviceManager.getUser(UserProfileManager.this, handler, userId);
                    }

                    @Override
                    public void onFailure(ErrorResponse error) {
                        serviceManager.getUser(UserProfileManager.this, handler, userId);
                    }

                }, sharedPreferencesControl);
            }
        });

    }


    UserModel mapToModel(QueryReceiver queryReceiver){
        return queryReceiver.user;
    }

}
