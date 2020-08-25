package br.com.legacy.models;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;
import br.com.R;

/**
 * Created by appsimples on 27/10/17.
 */

public class FeelingModel {

    @SerializedName("emoji")
    public Drawable emoji;

    @SerializedName("name")
    public String name;

    public void setDisplayName(String feeling, Activity activity) {
        switch (feeling) {
            case "glad":
                this.displayName = activity.getResources().getString(R.string.emoji_glad).toLowerCase() + " \uD83D\uDE42";
                break;
            case "excited":
                this.displayName = activity.getResources().getString(R.string.emoji_excited).toLowerCase() + " \uD83E\uDD17";
                break;
            case "happy":
                this.displayName = activity.getResources().getString(R.string.emoji_happy).toLowerCase() + " \uD83D\uDE01";
                break;
            case "cool":
                this.displayName = activity.getResources().getString(R.string.emoji_cool).toLowerCase() + " \uD83D\uDE0F";
                break;
            case "anxious":
                this.displayName = activity.getResources().getString(R.string.emoji_anxious).toLowerCase() + " \uD83D\uDE2B";
                break;
            case "joyful":
                this.displayName = activity.getResources().getString(R.string.emoji_joyful).toLowerCase() + " \uD83D\uDE1C";
                break;
            case "seductive":
                this.displayName = activity.getResources().getString(R.string.emoji_seductive).toLowerCase() + " \uD83D\uDE09";
                break;
            case "angry":
                this.displayName = activity.getResources().getString(R.string.emoji_angry).toLowerCase() + " \uD83D\uDE21";
                break;
            case "sick":
                this.displayName = activity.getResources().getString(R.string.emoji_sick).toLowerCase() + " \uD83E\uDD22";
                break;
            default:
                this.displayName = "unkown feeling";
                break;
        }
    }
    @SerializedName("displayName")
    public String displayName;

    public FeelingModel() {}

    public enum FeelingTypes {
        glad,
        excited,
        happy,
        cool,
        anxious,
        joyful,
        seductive,
        angry,
        sick
    }
}
