package br.com.goin.component.navigation

interface NavigationController{

    companion object {
        var instance : NavigationController? = null
    }

    fun loginModule(): LoginNavigationController
    fun termsModule(): TermsNavigationController
    fun legacyApp(): LegacyNavigationController
    fun feedModule(): FeedNavigationController
    fun homeModule(): HomeNavigationController
    fun configurationModule(): ConfigurationNavigationController
    fun notificationModule(): NotificationsNavigationController
    fun eventModule(): EventNavigationController
    fun allCategoriesModule(): AllCategoriesNavigationControler
    fun searchModule(): SearchNavigationController
    fun searchLocationModule(): SearchLocationNavigationController
    fun theatherPlayMovieModule(): TheatherPlayMoviesController
    fun placeModule(): PlaceNavigationController
    fun fullImageModule(): FullImageNavigationController
    fun welcomeModule(): WelcomeNavigationController
    fun searchCategoryModule(): SearchCategoryNavigationController
    fun deepLinkNavigationModule(): DeepLinkNavigationController
    fun followingsModule(): FollowingNavigationController
    fun changePasswordModule(): ChangePasswordNavigationController
    fun profileModule(): ProfileNavigationController
    fun findFriendsModule(): FindFriendsNavigationController
    fun walletModule(): WalletNavigationController
    fun paymentModule(): PaymentNavigationController
}

