package br.com.goin.component_deep_link

import android.app.Activity
import android.content.Context
import br.com.goin.component.navigation.DeepLinkNavigationController
import br.com.goin.component.navigation.NavigationController
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri
import br.com.goin.component.session.user.UserSessionInteractor
import br.com.goin.component_deep_link.branchIo.BranchIOHelper


class DeepLinkControllerImpl : DeepLinkNavigationController {

    val navigationController = NavigationController.instance

    override fun goToBannerAction(context: Activity, actionValue: String, bannerType: String) {
        when (bannerType) {
            LinkType.CINEMA_DETAIL.toString() -> goToCinemaDetail(context, actionValue)
            LinkType.CLUB_DETAIL.toString() -> goToClubDetail(context, actionValue)
            LinkType.EVENT_DETAIL.toString() -> goToEventDetail(context, actionValue)
            LinkType.MOVIE_DETAIL.toString() -> goToMovieDetail(context, actionValue)
            LinkType.EXTERNAL_URL.toString() -> externalUrl(context, actionValue)
            LinkType.INVITE_FRIENDS_BRANCHIO.toString() -> inviteFriends(context)
            LinkType.INVITE_FRIENDS.toString() -> inviteFriends(context)
            LinkType.INVITE_FRIEND.toString() -> inviteFriends(context)
        }
    }

    private fun externalUrl(context: Context, url: String?) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }

    private fun inviteFriends(context: Activity) {
        navigationController?.loginModule()?.goToLogin(context){
            val userSession = UserSessionInteractor.instance.fetchUser()
            BranchIOHelper.onClickInvitefriend(context, userSession?.id  ?: "", userSession?.name ?: "")
        }
    }

    private fun goToMovieDetail(context: Context, id: String) {
    }

    private fun goToEventDetail(context: Context, eventId: String, clubId: String? = null) {
        navigationController?.eventModule()?.goToEvent(context, eventId, clubId)
    }

    private fun goToClubDetail(context: Context, id: String) {
    }

    private fun goToCinemaDetail(context: Context, id: String) {
    }
}