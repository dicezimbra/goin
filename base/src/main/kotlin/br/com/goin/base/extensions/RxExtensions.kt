package br.com.goin.base.extensions

import android.util.Log
import android.view.View
import br.com.goin.base.helpers.PreferenceHelper
import br.com.goin.base.utils.GoinCache
import com.google.gson.GsonBuilder
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

fun <T> Observable<T>.ioThread(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Maybe<T>.ioThread(): Maybe<T> {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun Completable.ioThread(): Completable {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.ioThread(): Single<T> {
    return this.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun timer(delay: Long,unit: TimeUnit,  listener: () -> Unit): Disposable{
    return Observable.timer(delay, unit)
            .ioThread()
            .subscribe({ listener() }, {t-> Log.e("Timer", t.message, t)})
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}
