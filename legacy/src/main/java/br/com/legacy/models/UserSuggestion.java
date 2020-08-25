package br.com.legacy.models;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.linkedin.android.spyglass.mentions.Mentionable;

public class UserSuggestion implements Mentionable
{

    private final String name;
    private final String id;
    private String avatar = "";

    public UserSuggestion (String name, String id){
        this.name = name;
        this.id = id;
    }

    public UserSuggestion(String name, String id, String avatar) {
        this.name = name;
        this.id = id;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getAvatar() {
        return avatar;
    }

    @NonNull
    @Override
    public String getTextForDisplayMode(MentionDisplayMode mode) {
        return name;
    }

    @Override
    public MentionDeleteStyle getDeleteStyle() {
        return MentionDeleteStyle.PARTIAL_NAME_DELETE;
    }

    @Override
    public int getSuggestibleId() {
        return 0;
    }

    @Override
    public String getSuggestiblePrimaryText() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(avatar);
    }

    public UserSuggestion(Parcel in){
        name = in.readString();
        id = in.readString();
        avatar = in.readString();
    }

    public static final Parcelable.Creator<UserSuggestion> CREATOR
            = new Parcelable.Creator<UserSuggestion>() {
        public UserSuggestion createFromParcel(Parcel in) {
            return new UserSuggestion(in);
        }

        public UserSuggestion[] newArray(int size) {
            return new UserSuggestion[size];
        }
    };
}
