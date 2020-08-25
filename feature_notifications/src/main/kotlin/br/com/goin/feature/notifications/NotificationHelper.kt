package br.com.goin.feature.notifications

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import br.com.goin.component.navigation.NavigationController
import br.com.goin.feature.notifications.model.Notification
import br.com.goin.feature.notifications.model.NotificationType

object NotificationHelper{

    fun showUnfollowAlert(context: Context, userName: String?, callback: () -> Unit) {
        val builder = android.app.AlertDialog.Builder(context)
        val unfollowAlert = String.format(context.getString(R.string.unfollow_alert), userName)
        builder.setMessage(unfollowAlert)
        builder.setPositiveButton(R.string.unfollow) { _, _ ->
            callback()
        }.setNegativeButton(R.string.cancel){ dialog, _ -> dialog.dismiss() }
        builder.create()
        builder.show()
    }
    
    fun goToDeeplink(notification: Notification, navigationController: NavigationController, activity: Activity){
        if ("THEATER" == notification.categoryType) {
            navigationController.theatherPlayMovieModule().goToTheaterDetail(activity, notification.destinationId)
        } else {
            when (notification.type) {
                NotificationType.like -> navigationController.legacyApp().goToPostLikersFromNotification(activity, notification.destinationId)
                NotificationType.invite -> navigationController.eventModule().goToEvent(activity, notification.destinationId)
                NotificationType.follow -> navigationController.profileModule().goToProfile(activity, notification.destinationId)
                NotificationType.eventComing -> navigationController.eventModule().goToEvent(activity, notification.destinationId)
                NotificationType.friendCheckin -> navigationController.eventModule().goToEvent(activity, notification.destinationId)
                NotificationType.commentTag -> navigationController.legacyApp().goToComments(activity, notification.destinationId)
                NotificationType.postTag -> navigationController.legacyApp().goToSinglePost(activity, notification.destinationId)
                NotificationType.ticketConfirmed -> navigationController.legacyApp().redirectToPurchaseDetails(activity, notification.destinationId)
                NotificationType.ticketReceived -> navigationController.legacyApp().redirectToPurchaseDetails(activity, notification.destinationId)
                NotificationType.vipBoxInvite -> navigationController.legacyApp().goToVipBoxPayment(activity, notification.destinationId)
                NotificationType.THEATER -> navigationController.theatherPlayMovieModule().goToTheaterDetail(activity, notification.destinationId)
                else -> {}
            }
        }
    }
    
    fun getNotificationText(context: Context, notification: Notification): SpannableString {
        var text : String
        val PRIMARY_DARK_COLOR = Color.parseColor("#ff5851")
        val GRAY_COLOR = Color.parseColor("#7d7e7e")

        var spannableString = SpannableString("")
        val resources = context.resources
        
        when (notification.type) {
            NotificationType.comment -> if (notification.userName != null) {
                text = String.format(resources.getString(R.string.notification_comment), notification.userName)
                spannableString = SpannableString(text)
                setSubstringColor(spannableString, PRIMARY_DARK_COLOR, 0, notification.userName.length)
                setSubstringColor(spannableString, GRAY_COLOR, notification.userName.length + 1, spannableString.length)
            }
            NotificationType.invite -> if (notification.userName != null && notification.secondName != null) {
                text = String.format(resources.getString(R.string.notification_invite), notification.userName, notification.secondName)
                spannableString = SpannableString(text)
                setSubstringColor(spannableString, PRIMARY_DARK_COLOR, 0, notification.userName.length)
                setSubstringColor(spannableString, GRAY_COLOR, notification.userName.length + 1, spannableString.length - notification.secondName.length - 1)
                setSubstringColor(spannableString, PRIMARY_DARK_COLOR, spannableString.length - notification.secondName.length, spannableString.length)
            }
            NotificationType.like -> if (notification.userName != null) {
                text = String.format(resources.getString(R.string.notification_like), notification.userName)
                spannableString = SpannableString(text)
                setSubstringColor(spannableString, PRIMARY_DARK_COLOR, 0, notification.userName.length)
                setSubstringColor(spannableString, GRAY_COLOR, notification.userName.length + 1, spannableString.length)
            }
            NotificationType.follow -> if (notification.userName != null) {
                text = String.format(resources.getString(R.string.notification_follow), notification.userName)
                spannableString = SpannableString(text)
                setSubstringColor(spannableString, PRIMARY_DARK_COLOR, 0, notification.userName.length)
                setSubstringColor(spannableString, GRAY_COLOR, notification.userName.length + 1, spannableString.length)
            }
            NotificationType.eventComing -> if (notification.userName != null) {
                text = String.format(resources.getString(R.string.notification_event), notification.userName)
                spannableString = SpannableString(text)
                setSubstringColor(spannableString, GRAY_COLOR, 0, spannableString.length - notification.userName.length - 1)
                setSubstringColor(spannableString, PRIMARY_DARK_COLOR, spannableString.length - notification.userName.length, spannableString.length)
            }
            NotificationType.friendCheckin -> if (notification.userName != null && notification.secondName != null) {
                text = String.format(resources.getString(R.string.notification_checkin), notification.userName, notification.secondName)
                spannableString = SpannableString(text)
                setSubstringColor(spannableString, PRIMARY_DARK_COLOR, 0, notification.userName.length)
                setSubstringColor(spannableString, GRAY_COLOR, notification.userName.length + 1, spannableString.length - notification.secondName.length - 1)
                setSubstringColor(spannableString, PRIMARY_DARK_COLOR, spannableString.length - notification.secondName.length, spannableString.length)
            }
            NotificationType.commentTag -> if (notification.userName != null) {
                text = String.format(resources.getString(R.string.notification_comment_tag), notification.userName)
                spannableString = SpannableString(text)
                setSubstringColor(spannableString, PRIMARY_DARK_COLOR, 0, notification.userName.length)
                setSubstringColor(spannableString, GRAY_COLOR, notification.userName.length + 1, spannableString.length)
            }
            NotificationType.postTag -> if (notification.userName != null) {
                text = String.format(resources.getString(R.string.notification_post_tag), notification.userName)
                spannableString = SpannableString(text)
                setSubstringColor(spannableString, PRIMARY_DARK_COLOR, 0, notification.userName.length)
                setSubstringColor(spannableString, GRAY_COLOR, notification.userName.length + 1, spannableString.length)
            }
            NotificationType.addedToGroup -> if (notification.userName != null && notification.secondName != null) {
                text = String.format(resources.getString(R.string.notification_added_to_group), notification.userName, notification.secondName)
                spannableString = SpannableString(text)
                setSubstringColor(spannableString, PRIMARY_DARK_COLOR, 0, notification.userName.length)
                setSubstringColor(spannableString, GRAY_COLOR, notification.userName.length + 1, spannableString.length - notification.secondName.length - 1)
                setSubstringColor(spannableString, PRIMARY_DARK_COLOR, spannableString.length - notification.secondName.length, spannableString.length)
            }
            NotificationType.ticketReceived -> if (notification.userName != null && notification.secondName != null) {
                text = String.format(resources.getString(R.string.notification_ticket_received), notification.userName, notification.secondName)
                spannableString = SpannableString(text)
                setSubstringColor(spannableString, PRIMARY_DARK_COLOR, 0, notification.userName.length)
                setSubstringColor(spannableString, GRAY_COLOR, notification.userName.length + 1, spannableString.length - notification.secondName.length - 1)
                setSubstringColor(spannableString, PRIMARY_DARK_COLOR, spannableString.length - notification.secondName.length, spannableString.length)
            }
            NotificationType.ticketConfirmed -> if (notification.userName != null) {
                text = String.format(resources.getString(R.string.notification_ticket_confirmed), notification.userName)
                spannableString = SpannableString(text)
                setSubstringColor(spannableString, PRIMARY_DARK_COLOR, 0, notification.userName.length)
                setSubstringColor(spannableString, GRAY_COLOR, notification.userName.length + 1, spannableString.length)
            }
            NotificationType.vipBoxInvite -> if (notification.userName != null && notification.secondName != null) {
                text = String.format(resources.getString(R.string.notification_vip_box_invite), notification.userName, notification.secondName)
                spannableString = SpannableString(text)
                setSubstringColor(spannableString, PRIMARY_DARK_COLOR, 0, notification.userName.length)
                setSubstringColor(spannableString, GRAY_COLOR, notification.userName.length + 1, spannableString.length - notification.secondName.length - 1)
                setSubstringColor(spannableString, PRIMARY_DARK_COLOR, spannableString.length - notification.secondName.length, spannableString.length)
            }
            else -> spannableString = SpannableString("")
        }
        
        return spannableString
    }

    private fun setSubstringColor(ss: SpannableString, color: Int, startPosition: Int, endPosition: Int) {
        ss.setSpan(ForegroundColorSpan(color), startPosition, endPosition, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
    }
}