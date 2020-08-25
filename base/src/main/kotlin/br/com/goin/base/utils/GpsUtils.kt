package br.com.goin.base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.*
import io.reactivex.Observable
import java.lang.Exception

@SuppressLint("MissingPermission")
class GpsUtils {

    companion object {

        @JvmStatic
        fun getCurrentLocation(context: Context, callbackSuccess: (Location) -> Unit, callbackError: (Exception) -> Unit = {}) {
            val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 2000
            locationRequest.maxWaitTime = 1
            locationRequest.fastestInterval = 1000

            fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                if(it.isSuccessful){
                    it.result?.let{
                        callbackSuccess(it)
                    }
                }else{
                    it.exception?.let{
                        callbackError(it)
                    }
                }
            }
        }

        @JvmStatic
        fun getDistance(latitude: Double, longitude: Double, latitude1: Double, longitude1: Double): Observable<Float> {
            return Observable.create<Float> {
                try{
                    if(!it.isDisposed){
                        it.onNext(calculateDistance(latitude, longitude, latitude1, longitude1) / 1000)
                    }
                }catch (t: Throwable){
                    if(!it.isDisposed){
                        it.onError(t)
                    }
                }
            }
        }

        @JvmStatic
        fun calculateDistance(latitude: Double, longitude: Double, latitude1: Double, longitude1: Double): Float {
            val locationA = Location("point A")

            locationA.latitude = latitude
            locationA.longitude = longitude

            val locationB = Location("point B")

            locationB.latitude = latitude1
            locationB.longitude = longitude1

            val distance = locationA.distanceTo(locationB)

            return distance
        }

        @JvmStatic
        fun calculate(latitude: Double?, longitude: Double?, latitude1: Double?, longitude1: Double?): Float {
            latitude?.let { latitude->
                longitude?.let { longitude->
                    longitude1?.let { longitude1->
                        latitude1?.let { latitude1->
                            return  calculateDistance(latitude, longitude, latitude1, longitude1) / 1000

                        }
                    }
                }
            }
            return 0.0F
        }

        fun formatt(distance: Float?): String {
            return String.format("%.1f km", distance)
        }

    }
}

