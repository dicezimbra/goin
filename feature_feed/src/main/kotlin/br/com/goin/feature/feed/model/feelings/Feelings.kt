package br.com.goin.feature.feed.model.feelings

import android.app.Activity
import android.graphics.drawable.Drawable
import br.com.goin.feature.feed.R
import com.google.gson.annotations.SerializedName

class Feelings {

    @SerializedName("emoji")
    var emoji: Drawable? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("displayName")
    var displayName: String? = null


    fun setDisplayName(feeling: String, activity: Activity) {
        when (feeling) {
            "glad" -> this.displayName = activity.resources.getString(R.string.emoji_glad).toLowerCase() + " \uD83D\uDE42"
            "excited" -> this.displayName = activity.resources.getString(R.string.emoji_excited).toLowerCase() + " \uD83E\uDD17"
            "happy" -> this.displayName = activity.resources.getString(R.string.emoji_happy).toLowerCase() + " \uD83D\uDE01"
            "cool" -> this.displayName = activity.resources.getString(R.string.emoji_cool).toLowerCase() + " \uD83D\uDE0F"
            "anxious" -> this.displayName = activity.resources.getString(R.string.emoji_anxious).toLowerCase() + " \uD83D\uDE2B"
            "joyful" -> this.displayName = activity.resources.getString(R.string.emoji_joyful).toLowerCase() + " \uD83D\uDE1C"
            "seductive" -> this.displayName = activity.resources.getString(R.string.emoji_seductive).toLowerCase() + " \uD83D\uDE09"
            "angry" -> this.displayName = activity.resources.getString(R.string.emoji_angry).toLowerCase() + " \uD83D\uDE21"
            "sick" -> this.displayName = activity.resources.getString(R.string.emoji_sick).toLowerCase() + " \uD83E\uDD22"
            else -> this.displayName = "unkown feeling"

        }
    }

    enum class FeelingTypes {
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