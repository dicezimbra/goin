package br.com.goin.feature.profile.myinterests

import android.util.Log
import br.com.goin.base.extensions.ioThread
import br.com.goin.base.extensions.useCache
import br.com.goin.component.model.event.Event
import br.com.goin.component.model.event.EventInteractor
import br.com.goin.component.model.event.api.Interrest
import br.com.goin.component.session.user.UserModel
import br.com.goin.component.session.user.UserSessionInteractor
import com.google.gson.reflect.TypeToken
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class MyInterestPresenterImpl(val view: MyInterestView): MyInterestPresenter{

    private val interactor = EventInteractor.instance

    private val diposable = CompositeDisposable()
    private var profileId: String? = null

    override fun onCreate(){
        profileId?.let {
            loadInterests(it)
        }
    }

    override fun onReceivedProfileId(profileId: String?) {
        this.profileId = profileId
    }

    private fun loadInterests(userId: String){
        interactor.getInterests(userId)
                .useCache("INTERESTS$userId",object : TypeToken<ArrayList<Interrest>>() {}.type)
                .ioThread()
                .doOnSubscribe { view.showLoading() }
                .subscribe({ events ->
                    view.hideLoading()
                    if(events.isEmpty()){
                        view.showEmptyView()
                    }else {
                        view.configureMyInterests(events)
                    }
                }, { throwable ->
                    view.hideLoading()
                    Log.e("MyInterestPresenter", throwable.message, throwable)
                })
                .addTo(diposable)
    }
}