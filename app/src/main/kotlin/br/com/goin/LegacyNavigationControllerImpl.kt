package br.com.goin

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.goin.base.model.PurchaseModel
import br.com.goin.component.navigation.LegacyNavigationController
import br.com.goin.eventticket.TicketsActivity
import br.com.goin.feature.configuration.ConfigFragment
import br.com.goin.feature.feed.commentary.CommentActivity
import br.com.goin.feature.feed.post.PostActivity
import br.com.goin.feature.home.HomeActivity
import br.com.goin.feature.profile.edit.EditProfileActivity
import br.com.goin.feature.theaters.plays.movies.movies.MoviesActivity
import br.com.goin.feature.theaters.plays.movies.playsMoviesDetail.about.MovieAboutActivity
import br.com.goin.feature.theaters.plays.movies.theatherPlays.TheaterPlaysActivity
import br.com.goin.friends.InviteFriendsActivity

import br.com.goin.feature_theater.TheaterDetailActivity
import br.com.legacy.features.placeholderisloged.PlaceholderActivity
import br.com.goin.features.wallet.WalletFragment
import br.com.legacy.navigation.Coordinator
import br.com.legacy.utils.Constants
import br.com.legacy.viewControllers.activitites.*
import com.google.gson.Gson

class LegacyNavigationControllerImpl : LegacyNavigationController {

    override fun goToMovieAboutActivity(context: Context, sessionId: String, movieTitle: String,
                                        movieDetailCategory: String, movieDetailParentalControl: String,
                                        movieDetailDuration: String, movieAbout: String, urlImage: String) {

        MovieAboutActivity.starter(context, movieTitle, movieDetailCategory, movieDetailParentalControl,
                movieDetailDuration, movieAbout, urlImage)
    }

    override fun goToTheatersDetailActivity(context: Context, clubId: String?, categoryId: String, categoryName: String) {
        TheaterDetailActivity.starter(context, clubId, categoryId, categoryName)
    }

    override fun goToMovieActivity(context: Context, categoriyId: String, categoryName: String) {
        MoviesActivity.starter(context, categoriyId, categoryName)
    }

    override fun goToTheatersActivity(context: Context, categoriyId: String, categoryName: String) {
        TheaterPlaysActivity.starter(context, categoriyId, categoryName)
    }

    override fun goToForgotPassword(context: Context) {
        Coordinator.goToForgotPassword(context, true)
    }

    override fun goToChooseProfilePicture(context: Context) {
        Coordinator.goToChooseProfilePicture(context)
    }

    override fun goToHome(context: Context) {
        HomeActivity.starter(context, "")
    }

    override fun goToPost(activity: Activity, postType: String?, eventName: String?, eventId: String?,
                          clubId: String?, clubName: String?) {
        PostActivity.starter(activity, true, postType, eventName, eventId, clubId= clubId, clubName = clubName)
    }

    override fun goToComment(activity: Activity, postId: String, requestCode: Int) {
        val intent = Intent(activity, CommentActivity::class.java)
        intent.putExtra(Constants.POST_ID, postId)
        activity.startActivityForResult(intent, requestCode)
    }

    override fun goToVipBoxPayment(activity: Activity, destinationId: String?) {
        Coordinator.goToVipBoxPayment(activity, destinationId)
    }

    override fun goToPlayerActivity(activity: Activity, urlVideo: String?) {
        val intent = Intent(activity, PlayerActivity::class.java)
        intent.putExtra(Constants.URL, urlVideo)
        activity.startActivity(intent)
    }

    override fun goToComments(activity: Activity, destinationId: String?) {
        CommentActivity.starter(activity, "", null)
    }

    override fun goToSinglePost(activity: Activity, destinationId: String?) {
        Coordinator.goToSinglePost(activity, destinationId)
    }

    override fun redirectToPurchaseDetails(activity: Activity, destinationId: String?) {
        Coordinator.redirectToPurchaseDetails(activity, destinationId)
    }

    override fun goToComment(fragment: Fragment, postId: String, requestCode: Int) {
        val intent = Intent(fragment.context, CommentActivity::class.java)
        intent.putExtra(Constants.POST_ID, postId)
        fragment.startActivityForResult(intent, requestCode)
    }

    override fun goToPostDetail(activity: Activity, postId: String) {
        Coordinator.goToSinglePost(activity, postId)
    }


    override fun goToPostLikersFromNotification(activity: Activity, destinationId: String?) {
        Coordinator.goToPostLikersFromNotification(activity, destinationId)
    }

    override fun getConfig(): Fragment {
        return ConfigFragment.newInstance()
    }

    override fun getWallet(): Fragment {
        return WalletFragment.newInstance()
    }

    override fun goToPlaceholder(activity: AppCompatActivity, isWallet: Boolean) {
        if (isWallet) {
            PlaceholderActivity.starter(activity)
        } else {
            PlaceholderActivity.starterForWallet(activity)
        }
    }

    override fun goToEventTickets(context: Context, eventId: String, originType: String, ingresseToken: String? ) {
       TicketsActivity.starter(context, eventId=eventId, originType = originType, ingresseToken = ingresseToken)
    }

    override fun goToInviteFriends(context: Context, eventId: String) {
        InviteFriendsActivity.starter(context, eventId)
    }

    override fun goToPostLikes(context: Context?, id: String) {
        val intent = Intent(context, PostLikersActivity::class.java)
        intent.putExtra(Constants.POST_ID, id)
        context?.startActivity(intent)
    }


    override fun goToTransferTicket(activity: Context?, purchaseModel: PurchaseModel?, info: PurchaseModel.TicketsInfo, avatarUrl: String?) {
        val intent = Intent(activity, PurchaseDetailActivity::class.java)
        intent.putExtra(Constants.TYPE, PurchaseDetailActivity.PurchaseDetailType.TransferTicket)
        if (purchaseModel?.ticketsInfo?.size == 2) {
            purchaseModel.ticketsInfo?.removeAt(1)
        }
        purchaseModel?.ticketsInfo?.add(info)
        intent.putExtra(Constants.URL, avatarUrl)
        intent.putExtra(Constants.PURCHASE_MODEL, Gson().toJson(purchaseModel))
        activity?.startActivity(intent)
    }

    override fun goToEditProfile(activity: Activity) {
        EditProfileActivity.starter(activity)
    }
}