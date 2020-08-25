package br.com.goin.feature.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.visible
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.navigation.NavigationController
import br.com.goin.component.session.user.UserModel
import br.com.goin.component.ui.kit.features.error.ErrorViewHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.view_profile_header.*
import kotlinx.android.synthetic.main.view_tab_profile.*

const val UPDATE_CONNECTIONS = 1116

class ProfileFragment : Fragment(), ProfileView {

    private val presenter: ProfilePresenter = ProfilePresenterImpl(this)
    private val navigationController = NavigationController.instance

    companion object {
        private const val PROFILE_ID = "PROFILE_ID"

        fun newInstance(profileId: String? = null): Fragment{
            val bundle = Bundle()
            bundle.putString(PROFILE_ID, profileId)

            val fragment = ProfileFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onReceiveProfileId(arguments?.getString(PROFILE_ID))
        presenter.onCreate()
    }

    override fun onResume() {
        super.onResume()
        presenter.logScreenName()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun configureToolbarForOtherUsers(){
        toolbar.title = getString(R.string.profile)
        toolbar.setOnBackButtonClicked { activity!!.finish() }
    }

    override fun configureToolbarForCurrentUser(){
        toolbar.title = getString(R.string.my_profile)
        when(activity){
            is ProfileActivity -> { toolbar.setOnBackButtonClicked { activity!!.finish() } }
            else -> { toolbar.hideBackButton() }
        }

        toolbar.setRightButton(R.drawable.icon_notificacao){
            presenter.logOnAnalytics(getString(R.string.analytics_profile_notification_action))
            navigationController?.notificationModule()?.goToNotifications(activity!!)
        }

        toolbar.setRightSecondButton(R.drawable.ico_add_amigo){
            presenter.logOnAnalytics(getString(R.string.analytics_profile_find_friends_action))
            navigationController?.findFriendsModule()?.goToFindFriends(activity!!)
        }
    }

    override fun configureInviteButton(followByMe: Boolean){
        invite_button?.apply {
            if (followByMe) {
                background = ContextCompat.getDrawable(activity!!, R.drawable.button_border_transparent_new)
                setTextColor(ContextCompat.getColor(activity!!, R.color.battleship_grey))
                text = context?.getString(R.string.following)

            }else {
                background = ContextCompat.getDrawable(activity!!, R.drawable.button_primary_dark_new)
                setTextColor(ContextCompat.getColor(activity!!, R.color.colorPrimaryDark))
                text = context?.getString(R.string.follow)
            }
        }

        profile_edit_button.gone()
        invite_button.visible()
    }

    override fun setupClickListeners(currentUserId: String) {
        activity?.let { activity ->
            profile_edit_button.setOnClickListener {
                presenter.logOnAnalytics(getString(R.string.analytics_profile_edit_action))
                navigationController?.legacyApp()?.goToEditProfile(activity)
            }
            following_button.setOnClickListener {
                presenter.logOnAnalytics(getString(R.string.analytics_profile_following_action))
                navigationController?.followingsModule()?.goToFollowings(activity, currentUserId, UPDATE_CONNECTIONS)
            }
            followers_button.setOnClickListener {
                presenter.logOnAnalytics(getString(R.string.analytics_profile_followers_action))
                navigationController?.followingsModule()?.goToFollowers(activity, currentUserId, UPDATE_CONNECTIONS)
            }
            invite_button.setOnClickListener {
                presenter.onInviteClick()
            }
        }
    }

    override fun configureProfile(user: UserModel) {
        profile_user_name.text = user.name
        profile_user_score.text = user.getScore(context)
        profile_user_email.text = user.email
        textViewUserBiography.text = user.biography

        Glide.with(activity!!)
                .load(user.profilePicture)
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions().placeholder(R.drawable.background_circle_placeholder))
                .apply(RequestOptions().error(R.drawable.background_circle_placeholder))
                .into(profile_user_image)
    }

    override fun configurePostCount(postCount: Int?){
        countPosts.text = if (postCount == null) {
            context?.resources?.getQuantityString(br.com.goin.component.session.R.plurals.post_counter, 2, 0)
        } else {
            context?.resources?.getQuantityString(br.com.goin.component.session.R.plurals.post_counter, postCount, postCount)
        }
    }

    override fun configureFollowing(followingCount: Int?){
        following_button.text =  if (followingCount == null) {
            context?.resources?.getQuantityString(br.com.goin.component.session.R.plurals.following_counter, 2, 0)
        } else {
            context?.resources?.getQuantityString(br.com.goin.component.session.R.plurals.following_counter, followingCount, followingCount)
        }
    }

    override fun configureFollowers(followersCount: Int?){
        followers_button.text =   if (followersCount == null) {
            context?.resources?.getQuantityString(br.com.goin.component.session.R.plurals.followers_counter, 2, 0)
        } else {
            context?.resources?.getQuantityString(br.com.goin.component.session.R.plurals.followers_counter, followersCount, followersCount)
        }
    }

    override fun configureScore(scoreCount: Int?){
        profile_user_score.text = if (scoreCount == null) {
            context?.resources?.getQuantityString(br.com.goin.component.session.R.plurals.score_number, 2, 0)
        } else {
            context?.resources?.getQuantityString(br.com.goin.component.session.R.plurals.score_number, scoreCount, scoreCount)
        }
    }

    override fun configureViewPager(userId: String) {
        pager.adapter = ProfileAdapter(childFragmentManager, userId)
        pager.currentItem = 0

        customTabPosts.setTextColor(ContextCompat.getColor(activity!!, R.color.grapefruit))
        customTabInterests.setTextColor(ContextCompat.getColor(activity!!, R.color.battleship_grey))

        customTabPosts.setOnClickListener {
            pager.currentItem = 0
        }

        customTabInterests.setOnClickListener {
            pager.currentItem = 1
        }

        pager.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        customTabPosts.setTextColor(ContextCompat.getColor(activity!!, R.color.grapefruit))
                        customTabInterests.setTextColor(ContextCompat.getColor(activity!!, R.color.battleship_grey))
                    }
                    1 -> {
                        customTabPosts.setTextColor(ContextCompat.getColor(activity!!, R.color.battleship_grey))
                        customTabInterests.setTextColor(ContextCompat.getColor(activity!!, R.color.grapefruit))
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    override fun handleError(throwable: Throwable){
        ErrorViewHelper.handleErrorView(coordinator_layout, throwable){
            presenter.onCreate()
        }
    }

    override fun logMyProfileScreenName(){
        Analytics.instance.screenView(activity!!, getString(R.string.my_profile_screen_name))
    }

    override fun logProfileScreenName(){
        Analytics.instance.screenView(activity!!, getString(R.string.profile_screen_name))
    }

    override fun showLoading(){
        customTabProfile.gone()
        include2.gone()
        pager.gone()
        progress.visible()
    }

    override fun hideLoading(){
        customTabProfile.visible()
        include2.visible()
        pager.visible()
        progress.gone()
    }
}
