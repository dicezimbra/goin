package br.com.goin.feature_social

import br.com.goin.base.extensions.ioThread
import br.com.goin.feature_social.model.FollowInteractor
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class FollowPresenterImpl(val view: FollowActivity) : FollowPresenter {

    private var disposables = CompositeDisposable()
    private var followInteractor = FollowInteractor.instance

    override fun onCreate() {
        view.setupToolbar()
        view.configureRecyclerView()
        view.setupEmptyListMessage()
    }

    override fun loadUserModels(userId: String?, relation: FollowRelation) {
        followInteractor.requestFollowersList(userId, relation)
                .ioThread()
                .doOnSubscribe { view.showLoading() }
                .doOnTerminate { view.hideLoading() }
                .subscribe({
                    view.configureUserList(it)
                }, {
                    view.showSnackBarOnError()
                }).addTo(disposables)
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

    override fun logOnAnalytics(screenName: String, category: String, action: String, label: String?) {
    }

    override fun onDestroy() {
        disposables.dispose()
        disposables = CompositeDisposable()

    }
}