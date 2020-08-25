package br.com.goin.component.navigation

import android.app.Activity
import android.content.Context
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import br.com.goin.base.model.PurchaseModel

interface LegacyNavigationController {
    fun goToForgotPassword(context: Context)
    fun goToChooseProfilePicture(context: Context)
    fun goToHome(context: Context)
    fun goToPost(activity: Activity, postType: String? = null, eventName: String? = null, eventId: String? = null, clubId: String? = null, clubName: String? = null)
    fun goToPostDetail(activity: Activity, postId: String)
    fun goToComment(activity: Activity, postId: String, requestCode: Int)
    fun goToComment(fragment: androidx.fragment.app.Fragment, postId: String, requestCode: Int)
    fun goToPostLikersFromNotification(activity: Activity, destinationId: String?): Any
    fun goToComments(activity: Activity, destinationId: String?)
    fun goToSinglePost(activity: Activity, destinationId: String?)
    fun goToPlayerActivity(activity: Activity, urlVideo: String?)
    fun redirectToPurchaseDetails(activity: Activity, destinationId: String?)
    fun goToVipBoxPayment(activity: Activity, destinationId: String?)
    fun getConfig(): Fragment
    fun getWallet(): Fragment
    fun goToPlaceholder(activity: AppCompatActivity, isWallet: Boolean)
    fun goToEventTickets(context: Context, eventId: String, originType: String, ingresseToken: String?)
    fun goToPostLikes(context: Context?, id: String)

    fun goToMovieActivity(context: Context, categoriyId: String, categoryName: String)
    fun goToTheatersActivity(context: Context, categoriyId: String, categoryName: String)
    fun goToTheatersDetailActivity(context: Context, clubId: String?, categoryId: String, categoryName: String)
    fun goToMovieAboutActivity(context: Context, sessionId: String, movieTitle: String,
                               movieDetailCategory: String, movieDetailParentalControl: String,
                               movieDetailDuration: String, movieAbout: String,
                               urlImage: String)

    fun goToTransferTicket(activity: Context?, purchaseModel: PurchaseModel?, info: PurchaseModel.TicketsInfo, avatarUrl: String?)
    fun goToEditProfile(activity: Activity)
    fun goToInviteFriends(context: Context, eventId: String)

}