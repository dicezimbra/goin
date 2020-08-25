package br.com.goin.feature.profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.goin.component.navigation.NavigationController
import br.com.goin.feature.profile.myinterests.MyInterestsFragment

class ProfileAdapter(fm: FragmentManager, val userId: String) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> NavigationController.instance!!.feedModule().getFeed(userId)
            else -> MyInterestsFragment.newInstance(userId)
        }
    }

    override fun getCount(): Int{
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return ""
    }
}
