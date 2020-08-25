package br.com.legacy.models;

import android.app.Activity;
import android.content.res.Resources;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;

import br.com.BR;

import br.com.legacy.managers.FeedManager;
import br.com.legacy.managers.SessionManager;
import br.com.legacy.navigation.Coordinator;
import br.com.R;
import br.com.legacy.utils.DateFormatter;

/**
 * Created by ivo on 13/09/2017.
 */

public class FeedCardModel extends BaseObservable {


    @SerializedName("feedidPost")
    public String idPost;

    @SerializedName("feedposterId")
    public String posterId;

    @SerializedName("feeduserName")
    public String userName;

    @SerializedName("feedpostTime")
    public String postTime;

    @SerializedName("feedpostLocation")
    public String postLocation;

    @SerializedName("feedpostLocationId")
    public String postLocationId;

    @SerializedName("feedeventName")
    public String eventName;

    @SerializedName("feedcheckInEventId")
    public String checkInEventId;

    @SerializedName("feedpostDescriptionText")
    public String postDescriptionText;

    @SerializedName("feedprofilePhotoUrl")
    public String profilePhotoUrl;

    @SerializedName("feedpostPhotoUrl")
    public String postPhotoUrl;

    @SerializedName("feedpostVideoUrl")
    public String postVideoUrl;

    @SerializedName("feedcommentsCounter")
    @Bindable
    public Integer commentsCounter;

    @SerializedName("feedlikesCounter")
    @Bindable
    public Integer likesCounter;

    @SerializedName("feedcommentUserName")
    public String commentUserName;

    @SerializedName("feedcommentText")
    public String commentText;

    @SerializedName("feedcommentId")
    public String commentId;

    @SerializedName("feedlat")
    public Double lat;

    @SerializedName("feedlng")
    public Double lng;

    @SerializedName("feedimagePost")
    public Drawable imagePost;

    @SerializedName("feedhasImagePost")
    public Boolean hasImagePost = false;

    @SerializedName("feedhasVideoPost")
    public Boolean hasVideoPost = false;

    @SerializedName("feedsharerId")
    public String sharerId;

    @SerializedName("feedsharerName")
    public String sharerName;

    @SerializedName("feedsharerProfilePicture")
    public String sharerProfilePicture;

    @SerializedName("feedlikedByMe")
    @Bindable
    public Boolean likedByMe = false;

    @SerializedName("feedtimeStamp")
    public Long timeStamp;

    @SerializedName("feedfeeling")
    public FeelingModel feeling;

    @SerializedName("feedmediaPath")
    public String mediaPath;

    @SerializedName("feedactionOnClickOverflow")
    public String actionOnClickOverflow;
    public boolean showInMainFeed = false;

    Activity activity;

    public FeedCardModel() {
    }

    public FeedCardModel(Activity activity) {
        this.activity = activity;
    }

    public FeedCardModel(String idPost, String posterId, String userName, String postTime, String postLocation, String postLocationId, String eventName, String checkInEventId, String postDescriptionText, String profilePhotoUrl, String postPhotoUrl, String postVideoUrl, Integer commentsCounter, Integer likesCounter, String commentUserName, String commentText, String commentId, Double lat, Double lng, Drawable imagePost, Boolean hasImagePost, Boolean hasVideoPost, String sharerId, String sharerName, String sharerProfilePicture, Boolean likedByMe, Long timeStamp, FeelingModel feeling, String mediaPath, String actionOnClickOverflow, Activity activity) {
        this.idPost = idPost;
        this.posterId = posterId;
        this.userName = userName;
        this.postTime = postTime;
        this.postLocation = postLocation;
        this.postLocationId = postLocationId;
        this.eventName = eventName;
        this.checkInEventId = checkInEventId;
        this.postDescriptionText = postDescriptionText;
        this.profilePhotoUrl = profilePhotoUrl;
        this.postPhotoUrl = postPhotoUrl;
        this.postVideoUrl = postVideoUrl;
        this.commentsCounter = commentsCounter;
        this.likesCounter = likesCounter;
        this.commentUserName = commentUserName;
        this.commentText = commentText;
        this.commentId = commentId;
        this.lat = lat;
        this.lng = lng;
        this.imagePost = imagePost;
        this.hasImagePost = hasImagePost;
        this.hasVideoPost = hasVideoPost;
        this.sharerId = sharerId;
        this.sharerName = sharerName;
        this.sharerProfilePicture = sharerProfilePicture;
        this.likedByMe = likedByMe;
        this.timeStamp = timeStamp;
        this.feeling = feeling;
        this.mediaPath = mediaPath;
        this.actionOnClickOverflow = actionOnClickOverflow;
        this.activity = activity;
    }

    public FeedCardModel(String idPost, String posterId, String userName, String postTime, String postLocation, String postLocationId, String eventName, String checkInEventId, String postDescriptionText, String profilePhotoUrl, String postPhotoUrl, String postVideoUrl, Integer commentsCounter, Integer likesCounter, String commentUserName, String commentText, String commentId, Double lat, Double lng, Drawable imagePost, Boolean hasImagePost, Boolean hasVideoPost, String sharerId, String sharerName, String sharerProfilePicture, Boolean likedByMe, Long timeStamp, FeelingModel feeling, String mediaPath, String actionOnClickOverflow) {
        this.idPost = idPost;
        this.posterId = posterId;
        this.userName = userName;
        this.postTime = postTime;
        this.postLocation = postLocation;
        this.postLocationId = postLocationId;
        this.eventName = eventName;
        this.checkInEventId = checkInEventId;
        this.postDescriptionText = postDescriptionText;
        this.profilePhotoUrl = profilePhotoUrl;
        this.postPhotoUrl = postPhotoUrl;
        this.postVideoUrl = postVideoUrl;
        this.commentsCounter = commentsCounter;
        this.likesCounter = likesCounter;
        this.commentUserName = commentUserName;
        this.commentText = commentText;
        this.commentId = commentId;
        this.lat = lat;
        this.lng = lng;
        this.imagePost = imagePost;
        this.hasImagePost = hasImagePost;
        this.hasVideoPost = hasVideoPost;
        this.sharerId = sharerId;
        this.sharerName = sharerName;
        this.sharerProfilePicture = sharerProfilePicture;
        this.likedByMe = likedByMe;
        this.timeStamp = timeStamp;
        this.feeling = feeling;
        this.mediaPath = mediaPath;
        this.actionOnClickOverflow = actionOnClickOverflow;

    }

    public FeedCardModel(String idPost, String userName, String postTime, String eventName, String postDescriptionText, String profilePhotoUrl, String postPhotoUrl, Integer commentsCounter, Integer likesCounter, String commentUserName, String commentText) {
        this.idPost = idPost;
        this.userName = userName;
        this.postTime = postTime;
        this.eventName = eventName;
        this.postDescriptionText = postDescriptionText;
        this.profilePhotoUrl = profilePhotoUrl;
        this.postPhotoUrl = postPhotoUrl;
        this.commentsCounter = commentsCounter;
        this.likesCounter = likesCounter;
        this.commentUserName = commentUserName;
        this.commentText = commentText;
    }

//    public String getPostTime() {
//        if (timeStamp != null) {
//            return DateFormatter.getTimeDifText(this.timeStamp, activity);
//        }
//        return activity.getString(R.string.long_time_ago);
//    }

    public String getPostTime() {
        if (timeStamp != null) {
            return DateFormatter.stringToTimestampBrazilian(this.timeStamp);
        }
        return activity.getString(R.string.long_time_ago);
    }

    public String getPostLikes() {
        return "" + likesCounter;
    }

    public void onClickLikesCount() {
        if (this.likesCounter != null && this.likesCounter > 0) {
            Coordinator.goToPostLikers(activity, this.idPost);
        }
    }

    public void onClickLikedByMe() {
        this.likedByMe = !this.likedByMe;
        if (this.likedByMe)
            this.likesCounter++;
        else
            this.likesCounter--;
        notifyPropertyChanged(BR.likesCounter);
        notifyPropertyChanged(BR.likedByMe);
        FeedManager.likeSubject.onNext(this);
    }

    public Boolean hasLocation() {
        return this.eventName != null;
    }

    public Boolean isShareable() {
        if (SessionManager.getInstance().getCurrentUser(activity) != null) {
            Boolean isCheckin = hasLocation();
            Boolean isMine = (posterId != null && posterId.equals(SessionManager.getInstance().getCurrentUser(activity).getId()));
            return !(isCheckin || isMine);
        } else {
            return false;
        }
    }

    public Boolean isShared() {
        return this.sharerId != null;
    }

    public void setHeaderCard(int primaryColor, int highlightColor, Resources res, TextView textView) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        ForegroundColorSpan lightBlue = new ForegroundColorSpan(highlightColor);
        StyleSpan bold = new StyleSpan(Typeface.BOLD);

        if (this.userName != null) {
            Spannable spannableUser = new SpannableString(this.userName);
            spannableUser.setSpan(new ForegroundColorSpan(primaryColor), 0, this.userName.length(), 0);
            spannableUser.setSpan(bold, 0, this.userName.length(), 0);
            builder.append(spannableUser);
        }

        SpannableString span;

        if (this.feeling != null) {
            span = new SpannableString(" " + res.getString(R.string.is_feeling) + " ");
            builder.append(span);

            Spannable spanFeeling = new SpannableString(this.feeling.name);
            spanFeeling.setSpan(lightBlue, 0, this.feeling.name.length(), 0);
            spanFeeling.setSpan(new StyleSpan(Typeface.BOLD), 0, this.feeling.name.length(), 0);
            builder.append(spanFeeling);

            if (this.postLocation != null) {
                span = new SpannableString(" " + res.getString(R.string.in) + " ");
                builder.append(span);

                Spannable spanEvent = new SpannableString(this.eventName);
                spanEvent.setSpan(lightBlue, 0, this.eventName.length(), 0);
                spanEvent.setSpan(new StyleSpan(Typeface.BOLD), 0, this.eventName.length(), 0);
                builder.append(spanEvent);
            }
        } else {
            if (this.postLocation != null) {
                span = new SpannableString(" " + res.getString(R.string.did_checkin) + " ");
                builder.append(span);
                if (eventName == null) {
                    eventName = "";
                }
                Spannable spanEvent = new SpannableString(eventName);
                spanEvent.setSpan(lightBlue, 0, this.eventName.length(), 0);
                spanEvent.setSpan(new StyleSpan(Typeface.BOLD), 0, this.eventName.length(), 0);
                builder.append(spanEvent);
            }
        }

        textView.setText(builder, TextView.BufferType.SPANNABLE);
    }

    public void setSharedCard(TextView textView) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        StyleSpan bold = new StyleSpan(Typeface.BOLD);

        if (this.sharerName != null) {
            Spannable spannableAuthor = new SpannableString(this.sharerName);
            spannableAuthor.setSpan(bold, 0, this.sharerName.length(), 0);
            builder.append(spannableAuthor);

            Spannable sharedAPost = new SpannableString(" " + R.string.shared_a_post);
            builder.append(sharedAPost);
        }

        textView.setText(builder, TextView.BufferType.SPANNABLE);
    }

    public int getImageVisible() {
        if (hasImagePost)
            return View.VISIBLE;
        else if (hasVideoPost)
            return View.VISIBLE;
        else
            return View.GONE;
    }

    public void setCommentCard(TextView commentTextView) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        StyleSpan bold = new StyleSpan(Typeface.BOLD);

        if (this.commentUserName != null) {
            Spannable spannableAuthor = new SpannableString(this.commentUserName);
            spannableAuthor.setSpan(bold, 0, this.commentUserName.length(), 0);
            builder.append(spannableAuthor);
        }

        if (this.commentText != null && commentText.length() > 0) {
            Spannable spannableComment = new SpannableString(" " + this.commentText);
            builder.append(spannableComment);
        } else {
            commentTextView.setVisibility(View.GONE);
        }

        commentTextView.setText(builder, TextView.BufferType.SPANNABLE);
    }

}
