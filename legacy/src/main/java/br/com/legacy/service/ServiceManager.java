package br.com.legacy.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.goin.component.model.event.Event;
import br.com.goin.component.session.user.UserModel;
import br.com.legacy.managers.AuthManager;
import br.com.legacy.managers.BaseManager;
import br.com.legacy.managers.BaseManager.RequestResponseHandler;
import br.com.legacy.managers.dtos.BuyTicketBankSlipInput;
import br.com.legacy.managers.dtos.BuyTicketCreditCardInput;
import br.com.legacy.managers.dtos.CitiesList;
import br.com.legacy.managers.dtos.Club;
import br.com.legacy.managers.dtos.ConfirmedPeopleInput;
import br.com.legacy.managers.dtos.FacebookSigninInput;
import br.com.legacy.managers.dtos.Feed;
import br.com.legacy.managers.dtos.Notification;
import br.com.legacy.managers.dtos.OutsmartResponse;
import br.com.legacy.managers.dtos.Post;
import br.com.legacy.managers.dtos.PostLiker;
import br.com.legacy.managers.dtos.QueryReceiver;
import br.com.legacy.managers.dtos.QueryResponse;
import br.com.legacy.managers.dtos.Raffle;
import br.com.legacy.managers.dtos.SearchFilterInput;
import br.com.legacy.managers.dtos.SearchFilterResponse;
import br.com.legacy.managers.dtos.StatesList;
import br.com.legacy.managers.dtos.TicketDetails;
import br.com.legacy.managers.dtos.TransferTicketInput;
import br.com.legacy.managers.dtos.UserCardDetails;
import br.com.legacy.managers.dtos.UserTempInput;
import br.com.legacy.managers.dtos.VerifyEmail;
import br.com.legacy.models.GraphqlQuery;
import br.com.legacy.utils.GraphqlQueries;
import retrofit2.Call;

/**
 * Created by rudieros on 8/18/17.
 */

public class ServiceManager extends BaseServiceManager {

    public void createUserTemp(BaseManager manager, RequestResponseHandler handler, String email, String password) {
        UserTempInput input = new UserTempInput(email, password);
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.CREATE_USER_TEMP)
                .var("input", input)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().graphQl(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void verifyEmail(BaseManager manager, RequestResponseHandler handler, String email) {

        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.VERIFY_EMAIL)
                .var("email", email)
                .build();

        Call<OutsmartResponse<VerifyEmail>> call = outsmartService.getApiService().verifyEmail(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void facebookSignIn(BaseManager manager, RequestResponseHandler handler, String email) {
        FacebookSigninInput input = new FacebookSigninInput(email);
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.FACEBOOK_SIGN_IN)
                .var("input", input)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().graphQl(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void searchUser(BaseManager manager, RequestResponseHandler handler, String query) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.SEARCH_USER)
                .var("query", query)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().searchUser(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void searchFacebookUser(BaseManager manager, RequestResponseHandler handler) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.SEARCH_FB_USERS)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().searchUser(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void registerUser(BaseManager manager, RequestResponseHandler handler, UserModel userModel) {

        userModel.setPassword(null);
        userModel.setFollowedByMe(false);
        userModel.clean();
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.REGISTER_CUSTOMER)
                .var("input", userModel)
                .build();

        Call<OutsmartResponse<UserModel>> call = outsmartService.getApiService().registerCostumer(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getBanner(BaseManager manager, RequestResponseHandler handler) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_BANNER_HOME)
                .build();

        enqueueCall(
                outsmartService.getApiService().getBanner(graphqlQuery),
                manager,
                handler, GraphqlQueries.GET_BANNER_HOME
        );
    }

    public void getSubcategories(String categoryID, BaseManager manager, RequestResponseHandler handler) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_SUBCATEGORIES)
                .var("categoryId", categoryID)
                .build();

        enqueueCall(outsmartService.getApiService().getSubcategories(graphqlQuery),
                manager,
                handler,
                GraphqlQueries.GET_SUBCATEGORIES + categoryID
        );
    }

    public void unselectCategory(BaseManager manager, RequestResponseHandler handler, String categoryId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.UNSELECT_CATEGORY)
                .var("categoryId", categoryId)
                .build();

        Call<OutsmartResponse<HashMap<String, String>>> call = outsmartService.getApiService().likePost(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void updateUser(BaseManager manager, RequestResponseHandler handler, UserModel userModel) {
        userModel.clean();
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.UPDATE_USER)
                .var("input", userModel)
                .build();

        Call<OutsmartResponse<UserModel>> call = outsmartService.getApiService().registerCostumer(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getUser(BaseManager manager, RequestResponseHandler handler) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_MY_USER)
                .build();

        Call<OutsmartResponse<UserModel>> call = outsmartService.getApiService().registerCostumer(graphqlQuery);

        enqueueCall(call, manager, handler, GraphqlQueries.GET_MY_USER);
    }

    public void getMyUser(BaseManager manager, RequestResponseHandler handler) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_MY_USER)
                .build();

        Call<OutsmartResponse<QueryReceiver>> call = outsmartService.getApiService().getUser(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getUser(BaseManager manager, RequestResponseHandler handler, String userId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_USER_BY_ID)
                .var("id", userId)
                .build();

        Call<OutsmartResponse<QueryReceiver>> call = outsmartService.getApiService().getUser(graphqlQuery);

        enqueueCall(call, manager, handler, GraphqlQueries.GET_USER_BY_ID + userId);
    }

    public void createPost(BaseManager manager, RequestResponseHandler handler, Post post) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.CREATE_POST)
                .var("input", post)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getJumpersResponse(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void createPost(BaseManager manager, RequestResponseHandler handler, Post post, String eventId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.CREATE_EVENT_POST)
                .var("input", post)
                .var("eventId", eventId)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getJumpersResponse(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getPosts(BaseManager manager, RequestResponseHandler handler, String lastPostId, String userId, String clubId, Integer postCount) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_POSTS)
                .var("count", postCount)
                .var("lastPostId", lastPostId)
                .var("clubId", clubId)
                .var("userId", userId)
                .build();

        Call<OutsmartResponse<HashMap<String, List<Post>>>> call = outsmartService.getApiService().getPosts(graphqlQuery);

        enqueueCall(call, manager, handler, GraphqlQueries.GET_POSTS + clubId + userId);
    }

    public void getPost(BaseManager manager, RequestResponseHandler handler, String postId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_POST)
                .var("postId", postId)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getPost(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getNotifications(BaseManager manager, RequestResponseHandler handler, String lastId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_NOTIFICATIONS)
                .var("lastId", lastId)
                .var("count", null)
                .build();

        Call<OutsmartResponse<HashMap<String, List<Notification>>>> call = outsmartService.getApiService().getNotifications(graphqlQuery);

        enqueueCall(call, manager, handler, GraphqlQueries.GET_NOTIFICATIONS + lastId);
    }

    public void likePost(BaseManager manager, RequestResponseHandler handler, String postId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.LIKE_POST)
                .var("postId", postId)
                .build();

        Call<OutsmartResponse<HashMap<String, String>>> call = outsmartService.getApiService().likePost(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void dislikePost(BaseManager manager, RequestResponseHandler handler, String postId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.DISLIKE_POST)
                .var("postId", postId)
                .build();

        Call<OutsmartResponse<HashMap<String, String>>> call = outsmartService.getApiService().dislikePost(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getFeed(BaseManager manager, RequestResponseHandler handler, String lastPostId, String lastMasterPostId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_FEED)
                .var("lastPostId", lastPostId)
                .build();

        Call<OutsmartResponse<HashMap<String, Feed>>> call = outsmartService.getApiService().getFeed(graphqlQuery);

        enqueueCall(call, manager, handler, GraphqlQueries.GET_FEED);
    }

    public void getEventFeed(BaseManager manager, RequestResponseHandler handler, String lastPostId, String eventId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_EVENT_FEED)
                .var("lastPostId", lastPostId)
                .var("eventId", eventId)
                .build();

        Call<OutsmartResponse<HashMap<String, Feed>>> call = outsmartService.getApiService().getFeed(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getMyEventsShort(BaseManager manager, RequestResponseHandler handler, String userId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_MY_EVENTS)
                .var("userId", userId)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getMyEvents(graphqlQuery);

        enqueueCall(call, manager, handler, GraphqlQueries.GET_MY_EVENTS + userId);
    }

    public void getSuggestedEventsShort(BaseManager manager, RequestResponseHandler handler, float[] location) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_EVENTS_SUGGESTIONS)
                .var("pagination", null)
                .var("myLocation", location)
                .var("count", 20)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getEventsSuggestion(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getNextEvents(BaseManager manager, RequestResponseHandler handler) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_NEXT_EVENTS)
                .var("count", 20)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getAllEvents(graphqlQuery);

        enqueueCall(call, manager, handler, GraphqlQueries.GET_NEXT_EVENTS);
    }


    public void getMyEvents(BaseManager manager, RequestResponseHandler handler, String userId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_MY_EVENTS)
                .var("userId", userId)
                .var("count", null)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getMyEvents(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getSuggestedEvents(BaseManager manager, RequestResponseHandler handler, int pagination, float[] location, String filter) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_EVENTS_SUGGESTIONS)
                .var("pagination", pagination)
                .var("myLocation", location)
                .var("count", null)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getEventsSuggestion(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getSearchEvents(BaseManager manager, RequestResponseHandler handler, String query, String filter, int pagination, float[] location, String[] categories, float[] priceRange) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_SEARCH_EVENTS)
                .var("query", query)
                .var("filter", filter)
                .var("pagination", pagination)
                .var("myLocation", location)
                .var("categories", categories)
                .var("priceRange", priceRange)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getSearchEvents(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void searchEvents(BaseManager manager, RequestResponseHandler handler, String query, int pagination, String categoryId, String subCategoryId, float[] location) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_SEARCH_EVENTS_NEW)
                .var("query", query)
                .var("pagination", pagination)
                .var("categoryId", categoryId)
                .var("location", location)
                .var("subCategoryId", subCategoryId)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getSearchEvents(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void searchEvents(BaseManager manager, RequestResponseHandler handler, String query, String filter, int pagination, float[] location, String categoryId, float[] priceRange, String subCategoryId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.SEARCH_EVENTS)
                .var("query", query)
                .var("filter", filter)
                .var("pagination", pagination)
                .var("myLocation", location)
                .var("categoryId", categoryId)
                .var("priceRange", priceRange)
                .var("subCategoryId", subCategoryId)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getSearchEvents(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getSearchMapEvents(BaseManager manager, RequestResponseHandler handler, String query, String filter, float[] location, float[] topLeftLatLon, float[] bottomRightLatLon, String[] categories, float[] priceRange) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_SEARCH_MAP_EVENTS)
                .var("query", query)
                .var("filter", filter)
                .var("myLocation", location)
                .var("topLeftLatLon", topLeftLatLon)
                .var("bottomRightLatLon", bottomRightLatLon)
                .var("categories", categories)
                .var("priceRange", priceRange)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getSearchMapEvents(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getSearchMapSuggestions(BaseManager manager, RequestResponseHandler handler, String filter, float[] location, float[] topLeftLatLon, float[] bottomRightLatLon) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_SEARCH_MAP_SUGGESTIONS)
                .var("filter", filter)
                .var("myLocation", location)
                .var("topLeftLatLon", topLeftLatLon)
                .var("bottomRightLatLon", bottomRightLatLon)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getSearchMapSuggestions(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getEvents(BaseManager manager, RequestResponseHandler handler, Boolean locationBlocked, Float[] location, String query, String[] categories, Float[] priceRange) {
        // TODO GET_EVENTS
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_EVENTS)
                .var("clubId", null)
                .var("count", null)
                .var("locationBlocked", locationBlocked)
                .var("query", query)
                .var("myLocation", location)
                .var("categories", categories)
                .var("priceRange", priceRange)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getEventsSuggestion(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getCheckinAvailableEvents(BaseManager manager, RequestResponseHandler handler, float[] location) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_CHECKIN_AVAILABLE)
                .var("myLocation", location)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getJumpersResponse(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getFriendsToInvite(BaseManager manager, RequestResponseHandler handler, String eventId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_FRIENDS_TO_INVITE)
                .var("eventId", eventId)
                .build();

        Call<OutsmartResponse<HashMap<String, List<UserCardDetails>>>> call = outsmartService.getApiService().getFollowers(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void searchFriends(BaseManager manager, RequestResponseHandler handler, String query) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.SEARCH_FRIENDS)
                .var("query", query)
                .build();

        Call<OutsmartResponse<HashMap<String, List<UserCardDetails>>>> call = outsmartService.getApiService().getFollowers(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getFriendsToChat(BaseManager manager, RequestResponseHandler handler) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.SEARCH_FRIENDS)
                .build();

        Call<OutsmartResponse<HashMap<String, List<UserCardDetails>>>> call = outsmartService.getApiService().getFollowers(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void inviteToEvent(BaseManager manager, RequestResponseHandler handler, String eventId, String invitedId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.INVITE_FRIEND)
                .var("eventId", eventId)
                .var("invitedId", invitedId)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getJumpersResponse(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void sendChatTextMessage(BaseManager manager, RequestResponseHandler handler, String receiverId, String message) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.SEND_CHAT_TEXT_MESSAGE)
                .var("receiverId", receiverId)
                .var("message", message)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getJumpersResponse(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void sendChatImageMessage(BaseManager manager, RequestResponseHandler handler, String receiverId, String photoUrl) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.SEND_CHAT_IMAGE_MESSAGE)
                .var("receiverId", receiverId)
                .var("photoUrl", photoUrl)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getJumpersResponse(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void sendGroupChatTextMessage(BaseManager manager, RequestResponseHandler handler, String groupId, String message) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.SEND_GROUP_CHAT_TEXT_MESSAGE)
                .var("groupId", groupId)
                .var("message", message)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getJumpersResponse(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void sendGroupChatImageMessage(BaseManager manager, RequestResponseHandler handler, String groupId, String photoUrl) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.SEND_GROUP_CHAT_IMAGE_MESSAGE)
                .var("groupId", groupId)
                .var("photoUrl", photoUrl)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getJumpersResponse(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void createGroup(BaseManager manager, RequestResponseHandler handler, String name, ArrayList<String> membersIds) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.CREATE_GROUP)
                .var("name", name)
                .var("users", membersIds)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getJumpersResponse(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void leaveGroup(BaseManager manager, RequestResponseHandler handler, String groupId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.LEAVE_GROUP)
                .var("groupId", groupId)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getJumpersResponse(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void editGroup(BaseManager manager, RequestResponseHandler handler, String groupId, String name) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.EDIT_GROUP)
                .var("groupId", groupId)
                .var("name", name)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getJumpersResponse(graphqlQuery);

        enqueueCall(call, manager, handler);
    }


    public void confirmPresence(BaseManager manager, RequestResponseHandler handler, Event event) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.CONFIRM_PRESENCE)
                .var("eventId", event.getId())
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().confirmPresence(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void unconfirmPresence(BaseManager manager, RequestResponseHandler handler, Event event) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.UNCONFIRM_PRESENCE)
                .var("eventId", event.getId())
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().unconfirmPresence(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getUserFollowers(BaseManager manager, RequestResponseHandler handler, String userId) {
        if (userId != null) {
            GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_USER_FOLLOWERS)
                    .var("userId", userId)
                    .build();

            Call<OutsmartResponse<HashMap<String, List<UserCardDetails>>>> call = outsmartService.getApiService().getFollowers(graphqlQuery);

            enqueueCall(call, manager, handler, GraphqlQueries.GET_USER_FOLLOWERS + userId);
        } else {
            GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_CURRENT_USER_FOLLOWERS)
                    .build();

            Call<OutsmartResponse<HashMap<String, List<UserCardDetails>>>> call = outsmartService.getApiService().getFollowers(graphqlQuery);

            enqueueCall(call, manager, handler, GraphqlQueries.GET_CURRENT_USER_FOLLOWERS);
        }
    }

    public void getUserFollowing(BaseManager manager, RequestResponseHandler handler, String userId) {
        if (userId != null) {
            GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_USER_FOLLOWING)
                    .var("userId", userId)
                    .build();

            Call<OutsmartResponse<HashMap<String, List<UserCardDetails>>>> call = outsmartService.getApiService().getFollowers(graphqlQuery);

            enqueueCall(call, manager, handler, GraphqlQueries.GET_USER_FOLLOWING + userId);
        } else {
            GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_CURRENT_USER_FOLLOWING)
                    .build();

            Call<OutsmartResponse<HashMap<String, List<UserCardDetails>>>> call = outsmartService.getApiService().getFollowers(graphqlQuery);

            enqueueCall(call, manager, handler, GraphqlQueries.GET_CURRENT_USER_FOLLOWING);
        }
    }

    public void getSearchClubs(BaseManager manager, RequestResponseHandler handler, String query) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_SEARCH_CLUBS)
                .var("query", query)
                .build();

        Call<OutsmartResponse<Club>> call = outsmartService.getApiService().getSearchClubs(graphqlQuery);


        enqueueCall(call, manager, handler);
    }

    public void searchClubs(BaseManager manager, RequestResponseHandler handler, String query, Integer pagination, String categoryId, String subcategoryId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.SEARCH_CLUBS)
                .var("query", query)
                .var("pagination", pagination)
                .var("categoryId", categoryId)
                .var("subcategoryId", subcategoryId)
                .build();

        Call<OutsmartResponse<Club>> call = outsmartService.getApiService().getSearchClubs(graphqlQuery);

        enqueueCall(call, manager, handler, GraphqlQueries.SEARCH_CLUBS + query + pagination + categoryId + subcategoryId);
    }


    public void filterResults(SearchFilterInput searchFilterInput, Integer paginate, Integer limit, BaseManager manager, RequestResponseHandler handler) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.FILTER_RESULTS)
                .var("input", searchFilterInput)
                .var("limit", limit)
                .var("paginate", paginate)
                .build();

        Call<OutsmartResponse<SearchFilterResponse>> call = outsmartService.getApiService().filterResults(graphqlQuery);
        enqueueCall(call, manager, handler, GraphqlQueries.FILTER_RESULTS + searchFilterInput.to() + paginate);
    }


    public void getStates(BaseManager manager, RequestResponseHandler handler) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_STATES)
                .build();

        Call<OutsmartResponse<StatesList>> call = outsmartService.getApiService().getStates(graphqlQuery);
        enqueueCall(call, manager, handler);

    }

    public void getCitiesByState(String state, BaseManager manager, RequestResponseHandler handler) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_CITIES_BY_STATE)
                .var("state", state)
                .build();

        Call<OutsmartResponse<CitiesList>> call = outsmartService.getApiService().getCities(graphqlQuery);
        enqueueCall(call, manager, handler);

    }


    public void getFollowedClubs(BaseManager manager, RequestResponseHandler handler, String userId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_FOLLOWED_CLUBS)
                .var("userId", userId)
                .build();

        Call<OutsmartResponse<Club>> call = outsmartService.getApiService().getFollowedClubs(graphqlQuery);


        enqueueCall(call, manager, handler);
    }

    public void followUser(BaseManager manager, RequestResponseHandler handler, String userId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.FOLLOW_USER)
                .var("userId", userId)
                .build();

        Call<OutsmartResponse<QueryReceiver>> call = outsmartService.getApiService().getUser(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void followClub(BaseManager manager, RequestResponseHandler handler, String clubId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.FOLLOW_CLUB)
                .var("clubId", clubId)
                .build();

        Call<OutsmartResponse<Club>> call = outsmartService.getApiService().followClub(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void unfollowClub(BaseManager manager, RequestResponseHandler handler, String clubId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.UNFOLLOW_CLUB)
                .var("clubId", clubId)
                .build();

        Call<OutsmartResponse<Club>> call = outsmartService.getApiService().followClub(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getClub(BaseManager manager, RequestResponseHandler handler, String clubId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_CLUB)
                .var("clubId", clubId)
                .var("eventCount", 5)
                .var("postCount", 1)
                .build();

        Call<OutsmartResponse<Club>> call = outsmartService.getApiService().getClub(graphqlQuery);

        enqueueCall(call, manager, handler, GraphqlQueries.GET_CLUB + clubId);
    }

    public void getClubEvents(BaseManager manager, RequestResponseHandler handler, String clubId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_CLUB_EVENTS)
                .var("clubId", clubId)
                .var("eventCount", null)
                .build();

        Call<OutsmartResponse<Club>> call = outsmartService.getApiService().getClubEvents(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getClubFollowers(BaseManager manager, RequestResponseHandler handler, String clubId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_CLUB_FOLLOWERS)
                .var("clubId", clubId)
                .build();

        Call<OutsmartResponse<HashMap<String, List<UserCardDetails>>>> call = outsmartService.getApiService().getClubFollowers(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getPostsLikers(BaseManager manager, RequestResponseHandler handler, String postId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_POST_LIKERS)
                .var("postId", postId)
                .build();

        Call<OutsmartResponse<HashMap<String, List<PostLiker>>>> call = outsmartService.getApiService().getPostLikers(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getEventConfirmedList(BaseManager manager, RequestResponseHandler handler, ConfirmedPeopleInput input) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_EVENT_CONFIRMED_PEOPLE)
                .var("input", input)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().graphQl(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void unfollowUser(BaseManager manager, RequestResponseHandler handler, String userId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.UNFOLLOW_USER)
                .var("userId", userId)
                .build();

        Call<OutsmartResponse<QueryReceiver>> call = outsmartService.getApiService().getUser(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getEventDetails(BaseManager manager, RequestResponseHandler handler, String eventId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_EVENT)
                .var("id", eventId)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getJumpersResponse(graphqlQuery);

        enqueueCall(call, manager, handler, GraphqlQueries.GET_EVENT + eventId);
    }

    public void deletePost(BaseManager manager, RequestResponseHandler handler, String postId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.DELETE_POST)
                .var("postId", postId)
                .build();

        Call<OutsmartResponse<HashMap<String, String>>> call = outsmartService.getApiService().likePost(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void sharePost(BaseManager manager, RequestResponseHandler handler, String postId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.SHARE_POST)
                .var("postId", postId)
                .build();

        Call<OutsmartResponse<HashMap<String, String>>> call = outsmartService.getApiService().likePost(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getUserRaffles(BaseManager manager, RequestResponseHandler handler, String userId, Integer count) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_USER_RAFFLES)
                .var("userId", userId)
                .var("count", count)
                .build();

        Call<OutsmartResponse<HashMap<String, List<Raffle>>>> call = outsmartService.getApiService().getUserRaffles(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getMyRaffles(BaseManager manager, RequestResponseHandler handler, Integer count) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_MY_RAFFLES)
                .var("count", count)
                .build();

        Call<OutsmartResponse<HashMap<String, List<Raffle>>>> call = outsmartService.getApiService().getMyRaffles(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void joinRaffle(BaseManager manager, RequestResponseHandler handler, String raffleId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.JOIN_RAFFLE)
                .var("raffleId", raffleId)
                .build();

        Call<OutsmartResponse<HashMap<String, String>>> call = outsmartService.getApiService().joinRaffle(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void leaveRaffle(BaseManager manager, RequestResponseHandler handler, String raffleId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.LEAVE_RAFFLE)
                .var("raffleId", raffleId)
                .build();

        Call<OutsmartResponse<HashMap<String, String>>> call = outsmartService.getApiService().leaveRaffle(graphqlQuery);

        enqueueCall(call, manager, handler);
    }


    public void getEventTickets(BaseManager manager, RequestResponseHandler handler, String eventId, String coupon) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_EVENT_TICKETS)
                .var("eventId", eventId)
                .var("coupon", coupon)
                .build();

        Call<OutsmartResponse<HashMap<String, List<TicketDetails>>>> call = outsmartService.getApiService().getTickets(graphqlQuery);

        enqueueCall(call, manager, handler, GraphqlQueries.GET_EVENT_TICKETS + eventId);
    }

    public void getMyIndividualTickets(BaseManager manager, RequestResponseHandler handler, String lastTicketId, Integer qtdTickets) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_MY_INDIVIDUAL_TICKETS)
                .var("lastId", lastTicketId)
                .var("count", qtdTickets)
                .build();

        Call<OutsmartResponse<HashMap<String, List<TicketDetails>>>> call = outsmartService.getApiService().getTickets(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getMyVipBoxTickets(BaseManager manager, RequestResponseHandler handler, String lastTicketId, Integer qtdTickets) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_MY_VIP_BOX_TICKETS)
                .var("lastId", lastTicketId)
                .var("count", qtdTickets)
                .build();

        Call<OutsmartResponse<HashMap<String, List<TicketDetails>>>> call = outsmartService.getApiService().getTickets(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void getMyTicketDetails(BaseManager manager, RequestResponseHandler handler, String ticketId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.GET_MY_TICKET_DETAIL)
                .var("id", ticketId)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getJumpersResponse(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void buyTicket(BaseManager manager, RequestResponseHandler handler, String ticketId, String clubId, String eventId, Integer quantity) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.BUY_TICKET)
                .var("ticketId", ticketId)
                .var("clubId", clubId)
                .var("eventId", eventId)
                .var("quantity", quantity)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().buyTicket(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void transferTicket(BaseManager manager, RequestResponseHandler handler, TransferTicketInput input) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.TRANSFER_TICKET)
                .var("input", input)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().getJumpersResponse(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void cancelTicket(BaseManager manager, RequestResponseHandler handler, String ticketId, String eventId) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.CANCEL_TICKET)
                .var("ticketId", ticketId)
                .var("eventId", eventId)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().cancelTicket(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void confirmTicket(BaseManager manager, RequestResponseHandler handler, String ticketId, String paypalData) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.CONFIRM_TICKET)
                .var("ticketId", ticketId)
                .var("paypalData", paypalData)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().confirmTicket(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void buyTicketCreditCard(BaseManager manager, RequestResponseHandler handler, BuyTicketCreditCardInput input) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.BUY_TICKET_CREDIT_CARD)
                .var("input", input)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().confirmTicket(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void buyTicketBankSlip(BaseManager manager, RequestResponseHandler handler, BuyTicketBankSlipInput input) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.BUY_TICKET_BANK_SLIP)
                .var("input", input)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().confirmTicket(graphqlQuery);

        enqueueCall(call, manager, handler);
    }

    public void confirmTokenIsValid(BaseManager manager, RequestResponseHandler<QueryResponse> handler, String originType) {

        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.CONFIRM_TOKEN_IS_VALID)
                .var("originType", originType)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().confirmTokenIsValid(graphqlQuery);

        enqueueCall(call, manager, handler);

    }

    public void updatePartnerInfo(String ingresseToken, String originType, String userId, RequestResponseHandler handler, BaseManager manager) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.UPDATE_PARTNER_INFO)
                .var("originType", originType)
                .var("token", ingresseToken)
                .var("userId", userId)
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().updatePartnerInfo(graphqlQuery);

        enqueueCall(call, manager, handler);

    }

    public void hasUpdates(AuthManager authManager, RequestResponseHandler handler, String version) {
        GraphqlQuery graphqlQuery = GraphqlQuery.builder(GraphqlQueries.HAS_UPDATES)
                .var("version", version)
                .var("os", "ANDROID")
                .build();

        Call<OutsmartResponse<QueryResponse>> call = outsmartService.getApiService().hasUpdates(graphqlQuery);

        enqueueCall(call, authManager, handler);
    }
}
