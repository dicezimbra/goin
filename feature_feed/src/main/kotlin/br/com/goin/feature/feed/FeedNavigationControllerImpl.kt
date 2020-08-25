package br.com.goin.feature.feed

import androidx.fragment.app.Fragment
import br.com.goin.component.navigation.FeedNavigationController

class FeedNavigationControllerImpl: FeedNavigationController{

    override fun getFeed(userId: String?): Fragment {
        return FeedFragment.newInstance(userId)
    }
}