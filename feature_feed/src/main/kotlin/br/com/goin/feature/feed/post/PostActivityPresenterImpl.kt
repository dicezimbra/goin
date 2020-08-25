package br.com.goin.feature.feed.post

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.component.session.user.UserModel
import br.com.goin.feature.feed.model.PostInteractor
import br.com.goin.feature.feed.model.checkIn.CheckIn
import br.com.goin.feature.feed.model.friends.FriendsInteractor
import br.com.goin.feature.feed.model.friends.PostUser
import br.com.goin.feature.feed.model.friends.SearchFriendsResponse
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class PostActivityPresenterImpl(val view: PostActivityView) : PostActivityPresenter {

    private var feelings: String? = ""
    private var eventCheckedIn: CheckIn? = null
    private var disposables = CompositeDisposable()
    private var interactorFriends: FriendsInteractor = FriendsInteractor.instance
    private val friends: MutableList<PostUser> = ArrayList()

    override fun onCreate() {
        view.configHideKeyboard()
        view.configureOnClickListeners()
        view.setupTransaction()
        view.setupBroadcastReceiver()
        view.configurePostQuery()
        view.setupIntents()
        view.registerReceiver()
    }

    override fun onReceiveEvent(event: Any?) {
        (event as? CheckIn)?.let {
            eventCheckedIn = it
            view.setFeelings(null, eventCheckedIn)
        }
    }

    override fun onReceiveUser(user: UserModel?) {
        user?.let {
            view.loadUserPicture(it)
            view.loadUserName(it)
        }
    }

    override fun onReceiveFeelings(feeling: String?) {
        feeling?.let {
            feelings = it
            view.setFeelings(feelings, eventCheckedIn)
        }
    }

    override fun onReceiveEventCheckedIn(event: CheckIn?) {
        event?.let {
            eventCheckedIn = it
            view.setFeelings(null, eventCheckedIn)
        }
    }

    override fun searchFriends(queryFriends: String) {
        interactorFriends.searchFriends(queryFriends)
                .ioThread()
                .subscribe({
                    it?.let {
                        onReceiveFriends(it.data)
                    }
                }, { throwable ->
                    Log.e("PostActivityPresenter", throwable.message, throwable)
                }).addTo(disposables)
    }

    override fun onReceiveFriends(data: SearchFriendsResponse?) {
        friends.clear()
        data?.searchFriends?.forEach {
            friends.add(it)
            view.onFriendsLoaded(friends)
        }
    }
}
