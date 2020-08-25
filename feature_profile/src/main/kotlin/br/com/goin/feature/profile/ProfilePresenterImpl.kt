package br.com.goin.feature.profile

import android.util.Log
import br.com.goin.base.BaseApplication
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.extensions.useCache
import br.com.goin.base.extensions.useMemoryCache
import br.com.goin.component.session.user.UserModel
import br.com.goin.component.session.user.UserSessionInteractor
import br.com.goin.feature.profile.model.ProfileInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class ProfilePresenterImpl(val view: ProfileFragment) : ProfilePresenter {

    private val userSessionInteractor: UserSessionInteractor = UserSessionInteractor.instance
    private val profileInteractor = ProfileInteractor.instance
    private var disposables = CompositeDisposable()

    private var profileId: String? = null
    private var isUserProfile: Boolean = false
    private var profile: UserModel? = null

    override fun onCreate() {
        val userId = userSessionInteractor.fetchUser()?.id ?: ""
        isUserProfile = profileId == null || profileId == userId
        profileId = profileId ?: userId

        if(isUserProfile){
            view.configureToolbarForCurrentUser()
        }else{
            view.configureToolbarForOtherUsers()
        }

        loadProfile()
    }

    override fun logScreenName() {
        if(isUserProfile){
            view.logMyProfileScreenName()
        }else{
            view.logProfileScreenName()
        }
    }

    override fun onReceiveProfileId(profileId: String?) {
        this.profileId = profileId
    }

    override fun onInviteClick() {
        profile?.let{
            when(it.followedByMe){
                true -> {
                    profileInteractor.unfollow(it.id).ioThread().subscribe({
                        Log.d("ProfilePresenter", "unfollow success")
                    }, { t ->
                        Log.e("ProfilePresenter", t.message, t)
                    }).addTo(disposables)
                }
                else ->{
                    profileInteractor.follow(it.id).ioThread().subscribe({
                        Log.d("ProfilePresenter", "follow success")
                    }, { t ->
                        Log.e("ProfilePresenter", t.message, t)
                    }).addTo(disposables)
                }
            }
            it.followedByMe = it.followedByMe?.not()
            view.configureInviteButton(it.followedByMe == true)
        }
    }

    override fun loadProfile() {
        profileId?.let {
            profileInteractor.findById(it)
                    .useMemoryCache("PROFILE$profileId", UserModel::class.java)
                    .ioThread()
                    .doOnSubscribe { view.showLoading() }
                    .doOnTerminate { view.hideLoading() }
                    .subscribe({
                        this.profile = it
                        view.setupClickListeners(it.id)
                        view.configureViewPager(it.id)
                        view.configureProfile(it)
                        view.configureFollowing(it.followingCount)
                        view.configurePostCount(it.postsCount)
                        view.configureScore(it.userScore)
                        view.configureFollowers(it.followersCount)

                        if(!isUserProfile){

                            view.configureInviteButton(it.followedByMe == true)
                        }else{
                            userSessionInteractor.save(it)
                        }
                    }, { t->
                        Log.e("ProfilePresenter", t.message, t)
                    }).addTo(disposables)
        }
    }

    override fun logOnAnalytics(action: String, label: String?) {
        val app = BaseApplication.instance.context!!
    }

    override fun onDestroy() {
        disposables.dispose()
    }
}