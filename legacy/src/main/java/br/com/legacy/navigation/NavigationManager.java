package br.com.legacy.navigation;

import android.app.Activity;
import android.net.Uri;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import br.com.goin.component.navigation.NavigationController;


public class NavigationManager {

    //destinos antigos
    public static final String LikePost = "LikePost";
    public static final String CommentPost = "CommentPost";
    public static final String FollowUser = "FollowUser";
    public static final String RaffleWon = "RaffleWon";
    public static final String FriendCheckin = "FriendCheckin";
    public static final String ComingEvent = "ComingEvent";
    public static final String FriendInvite = "FriendInvite";
    public static final String CommentTag = "CommentTag";
    public static final String PostTag = "PostTag";
    public static final String AddedToGroup = "AddedToGroup";
    public static final String TicketReceived = "TicketReceived";
    public static final String TicketConfirmed = "TicketConfirmed";
    public static final String VipBoxInvite = "VipBoxInvite";

    //novos destinos
    public static final String PostLikers = "PostLikers";
    public static final String Comments = "Comments";
    public static final String UserProfile = "UserProfile";
    public static final String EventDetail = "EventDetail";
    public static final String ClubDetail = "ClubDetail";
    public static final String TheaterDetail = "TheaterDetail";
    public static final String SinglePost = "SinglePost";
    public static final String Chat = "Chat";
    public static final String PurchaseDetails = "PurchaseDetails";
    public static final String Home = "Home";
    public static final String PurchaseTicket = "PurchaseTicket";
    public static final String TheaterSessionDetail = "TheaterSessionDetail";

    private static final NavigationController navigationController = NavigationController.Companion.getInstance();

    @StringDef({LikePost, CommentPost, FollowUser, RaffleWon, FriendCheckin, ComingEvent, FriendInvite, CommentTag, PostTag, AddedToGroup, TicketReceived, TicketConfirmed, VipBoxInvite
            , PostLikers, Comments, UserProfile, EventDetail, ClubDetail, SinglePost, Chat, PurchaseDetails, Home, TheaterDetail})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SelectedDestination {
    }


    public static List<String> extractNotificationArgs(Activity activity, @SelectedDestination String destination) {
        return getArgs(activity, destination);
    }

    public static String extractUriArgs(Activity activity, String uri, List<String> args) {

        if(uri.contains(";;")){
            uri = uri.replace(";;", "&");
        }

        Uri oUri = Uri.parse(uri);

        Set<String> queryParameterNames = oUri.getQueryParameterNames();

        for (String key : queryParameterNames) {
            args.add(oUri.getQueryParameter(key));
        }

        return oUri.getLastPathSegment();

    }


    private static List<String> getArgs(Activity activity, String destination) {

        List<String> args = new ArrayList<>();

        switch (destination) {
            case LikePost:
                args.add(activity.getIntent().getStringExtra("postId"));
                break;
            case PostTag:
                args.add(activity.getIntent().getStringExtra("postId"));
                break;
            case AddedToGroup:
                args.add(activity.getIntent().getStringExtra("groupId"));
                args.add(activity.getIntent().getStringExtra("groupName"));
                args.add(activity.getIntent().getStringExtra("groupId"));
                break;
            case FollowUser:
                args.add(activity.getIntent().getStringExtra("userId"));
                break;
            case RaffleWon:
                //TODO alterar o caminho quando a tela de raffle estiver definida
                args.add(activity.getIntent().getStringExtra("userId"));
                break;
            case CommentPost:
                args.add(activity.getIntent().getStringExtra("postId"));
                break;
            case CommentTag:
                args.add(activity.getIntent().getStringExtra("postId"));
                break;
            case TicketReceived:
                args.add(activity.getIntent().getStringExtra("ticketId"));
                break;
            case TicketConfirmed:
                args.add(activity.getIntent().getStringExtra("ticketId"));
                break;
            case FriendCheckin:
                args.add(activity.getIntent().getStringExtra("eventId"));
                args.add(activity.getIntent().getStringExtra("clubId"));
                break;
            case ComingEvent:
                args.add(activity.getIntent().getStringExtra("eventId"));
                args.add(activity.getIntent().getStringExtra("clubId"));
                break;
            case FriendInvite:
                args.add(activity.getIntent().getStringExtra("eventId"));
                args.add(activity.getIntent().getStringExtra("clubId"));
                break;
            case VipBoxInvite:
                args.add(activity.getIntent().getStringExtra("ticketId"));
                break;
            case TheaterDetail:
                args.add(activity.getIntent().getStringExtra("eventId"));
                break;

            default:
                break;

        }

        return args;
    }

    public static boolean selectDestination(Activity activity, String id, @SelectedDestination String destination, List<String> args) {
        destination = mapDeprecatedEvents(destination);

        switch (destination) {
            case PostLikers:
                Coordinator.goToPostLikers(activity, args.get(0));
                return true;
            case SinglePost:
                Coordinator.goToSinglePost(activity, /**/args.get(0));
                return true;
            case UserProfile:
                NavigationController.Companion.getInstance().profileModule().goToProfile(activity, args.get(0));

                return true;
            case PurchaseDetails:
                Coordinator.redirectToPurchaseDetails(activity,/**/args.get(0));
                return true;
            case EventDetail:
                if (args.size() > 1)
                    NavigationController.Companion.getInstance().eventModule().goToEvent(activity, /**/args.get(0), /**/args.get(1));
                else
                    NavigationController.Companion.getInstance().eventModule().goToEvent(activity, /**/args.get(0), /**/null);
                return true;
            case ClubDetail:
                if (args.size() > 1)
                    NavigationController.Companion.getInstance().placeModule().goToPlace(activity, args.get(0), "");
                else
                    NavigationController.Companion.getInstance().placeModule().goToPlace(activity, args.get(0), "");
                return true;
            case VipBoxInvite:
                Coordinator.goToVipBoxPayment(activity,/**/args.get(0));
                return true;
            case TheaterDetail:
                NavigationController.Companion.getInstance().theatherPlayMovieModule().goToTheatersActivity(activity,args.get(0), "");
                return true;
            case PurchaseTicket:
                if(args.size() < 3){
                    Coordinator.goToTabsMain(activity);
                    return true;
                }
                return true;
            case TheaterSessionDetail:

                navigationController.legacyApp().goToTheatersDetailActivity(activity, args.get(0), "", "");
                break;
            case Home:
                Coordinator.goToTabsMain(activity);
                return true;
            default: return false;
        }
        return false;
    }

    private static String mapDeprecatedEvents(@SelectedDestination String destination) {

        if (destination == null) destination = "";

        switch (destination) {
            case LikePost:
                return PostLikers;
            case PostTag:
                return SinglePost;
            case AddedToGroup:
                return Chat;
            case FollowUser:
                return UserProfile;
            case RaffleWon:
                //TODO alterar o caminho quando a tela de raffle estiver definida
                return UserProfile;
            case CommentPost:
                return Comments;
            case CommentTag:
                return Comments;
            case TicketReceived:
                return PurchaseDetails;
            case TicketConfirmed:
                return PurchaseDetails;
            case FriendCheckin:
                return EventDetail;
            case ComingEvent:
                return EventDetail;
            case FriendInvite:
                return EventDetail;
            case PostLikers:
                return PostLikers;
            case SinglePost:
                return SinglePost;
            case Chat:
                return Chat;
            case UserProfile:
                return UserProfile;
            case Comments:
                return Comments;
            case PurchaseDetails:
                return PurchaseDetails;
            case EventDetail:
                return EventDetail;
            case ClubDetail:
                return ClubDetail;
            case VipBoxInvite:
                return VipBoxInvite;
            case PurchaseTicket:
                return PurchaseTicket;
            case TheaterDetail:
                return TheaterDetail;
            case TheaterSessionDetail:
                return TheaterSessionDetail;
            case Home:
                return Home;
        }

        return "";
    }


}
