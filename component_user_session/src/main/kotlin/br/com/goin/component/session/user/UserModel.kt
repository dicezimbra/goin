package br.com.goin.component.session.user

import android.content.Context
import br.com.goin.component.session.R
import com.google.gson.annotations.SerializedName
import java.io.Serializable

enum class ActionType {
    Invite, Follow, Conversation, StartConversation, StartGroupConversation, TransferTicket
}

enum class FriendStatus {
    Invited, NotInvited
}

open class UserModel(@SerializedName("email") var email: String = "",
                     @SerializedName("name")var name: String = "",
                     @SerializedName("password")var password: String = "",
                     @SerializedName("invitedBy")var invitedBy: Boolean = false,
                     @SerializedName("id")var id: String = "",
                     @SerializedName("profilePicture")var  profilePicture: String = "",
                     @SerializedName("accountProvider")var accountProvider: String? = null,
                     @SerializedName("actionType")var actionType: ActionType? = null,
                     @SerializedName("followedByMe")var followedByMe: Boolean? = false,
                     @SerializedName("clubsCount") var clubsNumber: Int? = null,
                     @SerializedName("eventsCount") var eventsNumber: Int? = null,
                     @SerializedName("categoriesCount") var categoriesNumber: Int? = null,
                     @SerializedName("followingCount")var followingCount: Int? = null,
                     @SerializedName("followersCount")var followersCount: Int? = null,
                     @SerializedName("userScore")var userScore: Int? = null,
                     @SerializedName("ticketCount")var ticketCount: Int? = null,
                     @SerializedName("pushEndpoint")var pushEndpoint: String? = null,
                     @SerializedName("unreadMessages")var unreadMessages: Int? = null,
                     @SerializedName("chatId")var chatId: String? = null,
                     @SerializedName("halfEntranceId")var halfEntranceId: String? = null,
                     @SerializedName("rg")var rg: String? = null,
                     @SerializedName("postsCount")
                     var postsCount: Int? = null,
                     @SerializedName("bio")
                     var biography: String? = null,
                     @SerializedName("categories")var categories: List<String>? = null,
                     @SerializedName("registerUser")var registerUser: UserModel? = null,
                     @SerializedName("updateUser")var updateUser: UserModel? = null,
                     @SerializedName("myUser")var myUser: UserModel? = null): Serializable {


    companion object {
        const val SIGNATURE = "user_signature"
    }

    var friendStatus: FriendStatus? = null

    fun getScore(context: Context?): String {
        var score = "0"
        if (context != null) {
            if (userScore == null) {
                score = context.resources.getQuantityString(R.plurals.score_number, 2, 0)
            } else {
                score = context.resources.getQuantityString(R.plurals.score_number, userScore
                        ?: 0, userScore)
            }
        }
        return score
    }

    fun getFollowers(context: Context?): String {
        var followers = "0"
        if (context != null) {
            if (followersCount == null) {
                followers = context.resources.getQuantityString(R.plurals.followers_counter, 2, 0)
            } else {
                followers = context.resources.getQuantityString(R.plurals.followers_counter, followersCount
                        ?: 0, followersCount)
            }
        }
        return followers
    }

    fun getFollowings(context: Context?): String {
        var following = "0"
        if (context != null) {
            if (followingCount == null) {
                following = context.resources.getQuantityString(R.plurals.following_counter, 2, 0)
            } else {
                following = context.resources.getQuantityString(R.plurals.following_counter, followingCount
                        ?: 0, followingCount)
            }
        }
        return following
    }

    open fun getTextButton(context: Context): String {

        return if (actionType == ActionType.Follow) {
            if (followedByMe != true) {
                context.getString(R.string.follow)
            } else {
                context.getString(R.string.following)
            }
        } else {
            ""
        }
    }

    open fun setBackgroundPrimaryColor(): Boolean {
        return if (actionType == ActionType.Follow) {
            this.followedByMe!!
        } else {
            false
        }
    }

    open fun setTextPrimaryColor(): Boolean {
        return if (actionType == ActionType.Follow) {
            followedByMe != true
        } else {
            true
        }
    }


    fun clean() {
        this.id = ""
        this.registerUser = null
        this.updateUser = null
        this.myUser = null
        this.password = ""
        this.followedByMe = null
        this.followersCount = null
        this.followingCount = null
        this.categoriesNumber = null
        this.eventsNumber = null
        this.clubsNumber = null
    }

    fun convert(): UpdateUserModel{
        return UpdateUserModel(email = this.email, name = this.name, profilePicture = this.profilePicture, accountProvider = this.accountProvider, pushEndpoint = this.pushEndpoint,
                categories = this.categories, cpf = "")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserModel

        if (email != other.email) return false
        if (name != other.name) return false
        if (id != other.id) return false
        if (profilePicture != other.profilePicture) return false
        if (accountProvider != other.accountProvider) return false
        if (actionType != other.actionType) return false
        if (categoriesNumber != other.categoriesNumber) return false
        if (followingCount != other.followingCount) return false
        if (followersCount != other.followersCount) return false
        if (userScore != other.userScore) return false
        if (ticketCount != other.ticketCount) return false
        if (unreadMessages != other.unreadMessages) return false
        if (chatId != other.chatId) return false
        if (halfEntranceId != other.halfEntranceId) return false
        if (rg != other.rg) return false
        if (postsCount != other.postsCount) return false
        if (biography != other.biography) return false
        if (categories != other.categories) return false
        if (registerUser != other.registerUser) return false
        if (updateUser != other.updateUser) return false
        if (myUser != other.myUser) return false
        if (friendStatus != other.friendStatus) return false

        return true
    }

    override fun hashCode(): Int {
        var result = email.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + profilePicture.hashCode()
        result = 31 * result + (accountProvider?.hashCode() ?: 0)
        result = 31 * result + (actionType?.hashCode() ?: 0)
        result = 31 * result + (categoriesNumber ?: 0)
        result = 31 * result + (followingCount ?: 0)
        result = 31 * result + (followersCount ?: 0)
        result = 31 * result + (userScore ?: 0)
        result = 31 * result + (ticketCount ?: 0)
        result = 31 * result + (unreadMessages ?: 0)
        result = 31 * result + (chatId?.hashCode() ?: 0)
        result = 31 * result + (halfEntranceId?.hashCode() ?: 0)
        result = 31 * result + (rg?.hashCode() ?: 0)
        result = 31 * result + (postsCount ?: 0)
        result = 31 * result + (biography?.hashCode() ?: 0)
        result = 31 * result + (categories?.hashCode() ?: 0)
        result = 31 * result + (registerUser?.hashCode() ?: 0)
        result = 31 * result + (updateUser?.hashCode() ?: 0)
        result = 31 * result + (myUser?.hashCode() ?: 0)
        result = 31 * result + (friendStatus?.hashCode() ?: 0)
        return result
    }
}
