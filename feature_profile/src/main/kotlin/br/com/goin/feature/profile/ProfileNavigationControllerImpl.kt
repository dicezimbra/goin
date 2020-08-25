package br.com.goin.feature.profile

import android.content.Context
import android.app.Activity
import androidx.fragment.app.Fragment
import br.com.goin.component.navigation.ProfileNavigationController
import br.com.goin.feature.profile.edit.EditProfileActivity

class ProfileNavigationControllerImpl : ProfileNavigationController {

    override fun getProfile(): Fragment {
        return ProfileFragment.newInstance()
    }

    override fun goToProfile(context: Context, profileId: String?){
        ProfileActivity.starter(context, profileId)
    }

    override fun goToEditProfile(activity: Activity) {
        EditProfileActivity.starter(activity)
    }

}