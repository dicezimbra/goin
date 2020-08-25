package br.com.goin.feature.rating

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.extensions.useCache
import br.com.goin.component.session.user.UserSessionInteractor
import br.com.goin.component_model_club.ClubInteractor
import br.com.goin.component_model_club.model.ClubRatingModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class RatingPresenterImpl(val view: RatingView) : RatingPresenter {



    private var userInteractor = UserSessionInteractor.instance
    private var clubId: String? = ""
    private val disposable = CompositeDisposable()

    override fun onReceiveClub(clubId: String?) {
        this.clubId = clubId
    }

    private val interactor = ClubInteractor.instance

    override fun loadRatings() {
        clubId?.let {
            interactor.getClubRatings(it)
                    .useCache("RATING_CACHE_${it}", ClubRatingModel::class.java)
                    .ioThread()
                    .doOnTerminate { view.hideLoading() }
                    .doOnSubscribe { view.showLoading() }
                    .subscribe({
                        view.loadRatings(it)
                    }, { t ->
                        view.handleError(t)
                        Log.e("PlacePresenter", t.message, t)
                    })
                    .addTo(disposable)
        }
    }

    override fun onDestroy() {
        disposable.dispose()
    }

    override fun loadUserInfos()  {
        view.loadUserInfos( userInteractor.fetchUser())
    }

    override fun userRating(rating: Float, text: String?) {
        clubId?.let {
            interactor.userRating(rating, text, it, userInteractor.fetchUser()?.id)
                    .ioThread()
                    .doOnTerminate { view.hideLoading() }
                    .doOnSubscribe { view.showLoading() }
                    .subscribe({
                        view.ratingSuccess(it)
                    }, { t ->
                        view.handleError(t)
                        Log.e("PlacePresenter", t.message, t)
                    })
                    .addTo(disposable)
        }
    }
}