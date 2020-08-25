package br.com.goin.findfriends

import br.com.goin.base.extensions.ioThread
import br.com.goin.component.session.user.UserModel
import br.com.goin.findfriends.model.FindFriendsInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class FindFriendsPresenterImpl(val view: FindFriendsActivity) : FindFriendsPresenter {

    private var followInteractor = FindFriendsInteractor.instance
    private var disposables = CompositeDisposable()

    override fun onCreate() {
        view.run {
            setupToolbar()
            configureSearch()
            configureRecyclerView()
        }
    }

    override fun onSearchFriend(searchInput: String) {
        followInteractor.requestFollowersList(searchInput)
                .ioThread()
                .doOnSubscribe { view.showLoading() }
                .doOnTerminate { view.hideLoading() }
                .subscribe({ friendsList ->
                    setupData(friendsList)
                    view.configureFriendsList(friendsList)
                }, {
                    view.showSnackBarOnError()
                }).addTo(disposables)
    }

    private fun setupData(friendsList: List<UserModel>) {
        if (friendsList.isEmpty()) {
            view.showEmptyMessage()
        } else {
            view.showFriendsList()
            view.configureFriendsList(friendsList)
        }
    }


    override fun followUser(userId: String?) {
        followInteractor
                .followUser(userId)
                .ioThread()
                .doOnSubscribe { view.changeUserStatus(userId, true) }
                .subscribe({
                }, {
                    view.changeUserStatus(userId, false)
                    view.showSnackBarOnError()
                }).addTo(disposables)
    }

    override fun unfollowUser(userId: String?) {
        followInteractor
                .unfollowUser(userId)
                .ioThread()
                .subscribe({
                    view.changeUserStatus(userId, false)
                }, {
                    view.changeUserStatus(userId, true)
                    view.showSnackBarOnError()
                }).addTo(disposables)
    }

    override fun onDestroy() {
        disposables.dispose()
        disposables = CompositeDisposable()
    }

    override fun logOnAnalytics(screenName: String, category: String, action: String, label: String?) {
    }
}
