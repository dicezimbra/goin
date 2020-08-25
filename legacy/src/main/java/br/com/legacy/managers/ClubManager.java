package br.com.legacy.managers;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import br.com.goin.component.model.event.api.EventMapper;
import br.com.goin.component.ui.kit.dialog.DialogUtils;
import br.com.legacy.managers.dtos.Club;
import br.com.legacy.managers.dtos.ErrorResponse;
import br.com.legacy.managers.dtos.SearchFilterInput;
import br.com.legacy.managers.dtos.SearchFilterResponse;
import br.com.legacy.models.ClubModel;
import br.com.goin.component.model.event.Event;
import br.com.legacy.models.SearchFilterModel;
import br.com.legacy.utils.GraphqlQueries;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.PublishSubject;
import permissions.dispatcher.NeedsPermission;

/**
 * Created by appsimples on 10/16/17.
 */

public class ClubManager extends BaseManager {

    public static final PublishSubject<ClubModel> clubSubject = PublishSubject.create();
    public static final PublishSubject<Float> ratingSubject = PublishSubject.create();
    public static final PublishSubject<String> ratingCountSubject = PublishSubject.create();

    private static final String TAG = ClubManager.class.getSimpleName();

    public interface ClubRequestHandler {
        void onClubReceived(ClubModel model);

        void onFailure(String error);
    }

    public interface SearchClubsRequestHandler {
        void onReceiveSearchClubs(List<ClubModel> clubsList);

        void onFailure(String error);
    }

    public interface SearchClubsRequestFilterHandler {
        void onReceiveSearchFilterClubs(SearchFilterModel clubModel);
        void onFailure(String error);
        void onReceiveSearchFilter(SearchFilterModel searchFilterModel);
        void onReceiveSearchFilterRefresh(SearchFilterModel searchFilterModel);
    }

    public interface MyClubsRequestHandler {
        void onReceiveMyClubs(List<ClubModel> myClubs);
    }

    public SearchClubsRequestFilterHandler searchClubsRequestFilterHandler;
    public ClubRequestHandler clubRequestHandler;
    public SearchClubsRequestHandler searchClubsRequestHandler;
    public MyClubsRequestHandler myClubsViewControllerHandler;
    public boolean shouldShowProgressBar = true;

    static Activity activity;
    ProgressDialog progressDialog;

    public ClubManager(Activity activity) {
        super(activity);

        ClubManager.activity = activity;
        progressDialog = DialogUtils.INSTANCE.createProgressDialog(activity);
        progressDialog.hide();

    }

    public void requestClub(final String clubId) {
        cacheServiceManager.getClub(sharedPreferencesControl, new RequestResponseHandler<Club>() {
            @Override
            public void onResponse(Club response) {
                if (clubRequestHandler != null && response != null) {
                    clubRequestHandler.onClubReceived(mapDtoToModel(response.club));
                    progressDialog.hide();
                    onlineCall(clubId, false);
                }else {
                    onlineCall(clubId, true);
                }
            }

            @Override
            public void onFailure(ErrorResponse error) {
                onlineCall(clubId, true);
            }
        }, clubId);


    }

    private void onlineCall(String clubId, Boolean shouldShowProgressBar) {
        if (shouldShowProgressBar) {
            progressDialog.show();
        }

        final RequestResponseHandler handler = new RequestResponseHandler<Club>() {
            @Override
            public void onResponse(Club response) {
                if (clubRequestHandler != null && response != null)
                    clubRequestHandler.onClubReceived(mapDtoToModel(response.club));
                progressDialog.hide();
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (clubRequestHandler != null) {
                    clubRequestHandler.onFailure(error.message);
                }
                progressDialog.hide();
            }
        };

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.getClub(ClubManager.this, handler, clubId);
            }
        });
    }

    public void requestClubEvents(final String clubId) {
        if (shouldShowProgressBar) {
            progressDialog.show();
        }
        final RequestResponseHandler handler = new RequestResponseHandler<Club>() {
            @Override
            public void onResponse(Club response) {
                if (clubRequestHandler != null)
                    clubRequestHandler.onClubReceived(mapDtoToModel(response.club));
                progressDialog.hide();
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (clubRequestHandler != null) {
                    clubRequestHandler.onFailure(error.message);
                }
                progressDialog.hide();
            }
        };

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.getClubEvents(ClubManager.this, handler, clubId);
            }
        });
    }

    public void searchClubs(final String query, final Integer pagination, final String categoryId, final String subCategoryId) {
        if (shouldShowProgressBar) {
            progressDialog.show();
        }


        cacheServiceManager.searchClubs(new RequestResponseHandler<Club>() {
            @Override
            public void onResponse(Club response) {
                if (response != null) {
                    searchClubsRequestHandler.onReceiveSearchClubs(mapDtoListToModelList(response.searchClubs));
                    progressDialog.hide();
                }
                onlineCall(query, pagination, categoryId, subCategoryId);
            }

            @Override
            public void onFailure(ErrorResponse error) {
                onlineCall(query, pagination, categoryId, subCategoryId);
            }
        }, sharedPreferencesControl, query, pagination, categoryId, subCategoryId);

    }

    public void filterResults(final SearchFilterInput searchFilterInput,
                              final Integer paginate, final Integer limit, boolean firstRequest) {
        if (shouldShowProgressBar) {
            progressDialog.show();
        }
        if(firstRequest) {
            cacheServiceManager.filterResults(new RequestResponseHandler<SearchFilterResponse>() {
                @Override
                public void onResponse(SearchFilterResponse response) {
                    if (response != null && searchClubsRequestFilterHandler != null) {
                        searchClubsRequestFilterHandler.onReceiveSearchFilter(mapToModel(response));
                        progressDialog.hide();
                    }
                    searchFilterOnline(searchFilterInput, paginate, limit, firstRequest);
                }

                @Override
                public void onFailure(ErrorResponse error) {
                    searchFilterOnline(searchFilterInput, paginate, limit, firstRequest);
                }
            }, sharedPreferencesControl, GraphqlQueries.FILTER_RESULTS + searchFilterInput.to() + paginate);
        }else{
            searchFilterOnline(searchFilterInput, paginate, limit, firstRequest);
        }
    }

    private void searchFilterOnline(SearchFilterInput searchFilterInput, Integer paginate, Integer limit, boolean firstRequest) {
        serviceManager.filterResults(searchFilterInput, paginate, limit, ClubManager.this, new RequestResponseHandler<SearchFilterResponse>() {
            @Override
            public void onResponse(SearchFilterResponse response) {
                if (searchClubsRequestFilterHandler != null && response != null) {
                    if(firstRequest)
                    searchClubsRequestFilterHandler.onReceiveSearchFilter(mapToModel(response));
                    else  searchClubsRequestFilterHandler.onReceiveSearchFilterRefresh(mapToModel(response));
                    progressDialog.hide();
                }
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (searchClubsRequestFilterHandler != null) {
                    searchClubsRequestFilterHandler.onFailure(error.message);
                    progressDialog.hide();
                }
            }
        });
    }

    private SearchFilterModel mapToModel(SearchFilterResponse response) {
        if(response == null)
            return null;
        return new SearchFilterModel(response.searchFilter.currentPage, response.searchFilter.totalPages, response.searchFilter.totalItems, new EventMapper().mapToModel(response.searchFilter.events), mapDtoListToModelList(response.searchFilter.clubs));
    }


    private void onlineCall(String query, Integer pagination, String categoryId, String subCategoryId) {
        final RequestResponseHandler handler = new RequestResponseHandler<Club>() {
            @Override
            public void onResponse(Club response) {
                if(response!= null)
                searchClubsRequestHandler.onReceiveSearchClubs(mapDtoListToModelList(response.searchClubs));
                // sharedPreferencesControl.saveSearchClubs(categoryId , mapDtoListToModelList(response.searchClubs));
                progressDialog.hide();
            }

            @Override
            public void onFailure(ErrorResponse error) {
                searchClubsRequestHandler.onFailure(error.message);
                progressDialog.hide();
            }
        };

        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.searchClubs(ClubManager.this, handler, query, pagination, categoryId, subCategoryId);
            }
        });
    }

    public void requestMyClubsList(final String userId) {

        final RequestResponseHandler handler = new RequestResponseHandler<Club>() {
            @Override
            public void onResponse(Club response) {
                List<Club> followedClubs = response.followedClubs;
                myClubsViewControllerHandler.onReceiveMyClubs(mapDtoListToModelList(followedClubs));
            }

            @Override
            public void onFailure(ErrorResponse error) {
                DialogUtils.INSTANCE.handleError(activity);
            }
        };

        if (userId == null || userId.equals(""))
            makeAuthenticatedRequest(new AuthenticatedRequest() {
                @Override
                public void makeRequest() {
                    serviceManager.getFollowedClubs(ClubManager.this, handler, null);
                }
            });
        else
            makeAuthenticatedRequest(new AuthenticatedRequest() {
                @Override
                public void makeRequest() {
                    serviceManager.getFollowedClubs(ClubManager.this, handler, userId);
                }
            });

    }

    public void toggleFollowClub(ClubModel clubModel) {
        if (clubModel.isFollowing) {
            followClub(clubModel);
        } else {
            unfollowClub(clubModel);
        }
    }

    public void followClub(final ClubModel clubModel) {
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
                serviceManager.followClub(ClubManager.this, handler, clubModel.clubId);
            }
        });
    }

    public void unfollowClub(final ClubModel clubModel) {
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
                serviceManager.unfollowClub(ClubManager.this, handler, clubModel.clubId);
            }
        });
    }

    private ClubModel mapDtoToModel(Club club) {
        ClubModel model = new ClubModel(activity);
        model.clubId = club.id;
        model.clubLogoUrl = club.logoImage;
        model.followers = club.followersCount == null ? 0 : club.followersCount;
        model.hasLogo = club.logoImage != null;
        model.clubName = club.name;
        model.clubSite = club.website == null ? "" : club.website;
        model.rating = club.rating;
        model.ratingCount = club.ratingCount == null ? 0 : club.ratingCount;
        model.isFollowing = club.followedByMe == null ? false : club.followedByMe;

        if (club.posts != null)
            if (!club.posts.isEmpty())
                model.posts = FeedManager.mapDtoListToModelList(club.posts, activity);
        if (club.events != null)
            if (!club.events.isEmpty())
                model.events = new EventMapper().mapToModel(club.events);
        model.clubDescription = club.description;
        model.coverImage = club.coverImage;
        model.address = club.address;
        model.galleryUrls = new ArrayList<>();
        if (club.photoGallery != null) {
            for (Club.PhotoGalleryItem item : club.photoGallery) {
                model.galleryUrls.add(item.url);
            }
        }
        model.handler = new ClubModel.ClubItemHandler() {
            @Override
            public void onClickFollowButton(ClubModel model) {
                toggleFollowClub(model);
            }
        };

        if (club.latitude != null && club.longitude != null) {
            model.latitude = club.latitude;
            model.longitude = club.longitude;

            Location myLocation = getMyLocation();
            float[] results = new float[3];
            if (myLocation != null) {
                Location.distanceBetween(myLocation.getLatitude(), myLocation.getLongitude(), club.latitude, club.longitude, results);
            }
            model.distance = results[0];
        } else {
            model.distance = null;
        }

        return model;
    }




    private List<ClubModel> mapDtoListToModelList(List<Club> clubs) {
        List<ClubModel> clubsModels = new ArrayList<>();
        if(clubs == null)
            return clubsModels;
        for (Club c : clubs) {
            clubsModels.add(mapDtoToModel(c));
        }
        return clubsModels;
    }

    @NeedsPermission(value = {Manifest.permission.ACCESS_COARSE_LOCATION})
    public static Location getMyLocation() {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.ACCURACY_COARSE);
        String provider = locationManager.getBestProvider(criteria, false);
        Location myLocation = null;

        if (provider != null) {
            try {
                myLocation = locationManager.getLastKnownLocation(provider);
                return myLocation;
            } catch (SecurityException error) {
                Log.e(TAG, error.getMessage(), error);
            }
        }
        return null;
    }

}
