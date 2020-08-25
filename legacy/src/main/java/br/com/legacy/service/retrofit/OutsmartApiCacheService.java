package br.com.legacy.service.retrofit;

import java.util.HashMap;
import java.util.List;

import br.com.goin.component.session.user.UserModel;
import br.com.legacy.managers.dtos.Club;
import br.com.legacy.managers.dtos.Feed;
import br.com.legacy.managers.dtos.Notification;
import br.com.legacy.managers.dtos.OutsmartResponse;
import br.com.legacy.managers.dtos.Post;
import br.com.legacy.managers.dtos.PostLiker;
import br.com.legacy.managers.dtos.QueryReceiver;
import br.com.legacy.managers.dtos.QueryResponse;
import br.com.legacy.managers.dtos.Raffle;
import br.com.legacy.managers.dtos.SubcategoriesList;
import br.com.legacy.managers.dtos.TicketDetails;
import br.com.legacy.managers.dtos.UserCardDetails;
import br.com.legacy.managers.dtos.VerifyEmail;
import br.com.legacy.models.GraphqlQuery;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by ruderos on 8/9/17.
 */

public interface OutsmartApiCacheService {

    @POST("graphql")
    Call<OutsmartResponse<QueryResponse>> graphQl(@Body GraphqlQuery body); // TODO make all endpoints use this single method

    @POST("graphql")
    Call<OutsmartResponse<VerifyEmail>> verifyEmail(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<UserModel>> registerCostumer(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<SubcategoriesList>> getSubcategories(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<HashMap<String,String>>> createPost(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<QueryResponse>> searchUser(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<HashMap<String,List<Post>>>> getPosts(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<QueryResponse>> getPost(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<HashMap<String,Feed>>> getFeed(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<HashMap<String,List<Notification>>>> getNotifications(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<HashMap<String,String>>> createComment(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<HashMap<String,String>>> likePost(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<HashMap<String,String>>> dislikePost(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<HashMap<String,List<Raffle>>>> getMyRaffles(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<HashMap<String,List<Raffle>>>> getUserRaffles(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<HashMap<String,String>>> joinRaffle(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<HashMap<String,String>>> leaveRaffle(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<HashMap<String,List<UserCardDetails>>>> getFollowers(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<QueryResponse>> getMyEvents(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<QueryResponse>> getEventsSuggestion(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<QueryResponse>> getAllEvents(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<QueryResponse>> getSearchEvents(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<QueryResponse>> getSearchMapEvents(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<QueryResponse>> getSearchMapSuggestions(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<QueryResponse>> getEvents(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<QueryResponse>> confirmPresence(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<QueryResponse>> unconfirmPresence(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<Club>> getSearchClubs(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<Club>> getFollowedClubs(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<Club>> followClub(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<QueryReceiver>> getUser(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<Club>> getClub(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<Club>> getClubEvents(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<HashMap<String, List<UserCardDetails>>>> getClubFollowers(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<HashMap<String, List<PostLiker>>>> getPostLikers(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<QueryResponse>> getJumpersResponse(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<HashMap<String, List<TicketDetails>>>> getTickets(@Body GraphqlQuery body);

    @POST("graphql")
    Call<OutsmartResponse<QueryResponse>> buyTicket(@Body GraphqlQuery query);

    @POST("graphql")
    Call<OutsmartResponse<QueryResponse>> confirmTicket(@Body GraphqlQuery query);

    @POST("graphql")
    Call<OutsmartResponse<QueryResponse>> cancelTicket(@Body GraphqlQuery query);
}
