package br.com.legacy.models;

import android.app.Activity;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import br.com.BR;

import br.com.goin.component.model.event.Event;
import br.com.goin.component.ui.kit.dialog.DialogUtils;
import br.com.goin.component.navigation.NavigationController;
import br.com.legacy.managers.ClubManager;
import br.com.R;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

/**
 * Created by kalunga on 18/09/2017.
 */

public class ClubModel extends BaseObservable implements Serializable {

    NavigationController navigationController = NavigationController.Companion.getInstance();

    public interface ClubItemHandler {
        void onClickFollowButton(ClubModel model);
    }

    public Activity activity;

    public String clubId;
    public String clubSite;
    @Bindable
    public Integer followers;
    public String clubName;
    public String clubLocation;
    public String clubDescription;
    public String address;
    public List<FeedCardModel> posts;
    public List<br.com.goin.component.model.event.Event> events;
    public String coverImage;
    public ImageView photoProfile;
    public ImageView photoPost;
    public String clubLogoUrl;
    public Drawable clubImageLogo;
    public Float rating;
    public Integer ratingCount;
    public List<String> galleryUrls;
    public Float distance;
    public boolean hasLogo;
    public Float latitude;
    public Float longitude;

    public LikeClick onLikeClicked;

    @FunctionalInterface
    public interface LikeClick {
        void invoke();
    }

    @Bindable
    public boolean isFollowing = false;

    public ClubItemHandler handler;

    public ClubModel() {
    }

    public ClubModel(Activity activity, String clubId, String clubSite, Integer followers, String clubName, String clubLocation, String clubDescription, String address, List<FeedCardModel> posts, List<br.com.goin.component.model.event.Event> events, String coverImage, ImageView photoProfile, ImageView photoPost, String clubLogoUrl, Drawable clubImageLogo, Float rating, Integer ratingCount, List<String> galleryUrls, Float distance, boolean hasLogo, Float latitude, Float longitude, boolean isFollowing, ClubItemHandler handler) {
        this.activity = activity;
        this.clubId = clubId;
        this.clubSite = clubSite;
        this.followers = followers;
        this.clubName = clubName;
        this.clubLocation = clubLocation;
        this.clubDescription = clubDescription;
        this.address = address;
        this.posts = posts;
        this.events = events;
        this.coverImage = coverImage;
        this.photoProfile = photoProfile;
        this.photoPost = photoPost;
        this.clubLogoUrl = clubLogoUrl;
        this.clubImageLogo = clubImageLogo;
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.galleryUrls = galleryUrls;
        this.distance = distance;
        this.hasLogo = hasLogo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isFollowing = isFollowing;
        this.handler = handler;
    }

    public ClubModel(String clubId, String clubSite, Integer followers, String clubName, String clubLocation, String clubDescription, String address, List<FeedCardModel> posts, List<Event> events, String coverImage, ImageView photoProfile, ImageView photoPost, String clubLogoUrl, Drawable clubImageLogo, Float rating, Integer ratingCount, List<String> galleryUrls, Float distance, boolean hasLogo, Float latitude, Float longitude, boolean isFollowing) {

        this.clubId = clubId;
        this.clubSite = clubSite;
        this.followers = followers;
        this.clubName = clubName;
        this.clubLocation = clubLocation;
        this.clubDescription = clubDescription;
        this.address = address;
        this.posts = posts;
        this.events = events;
        this.coverImage = coverImage;
        this.photoProfile = photoProfile;
        this.photoPost = photoPost;
        this.clubLogoUrl = clubLogoUrl;
        this.clubImageLogo = clubImageLogo;
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.galleryUrls = galleryUrls;
        this.distance = distance;
        this.hasLogo = hasLogo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isFollowing = isFollowing;

    }

    public ClubModel(Activity activity) {
        this.activity = activity;
    }

    public ClubModel(String name, String location) {
        this.clubName = name;
        this.clubLocation = location;
    }

    public void onClickFollowButton() {
        if(onLikeClicked != null) onLikeClicked.invoke();

        navigationController.loginModule().goToLogin(activity, () -> {
            if (this.isFollowing) {
                DialogUtils.INSTANCE.showDecisionDialog(activity, activity.getString(R.string.confirm_unfollow_club),
                        activity.getString(R.string.yes), activity.getString(R.string.no), new DialogUtils.DecisionListener() {
                            @Override
                            public void onAccept() {
                                setIsFollowing(false);
                            }

                            @Override
                            public void onReject() {

                            }
                        });
            } else {
                setIsFollowing(true);
            }

            return null;
        });

    }

    public String getRatingText() {
        String text = "";

        if (this.ratingCount > 1) {

            text = activity.getString(R.string.see_all_views_establishment_start)
                    + " "
                    + this.ratingCount
                    + " "
                    + activity.getString(R.string.see_all_views_establishment_end);
        } else {

            text = activity.getString(R.string.see_all_views_establishment_start_singular)
                    + " "
                    + this.ratingCount
                    + " "
                    + activity.getString(R.string.see_all_views_establishment_end_singular);
        }
        return text;
    }

    public void setIsFollowing(boolean isFollowing) {
        this.isFollowing = isFollowing;
        notifyPropertyChanged(BR.isFollowing);
        if (this.handler != null) {
            handler.onClickFollowButton(this);
            if (isFollowing) {
                followers++;
            } else {
                followers--;
            }
            notifyPropertyChanged(BR.followers);
            ClubManager.clubSubject.onNext(this);
        }
    }

    public String getFollowers() {
        String followersCount;
        DecimalFormat decimal = new DecimalFormat();
        decimal.setDecimalSeparatorAlwaysShown(false);
        decimal.setMaximumFractionDigits(1);
        DecimalFormatSymbols thousandSymbol = new DecimalFormatSymbols();
        thousandSymbol.setGroupingSeparator('.');
        decimal.setDecimalFormatSymbols(thousandSymbol);

        if (followers == 1) {
            followersCount = followers.toString() + activity.getString(R.string.follower_count_1);
            return followersCount;
        }
        if (followers < 1000) {
            followersCount = followers.toString() + activity.getString(R.string.follower_count_2);
            return followersCount;
        } else if (followers < 1000000) {
            Float fk = Float.valueOf(followers) / 1000;
            return decimal.format(fk) + "k" + activity.getString(R.string.follower_count_2);
//            return decimal.format(fk).toString() + "k";
        } else {
            Float fM = Float.valueOf(followers) / 1000000;
            return decimal.format(fM) + "M" + activity.getString(R.string.follower_count_2);
        }
    }

    public String getClubDistance() {
        if (distance != null) {
            if (distance < 1000) {
                return String.format("%dm", distance.intValue());
            } else {
                Float dkm = distance / 1000;
                if (distance / 100 < 100) {
                    return String.format("%dkm", dkm.intValue());
                } else {
                    return String.format("%.1fkm", dkm);
                }
            }
        } else {
            return "";
        }
    }

}
