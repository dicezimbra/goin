package br.com.legacy.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import com.google.android.gms.location.*
import java.lang.Exception

@SuppressLint("MissingPermission")
class GpsUtils {

    companion object {

        fun getCurrentLocation(activity: Activity, callbackSuccess: (Location) -> Unit, callbackError: (Exception) -> Unit) {
            val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 2000
            locationRequest.maxWaitTime = 1
            locationRequest.fastestInterval = 1000


            fusedLocationProviderClient.lastLocation.addOnSuccessListener(activity) { p0 ->
                p0?.let {
                    callbackSuccess.invoke(it)
                }
            }

            fusedLocationProviderClient.lastLocation.addOnFailureListener {
                callbackError.invoke(it)
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

    }
}

