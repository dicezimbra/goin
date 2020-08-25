package br.com.goin.feature.search.location.searchLocation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.*
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationCallback
import io.reactivex.Observable
import java.util.*

object LocationHelper {

    @SuppressLint("MissingPermission")
    fun getCitiesFromLocation(context: Context, location: Location): Observable<List<Address>> {
        return Observable.create<List<Address>> { emitter ->
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                emitter.onNext(geocoder.getFromLocation(location.latitude, location.longitude, 20).filter { !it.featureName.isNullOrBlank() })
            } catch (throwable: Throwable) {
                emitter.onError(throwable)
            }

            emitter.onComplete()
        }
    }
}
