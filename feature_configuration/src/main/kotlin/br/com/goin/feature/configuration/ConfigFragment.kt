package br.com.goin.feature.configuration

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.goin.base.helpers.PreferenceHelper
import br.com.goin.base.utils.Constants
import br.com.goin.base.utils.VersionHelper
import br.com.goin.component.analytics.analytics.Analytics
import br.com.goin.component.navigation.NavigationController
import br.com.goin.component.session.user.UserModel
import br.com.goin.component_deep_link.branchIo.BranchIOHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.example.feature_configuration.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_config.*

private val IDENTITY_PROVIDER = "graph.facebook.com"

class ConfigFragment : androidx.fragment.app.Fragment(), ConfigView {

    private val presenter: ConfigPresenter = ConfigPresenterImpl(this)
    private val navigationController = NavigationController.instance

    private var updateUser: UserModel? = null

    companion object {
        fun newInstance() = ConfigFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_config, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onCreate()
        presenter.checkFromFacebook(IDENTITY_PROVIDER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        updateUserModel(data)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
        Analytics.instance.screenView(activity!!, getString(R.string.confi_screen_name))
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun configureOnCLickListener() {
        field_term_of_use.setOnClickListener {
            navigationController?.termsModule()?.goToTermOfUse(context!!)
        }

        field_half_price.setOnClickListener {
            navigationController?.termsModule()?.goToHalfPricePolicy(context!!)
        }

        field_privacy_policy.setOnClickListener {
            navigationController?.termsModule()?.goToPrivacyPolicy(context!!)
        }

        field_edit_profile.setOnClickListener {
            navigationController?.legacyApp()?.goToEditProfile(activity!!)
        }

        field_change_password.setOnClickListener {
            navigationController?.changePasswordModule()?.goToChangePassword(activity!!)
        }

        fieldInviteFriends.setOnClickListener {
            Analytics.instance.logClickEvent(getString(R.string.invite_friends_category))
            presenter.onClickInviteFriend()
        }

        linearLayoutLogout.setOnClickListener {
            ConfigHelper.showDialogLogout(context!!) {
                Analytics.instance.logClickEvent(getString(R.string.logout_category))
                logout()
            }
        }

        buttonGoToStart.setOnClickListener {
            navigationController?.loginModule()?.goToLogin(context!!) {
                navigationController.homeModule().goToHome(context!!)
            }
        }
    }

    override fun onClickInvitefriend(userModel: UserModel) {
        BranchIOHelper.onClickInvitefriend(activity!!, userId = userModel.id,userName = userModel.name)
    }

    private fun logout() {
        presenter.clearPushNotification()
        PreferenceHelper.clear()
        configureLoggedOff()
    }

    override fun configureLoggedIn() {
        logged_group.visibility = View.VISIBLE
        logged_off_group.visibility = View.GONE
    }

    override fun configureLoggedOff() {
        hideChangePassword()
        logged_group.visibility = View.GONE
        logged_off_group.visibility = View.VISIBLE
    }

    override fun configureProfile(userModel: UserModel) {
        Glide.with(activity!!)
                .load(userModel.profilePicture)
                .transition(withCrossFade())
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.background_circle_placeholder))
                .into(image_view_user_config)

        textViewProfileName.text = userModel.name
        version_code.text = VersionHelper.versionName()
    }

    private fun updateUserModel(data: Intent?) {
        updateUser = Gson().fromJson(data?.getStringExtra(Constants.UPDATED_USER), UserModel::class.java)
        updateUser?.let {
            presenter.updateUser(it)
        }
    }

    override fun hideChangePassword() {
        field_change_password.visibility = View.GONE
    }

}