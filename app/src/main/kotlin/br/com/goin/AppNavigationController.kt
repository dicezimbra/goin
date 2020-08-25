package br.com.goin

import br.com.features.welcome.WelcomeNavigationControllerImpl
import br.com.goin.component.navigation.*
import br.com.goin.component.terms.TermsNavigationControllerImpl
import br.com.goin.component_deep_link.DeepLinkControllerImpl
import br.com.goin.feature.change.password.ChangePasswordNavigationControllerImpl
import br.com.goin.feature.configuration.ConfigurationNavigationControllerImpl
import br.com.goin.feature.event.EventNavigationControllerImpl
import br.com.goin.feature.feed.FeedNavigationControllerImpl
import br.com.goin.feature.full.image.FullImageNavigationControllerImpl
import br.com.goin.feature.home.HomeNavigationControllerImpl
import br.com.goin.feature.notifications.NotificationsNavigationControllerImpl
import br.com.goin.feature.search.category.SearchCategoryNavigationControllerImpl
import br.com.goin.feature.place.PlaceNavigationControllerImpl
import br.com.goin.component.navigation.ProfileNavigationController
import br.com.goin.component.PaymentNavigationControllerImpl
import br.com.goin.feature.profile.ProfileNavigationControllerImpl
import br.com.goin.feature.search.event.SearchNavigationControllerImpl
import br.com.goin.feature.search.location.searchLocation.SearchLocationNavigationControllerImpl
import br.com.goin.feature.theaters.plays.movies.TheatherPlayMoviesControllerImpl
import br.com.goin.feature_all_categories.AllCategoriesNavigationControlerImpl
import br.com.goin.findfriends.FindFriendsNavigationControllerImpl
import br.com.goin.feature_social.FollowNavigationControllerImpl
import br.com.goin.features.WalletNavigationControllerImpl
import br.goin.features.login.login.LoginNavigationControllerImpl

class AppNavigationController : NavigationController {


    companion object {
        fun init() {
            NavigationController.instance = AppNavigationController()
        }
    }

    override fun configurationModule(): ConfigurationNavigationController {
        return ConfigurationNavigationControllerImpl()
    }

    override fun changePasswordModule(): ChangePasswordNavigationController {
        return ChangePasswordNavigationControllerImpl()
    }

    override fun allCategoriesModule(): AllCategoriesNavigationControler {
        return AllCategoriesNavigationControlerImpl()
    }

    override fun termsModule(): TermsNavigationController {
        return TermsNavigationControllerImpl()
    }

    override fun loginModule(): LoginNavigationController {
        return LoginNavigationControllerImpl()
    }

    override fun notificationModule(): NotificationsNavigationController {
        return NotificationsNavigationControllerImpl()
    }

    override fun legacyApp(): LegacyNavigationController {
        return LegacyNavigationControllerImpl()
    }

    override fun feedModule(): FeedNavigationController {
        return FeedNavigationControllerImpl()
    }

    override fun homeModule(): HomeNavigationController {
        return HomeNavigationControllerImpl()
    }

    override fun searchModule(): SearchNavigationController {
        return SearchNavigationControllerImpl()
    }

    override fun eventModule(): EventNavigationController {
        return EventNavigationControllerImpl()
    }

    override fun searchLocationModule(): SearchLocationNavigationController {
        return SearchLocationNavigationControllerImpl()
    }

    override fun theatherPlayMovieModule(): TheatherPlayMoviesController {
        return TheatherPlayMoviesControllerImpl()
    }

    override fun searchCategoryModule(): SearchCategoryNavigationController {
        return SearchCategoryNavigationControllerImpl()
    }

    override fun placeModule(): PlaceNavigationController {
        return PlaceNavigationControllerImpl()
    }

    override fun fullImageModule(): FullImageNavigationController {
        return FullImageNavigationControllerImpl()
    }

    override fun deepLinkNavigationModule(): DeepLinkNavigationController {
        return DeepLinkControllerImpl()
    }

    override fun followingsModule(): FollowingNavigationController {
        return FollowNavigationControllerImpl()
    }

    override fun welcomeModule(): WelcomeNavigationController {
        return WelcomeNavigationControllerImpl()
    }

    override fun profileModule(): ProfileNavigationController {
        return ProfileNavigationControllerImpl()
    }

    override fun findFriendsModule(): FindFriendsNavigationController {
        return FindFriendsNavigationControllerImpl()
    }

    override fun walletModule(): WalletNavigationController {
        return WalletNavigationControllerImpl()
    }

    override fun paymentModule(): PaymentNavigationController {
       return PaymentNavigationControllerImpl()
    }
}