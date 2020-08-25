package br.com.legacy.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

import br.com.goin.component.session.user.UserModel;
import br.com.legacy.managers.BaseManager;
import br.com.legacy.managers.BaseManager.RequestResponseHandler;
import br.com.legacy.managers.dtos.Club;
import br.com.legacy.managers.dtos.Feed;
import br.com.legacy.managers.dtos.Notification;
import br.com.legacy.managers.dtos.Post;
import br.com.legacy.managers.dtos.QueryReceiver;
import br.com.legacy.managers.dtos.QueryResponse;
import br.com.legacy.managers.dtos.SearchFilterResponse;
import br.com.legacy.managers.dtos.SubcategoriesList;
import br.com.legacy.managers.dtos.TicketDetails;
import br.com.legacy.managers.dtos.UserCardDetails;
import br.com.legacy.utils.GraphqlQueries;
import br.com.legacy.utils.SharedPreferencesControl;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by rudieros on 8/18/17.
 */

public class CacheServiceManager extends BaseServiceManager {

    public void getProfileById(String id, RequestResponseHandler<QueryReceiver> handler, SharedPreferencesControl control) {
        Disposable disposable = Observable.create((ObservableOnSubscribe<QueryReceiver>) emitter -> {
            try {
                emitter.onNext(control.loadCacheQueryReceiver(GraphqlQueries.GET_USER_BY_ID + id));
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handler::onResponse, throwable -> {
                    handler.onResponse(null);
                });
    }

    public void getProfile(RequestResponseHandler<UserModel> handler, SharedPreferencesControl control) {
        Disposable disposable = Observable.create((ObservableOnSubscribe<UserModel>) emitter -> {
            try {
                emitter.onNext(control.loadCacheProfile(GraphqlQueries.GET_MY_USER));
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handler::onResponse, throwable -> {
                    handler.onResponse(null);
                });

    }

    public void getMyEventsShort(RequestResponseHandler<QueryResponse> handler, SharedPreferencesControl control, String userId) {
        Disposable disposable = Observable.create((ObservableOnSubscribe<QueryResponse>) emitter -> {
            try {
                emitter.onNext(control.loadCache(GraphqlQueries.GET_MY_EVENTS + userId));
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handler::onResponse, throwable -> {
                    handler.onResponse(null);
                });
    }

    public void getPosts(String clubId, String userId, RequestResponseHandler<HashMap<String, List<Post>>> handler, SharedPreferencesControl control) {
        Disposable disposable = Observable.create((ObservableOnSubscribe<HashMap<String, List<Post>>>) emitter -> {
            try {
                emitter.onNext(control.loadCachePost(GraphqlQueries.GET_POSTS + clubId + userId));
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handler::onResponse, throwable -> {
                    handler.onResponse(null);
                });
    }

    public void getFeed(RequestResponseHandler<HashMap<String, Feed>> handler, SharedPreferencesControl control) {
        Disposable disposable = Observable.create((ObservableOnSubscribe<HashMap<String, Feed>>) emitter -> {
            try {
                HashMap<String, Feed> stringFeedHashMap = control.loadCacheFeed(GraphqlQueries.GET_FEED);
                emitter.onNext(stringFeedHashMap);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handler::onResponse, throwable -> {
                    handler.onResponse(null);
                });
    }

    public void getNextEvents(BaseManager manager, RequestResponseHandler handler, SharedPreferencesControl control) {
        Disposable disposable = Observable.create((ObservableOnSubscribe) emitter -> {
            try {
                QueryResponse queryResponse = control.loadCache(GraphqlQueries.GET_NEXT_EVENTS);
                emitter.onNext(queryResponse);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handler::onResponse, throwable -> {
                    handler.onResponse(null);
                });
    }

    public void filterResults(RequestResponseHandler<SearchFilterResponse> handler, SharedPreferencesControl shared, String query) {
        Disposable disposable = Observable.create((ObservableOnSubscribe<SearchFilterResponse>) emitter -> {
            try {
                emitter.onNext(shared.loadFilter(query));
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handler::onResponse, throwable -> {
                    handler.onResponse(null);
                });
    }

    public void searchClubs(RequestResponseHandler<Club> handler, SharedPreferencesControl shared, String query, int pagination, String categoryId, String subCategoryId) {
        Disposable disposable = Observable.create((ObservableOnSubscribe<Club>) emitter -> {
            try {
                emitter.onNext(shared.loadCacheClubs(GraphqlQueries.SEARCH_CLUBS + query + pagination + categoryId + subCategoryId));
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handler::onResponse, throwable -> {
                    handler.onResponse(null);
                });
    }

    public void getEventDetails(RequestResponseHandler<QueryResponse> handler, SharedPreferencesControl shared, String eventID) {
        Disposable disposable = Observable.create((ObservableOnSubscribe<QueryResponse>) emitter -> {
            try {
                emitter.onNext(shared.loadCache(GraphqlQueries.GET_EVENT + eventID));
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handler::onResponse, throwable -> {
                    handler.onResponse(null);
                });
    }

    public void getClub(SharedPreferencesControl shared, RequestResponseHandler<Club> handler, String clubId) {
        Disposable disposable = Observable.create((ObservableOnSubscribe<Club>) emitter -> {
            try {
                emitter.onNext(shared.loadCacheClubs(GraphqlQueries.GET_CLUB + clubId));
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handler::onResponse, throwable -> {
                    handler.onResponse(null);
                });
    }

    public void getUserFollowers(RequestResponseHandler handler, SharedPreferencesControl shared, String userId) {


        Disposable disposable = Observable.create((ObservableOnSubscribe<HashMap<String,List<UserCardDetails>>>) emitter -> {
            try {
                if(userId != null)
                    emitter.onNext(shared.loadNotifications(GraphqlQueries.GET_USER_FOLLOWERS + userId));
                else
                    emitter.onNext(shared.loadNotifications(GraphqlQueries.GET_CURRENT_USER_FOLLOWERS ));
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handler::onResponse, throwable -> {
                    handler.onResponse(null);
                });

    }

    public void getUserFollowing(RequestResponseHandler handler, SharedPreferencesControl shared, String userId) {


        Disposable disposable = Observable.create((ObservableOnSubscribe<HashMap<String,List<UserCardDetails>>>) emitter -> {
            try {
                if(userId != null)
                    emitter.onNext(shared.loadNotifications(GraphqlQueries.GET_USER_FOLLOWING + userId));
                else
                    emitter.onNext(shared.loadNotifications(GraphqlQueries.GET_CURRENT_USER_FOLLOWING ));
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handler::onResponse, throwable -> {
                    handler.onResponse(null);
                });

    }

    public void getNotifications(RequestResponseHandler handler, String lastId, SharedPreferencesControl shared) {

        Disposable disposable = Observable.create((ObservableOnSubscribe<HashMap<String, List<Notification>>>) emitter -> {
            try {
                emitter.onNext(shared.getNotifications(GraphqlQueries.GET_NOTIFICATIONS + lastId));
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handler::onResponse, throwable -> {
                    handler.onResponse(null);
                });

    }

    public void getSubcategories(String categoryId, RequestResponseHandler<SubcategoriesList> handler, SharedPreferencesControl shared) {

        Disposable disposable = Observable.create((ObservableOnSubscribe<SubcategoriesList>) emitter -> {
            try {
                emitter.onNext(shared.getSubcategories(GraphqlQueries.GET_SUBCATEGORIES + categoryId));
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handler::onResponse, throwable -> {
                    handler.onResponse(null);
                });
    }

    public void getEventTickets(RequestResponseHandler handler, String eventId, SharedPreferencesControl sharedPreferencesControl) {



        Disposable disposable = Observable.create((ObservableOnSubscribe<HashMap<String, List<TicketDetails>>>) emitter -> {
            try {
                emitter.onNext(sharedPreferencesControl.getEventTickets( GraphqlQueries.GET_EVENT_TICKETS + eventId));
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handler::onResponse, throwable -> {
                    handler.onResponse(null);
                });
    }

    public void getBanner( @NotNull RequestResponseHandler<QueryResponse> requestResponseHandler, @Nullable SharedPreferencesControl sharedPreferencesControl) {

        Disposable disposable = Observable.create((ObservableOnSubscribe<QueryResponse>) emitter -> {
            try {
                emitter.onNext(sharedPreferencesControl.loadCache( GraphqlQueries.GET_BANNER_HOME ));
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(requestResponseHandler::onResponse, throwable -> {
                    requestResponseHandler.onResponse(null);
                });
    }
}
