package br.com.goin.feature.feed.post

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import com.linkedin.android.spyglass.mentions.Mentionable

class UserSuggestion : Mentionable {

    val name: String
    val id: String
    var avatar = ""
        private set

    constructor(name: String, id: String) {
        this.name = name
        this.id = id
    }

    constructor(name: String, id: String, avatar: String) {
        this.name = name
        this.id = id
        this.avatar = avatar
    }

    @NonNull
    override fun getTextForDisplayMode(mode: Mentionable.MentionDisplayMode): String {
        return name
    }

    override fun getDeleteStyle(): Mentionable.MentionDeleteStyle {
        return Mentionable.MentionDeleteStyle.PARTIAL_NAME_DELETE
    }

    override fun getSuggestibleId(): Int {
        return 0
    }

    override fun getSuggestiblePrimaryText(): String {
        return name
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeString(id)
        dest.writeString(avatar)
    }

    constructor(`in`: Parcel) {
        name = `in`.readString()!!
        id = `in`.readString()!!
        avatar = `in`.readString()!!
    }

    companion object CREATOR : Parcelable.Creator<UserSuggestion> {
        override fun createFromParcel(parcel: Parcel): UserSuggestion {
            return UserSuggestion(parcel)
        }

        override fun newArray(size: Int): Array<UserSuggestion?> {
            return arrayOfNulls(size)
        }
    }
}
