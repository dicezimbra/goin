package br.com.legacy.managers;

import android.app.Activity;

import br.com.goin.component.session.user.UserModel;
import br.com.legacy.managers.dtos.ErrorResponse;
import br.com.legacy.managers.dtos.UserCardDetails;
import br.com.legacy.models.FriendModel;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Created by teruya on 05/09/2017.
 */

public class FriendsManager extends BaseManager {

    FriendRequestHandler viewControllerHandler;
    Activity activity;

    public interface  FriendRequestHandler {
        void onReceiveFriendsList(List<UserModel> friends);
        void onFailureToReceiveFriendsList(String error);
    }

    private int friendsCount;

    public FriendsManager(Activity activity) {
        super(activity);
        this.viewControllerHandler = (FriendsManager.FriendRequestHandler) activity;
        this.activity = activity;
    }

    public void requestFriendsList(final String eventId){
        final RequestResponseHandler handler = new RequestResponseHandler<HashMap<String, List<UserCardDetails>>>() {
            @Override
            public void onResponse(HashMap<String, List<UserCardDetails>> response) {
                List<UserModel> models = mapToModel(response.get("friendsToInvite"));
                if (viewControllerHandler != null) {
                    viewControllerHandler.onReceiveFriendsList(models);
                }
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (viewControllerHandler != null) {
                    viewControllerHandler.onFailureToReceiveFriendsList(error.message);
                }
            }
        };
        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.getFriendsToInvite(FriendsManager.this, handler, eventId);
            }
        });
    }

    public void inviteToEvent(final String eventId, final String invitedId){
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
                serviceManager.inviteToEvent(FriendsManager.this,handler,eventId,invitedId);
            }
        });

    }

    public void getFriends(final String query){
        final RequestResponseHandler handler = new RequestResponseHandler<HashMap<String, List<UserCardDetails>>>() {
            @Override
            public void onResponse(HashMap<String, List<UserCardDetails>> response) {
                List<UserModel> models = mapToModel(response.get("searchFriends"));
                if (viewControllerHandler != null) {
                    viewControllerHandler.onReceiveFriendsList(models);
                }
            }

            @Override
            public void onFailure(ErrorResponse error) {
                if (viewControllerHandler != null) {
                    viewControllerHandler.onFailureToReceiveFriendsList(error.message);
                }
            }
        };
        makeAuthenticatedRequest(new AuthenticatedRequest() {
            @Override
            public void makeRequest() {
                serviceManager.searchFriends(FriendsManager.this, handler, query);
            }
        });
    }



    List<UserModel> mapToModel(List<UserCardDetails> users){
        List<UserModel> list = new ArrayList<>();
        for(final UserCardDetails user: users){
            FriendModel model;
            String userId = user.userId != null ? user.userId : user.id;
            if(user.invitedByMe){
                model = new FriendModel(userId,user.name,user.avatar, FriendModel.FriendStatus.Invited);
            } else {
                model = new FriendModel(userId,user.name,user.avatar, FriendModel.FriendStatus.NotInvited);
            }

            list.add(model);
        }
        return list;
    }

}
