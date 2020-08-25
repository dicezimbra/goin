package br.com.goin.feature.splash

import android.app.Activity
import android.net.Uri
import java.util.ArrayList

import br.com.goin.component.navigation.NavigationController


object NavigationManager {

    val LikePost = "LikePost"
    val CommentPost = "CommentPost"
    val FollowUser = "FollowUser"
    val RaffleWon = "RaffleWon"
    val FriendCheckin = "FriendCheckin"
    val ComingEvent = "ComingEvent"
    val FriendInvite = "FriendInvite"
    val CommentTag = "CommentTag"
    val PostTag = "PostTag"
    val AddedToGroup = "AddedToGroup"
    val TicketReceived = "TicketReceived"
    val TicketConfirmed = "TicketConfirmed"
    val VipBoxInvite = "VipBoxInvite"
    val PostLikers = "PostLikers"
    val Comments = "Comments"
    val UserProfile = "UserProfile"
    val EventDetail = "EventDetail"
    val ClubDetail = "ClubDetail"
    val TheaterDetail = "TheaterDetail"
    val SinglePost = "SinglePost"
    val Chat = "Chat"
    val PurchaseDetails = "PurchaseDetails"
    val Home = "Home"
    val PurchaseTicket = "PurchaseTicket"
    val TheaterSessionDetail = "TheaterSessionDetail"

    private val navigationController = NavigationController.instance


    fun extractNotificationArgs(activity: Activity, destination: String): List<String> {
        return getArgs(activity, destination)
    }

    fun extractUriArgs(activity: Activity, uri: String, args: MutableList<String>): String? {
        var uri = uri

        if (uri.contains(";;")) {
            uri = uri.replace(";;", "&")
        }

        val oUri = Uri.parse(uri)

        val queryParameterNames = oUri.queryParameterNames

        for (key in queryParameterNames) {
            args.add(oUri.getQueryParameter(key))
        }

        return oUri.lastPathSegment

    }


    private fun getArgs(activity: Activity, destination: String): List<String> {

        val args = ArrayList<String>()

        when (destination) {
            LikePost -> args.add(activity.intent.getStringExtra("postId"))
            PostTag -> args.add(activity.intent.getStringExtra("postId"))
            AddedToGroup -> {
                args.add(activity.intent.getStringExtra("groupId"))
                args.add(activity.intent.getStringExtra("groupName"))
                args.add(activity.intent.getStringExtra("groupId"))
            }
            FollowUser -> args.add(activity.intent.getStringExtra("userId"))
            RaffleWon ->
                args.add(activity.intent.getStringExtra("userId"))
            CommentPost -> args.add(activity.intent.getStringExtra("postId"))
            CommentTag -> args.add(activity.intent.getStringExtra("postId"))
            TicketReceived -> args.add(activity.intent.getStringExtra("ticketId"))
            TicketConfirmed -> args.add(activity.intent.getStringExtra("ticketId"))
            FriendCheckin -> {
                args.add(activity.intent.getStringExtra("eventId"))
                args.add(activity.intent.getStringExtra("clubId"))
            }
            ComingEvent -> {
                args.add(activity.intent.getStringExtra("eventId"))
                args.add(activity.intent.getStringExtra("clubId"))
            }
            FriendInvite -> {
                args.add(activity.intent.getStringExtra("eventId"))
                args.add(activity.intent.getStringExtra("clubId"))
            }
            VipBoxInvite -> args.add(activity.intent.getStringExtra("ticketId"))
            TheaterDetail -> args.add(activity.intent.getStringExtra("eventId"))

            else -> {
            }
        }

        return args
    }

    fun selectDestination(activity: Activity, dest: String?, args: List<String>): Boolean {
         var destination = mapDeprecatedEvents(dest)
        when (destination) {
            PostLikers -> {
                NavigationController.instance?.legacyApp()?.goToPostLikes(activity, args[0])
                return true
            }
            SinglePost -> {
                NavigationController.instance?.legacyApp()?.goToSinglePost(activity, args[0])
                return true
            }
            UserProfile -> {
                NavigationController.instance?.loginModule()?.goToLogin(activity) {
                    NavigationController.instance?.profileModule()?.goToProfile(activity, args[0])
                }
                return true
            }
            Comments -> {
                NavigationController.instance?.loginModule()?.goToLogin(activity) {
                    NavigationController.instance?.legacyApp()?.goToComment(activity, args[0], 1)
                }
                return true
            }
            PurchaseDetails -> {
                NavigationController.instance?.loginModule()?.goToLogin(activity) {
                    NavigationController.instance?.legacyApp()?.redirectToPurchaseDetails(activity, args[0])
                }
                return true
            }
            EventDetail -> {
                if (args.size > 1)
                    NavigationController.instance!!.eventModule().goToEvent(activity, /**/args[0], /**/args[1])
                else
                    NavigationController.instance!!.eventModule().goToEvent(activity, /**/args[0])
                return true
            }
            ClubDetail -> {
                if (args.size > 1)
                    NavigationController.instance!!.placeModule().goToPlace(activity, args[0], "")
                else
                    NavigationController.instance!!.placeModule().goToPlace(activity, args[0], "")
                return true
            }
            VipBoxInvite -> {
                NavigationController.instance!!.legacyApp().goToVipBoxPayment(activity, /**/args[0])
                return true
            }
            TheaterDetail -> {
                NavigationController.instance!!.theatherPlayMovieModule().goToTheatersActivity(activity, args[0], "")
                return true
            }
            PurchaseTicket -> {
                NavigationController.instance?.loginModule()?.goToLogin(activity) {
                    if (args.size == 3)  NavigationController.instance!!.legacyApp().goToEventTickets(activity, args[1], args[0], args[2])
                }
                return true
            }
            TheaterSessionDetail ->
                navigationController!!.legacyApp().goToTheatersDetailActivity(activity, args[0], "", "")
            Home -> {
                NavigationController.instance!!.homeModule().goToHome(activity)
                return true
            }
            else -> return false
        }
        return false
    }

    private fun mapDeprecatedEvents(destination: String?): String {
        var destination = destination

        if (destination == null) destination = ""

        when (destination) {
            LikePost -> return PostLikers
            PostTag -> return SinglePost
            AddedToGroup -> return Chat
            FollowUser -> return UserProfile
            RaffleWon ->
                return UserProfile
            CommentPost -> return Comments
            CommentTag -> return Comments
            TicketReceived -> return PurchaseDetails
            TicketConfirmed -> return PurchaseDetails
            FriendCheckin -> return EventDetail
            ComingEvent -> return EventDetail
            FriendInvite -> return EventDetail
            PostLikers -> return PostLikers
            SinglePost -> return SinglePost
            Chat -> return Chat
            UserProfile -> return UserProfile
            Comments -> return Comments
            PurchaseDetails -> return PurchaseDetails
            EventDetail -> return EventDetail
            ClubDetail -> return ClubDetail
            VipBoxInvite -> return VipBoxInvite
            PurchaseTicket -> return PurchaseTicket
            TheaterDetail -> return TheaterDetail
            TheaterSessionDetail -> return TheaterSessionDetail
            Home -> return Home
        }

        return ""
    }


}
