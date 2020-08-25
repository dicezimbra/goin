package br.com.legacy.models;

import android.app.Activity;
import android.content.Context;

import java.util.Objects;

import br.com.R;
import br.com.legacy.utils.DateFormatter;

/**
 * Created by teruya on 23/10/2017.
 */

public class    NotificationModel {

    public String notificationId;
    public String userName;
    public String secondName;
    public String avatar;
    public String text;
    public NotificationType type;
    public String categoryType;
    public Long time;
    public String creatorId;
    public String destinationId;
    public Activity activity;
    public Boolean followedByMe = false;

    public NotificationModel(Activity activity) {
        this.activity = activity;
    }

    public NotificationModel(Activity activity, String notificationId, String userName, String secondName, NotificationType type, Long time) {
        this.activity = activity;
        this.notificationId = notificationId;
        this.userName = userName;
        this.secondName = secondName;
        this.type = type;
        this.time = time;
    }

    public String getNotificationTime() {
        if (this.time != null) {
            return DateFormatter.getTimeDifText(this.time, activity);
        }
        return activity.getString(R.string.long_time_ago);
    }

    public enum NotificationType{
        undefined, comment, like, invite, follow, raffleWon, eventComing, friendCheckin, commentTag, postTag, addedToGroup, ticketReceived, ticketConfirmed, vipBoxInvite, THEATER
    }

    public boolean setBackgroundPrimaryColor(){
        return followedByMe;
    }

    public boolean setTextPrimaryColor(){
        return !this.followedByMe;
    }

    public String getTextButton(Context context) {
        if(!this.followedByMe) {
            return context.getString(R.string.follow);
        } else {
            return context.getString(R.string.following);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationModel that = (NotificationModel) o;
        return Objects.equals(notificationId, that.notificationId) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(secondName, that.secondName) &&
                Objects.equals(avatar, that.avatar) &&
                Objects.equals(text, that.text) &&
                type == that.type &&
                Objects.equals(time, that.time) &&
                Objects.equals(creatorId, that.creatorId) &&
                Objects.equals(destinationId, that.destinationId) &&
                Objects.equals(activity, that.activity) &&
                Objects.equals(followedByMe, that.followedByMe);
    }

    @Override
    public int hashCode() {

        return Objects.hash(notificationId, userName, secondName, avatar, text, type, time, creatorId, destinationId, activity, followedByMe);
    }
}
