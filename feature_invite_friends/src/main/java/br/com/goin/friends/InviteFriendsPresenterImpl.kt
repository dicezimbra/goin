package br.com.goin.friends

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.component.session.user.UserModel
import br.com.goin.component.session.user.UserSessionInteractor
import br.com.goin.friends.model.FriendToInvite
import br.com.goin.friends.model.InviteFriendsInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class InviteFriendsPresenterImpl(val view: InviteFriendsView) : InviteFriendsPresenter {

    private val interactor = InviteFriendsInteractor.instance

    private var eventId: String? = null
    private val disposible = CompositeDisposable()

    override fun onCreate() {
        view.configureToolbar()
        view.configureRecyclerView()
        view.configureSearchView()
        getFriendsToInvite()
    }

    override fun onReceivedEventId(eventId: String?) {
        this.eventId = eventId
    }

    override fun onRefresh() {
        getFriendsToInvite()
    }

    override fun onDestroy() {
        disposible.dispose()
    }

    fun getFriendsToInvite() {
        eventId?.let {
            interactor.getFriendsToInvite(it)
                    .ioThread()
                    .doOnSubscribe { view.showLoading() }
                    .doOnTerminate { view.hideLoading() }
                    .subscribe({ userModels ->
                        if (userModels.isNotEmpty()) {
                            view.receiveFriendsToInvite(userModels.toMutableList())
                        } else view.configureEmptyView()
                    }, {

                        Log.e("InviteFriendsPresenter", it.message)
                    }).addTo(disposible)
        }
    }

    override fun inviteFriend(friend: FriendToInvite) {
        if(friend.invitedByMe == false) {
            friend.invitedByMe = true
            view.updateInviteFriendItem()

            eventId?.let {
                interactor.inviteFriend(it, friend.userId)
                        .ioThread()
                        .subscribe({
                            view.inviteFrendsSuccess()
                        }, {
                            Log.e("InviteFriendsPresenter", it.message)
                        }).addTo(disposible)
            }
        }
    }
}