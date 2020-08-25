package br.com.goin.component.session.user.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.*
import br.com.goin.base.BuildConfig
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*
import java.util.*
import com.google.android.gms.common.api.ResolvableApiException


private const val REQUEST_CHECK_SETTINGS = 12321
private val PERMISSIONS_ARRAY: Array<String> = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
private const val PERMISSIONS_REQUEST_CODE: Int = 13432

class UserLocationHelper {

    private val userLocationInteractor = UserLocationInteractor.instance

    var onRequestLocationListener: ((Location?) -> Unit)? = null

    private fun hasPermission(activity: Activity) = checkSelfPermission(activity, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED

    fun askPermission(activity: Activity) {
        if (hasPermission(activity)) {
            getCurrentLocation(activity)

        } else {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_ARRAY, PERMISSIONS_REQUEST_CODE)
        }
    }

    fun onRequestPermissionsResult(activity: Activity, requestCode: Int, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation(activity)
                } else {
                    onRequestLocationListener?.invoke(null)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(activity: Activity) {
        val fusedLocationProviderClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_LOW_POWER

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, object: LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                fusedLocationProviderClient.removeLocationUpdates(this)

                when (locationResult) {
                    null -> {
                        checkLocationConfig(activity)
                    }
                    else -> {
                        val lastLocation = locationResult.lastLocation
                        val cityFromLocation = getCityFromLocation(activity, lastLocation)

                        cityFromLocation?.let { location ->
                            if (!userLocationInteractor.hasLocation()) {
                                userLocationInteractor.save(UserLocation(lat = lastLocation.latitude,
                                        lng = lastLocation.longitude, cityName = location.locality,
                                        city = location.locality, state = null,
                                        uf = location.countryCode))
                            }
                        }

                        onRequestLocationListener?.invoke(locationResult.lastLocation)
                    }
                }
            }
        }, null)
    }

    private fun checkLocationConfig(activity: Activity) {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 30 * 1000
        locationRequest.fastestInterval = 5 * 1000

        val build = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
                .setAlwaysShow(true)
                .build()

        val task = LocationServices.getSettingsClient(activity).checkLocationSettings(build)
        task.addOnCompleteListener {
            try {
                val response = task.getResult(ApiException::class.java)
                val hasLocation = response?.locationSettingsStates?.isLocationUsable ?: false
                if (hasLocation) getCurrentLocation(activity)

            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        val resolvable = exception as ResolvableApiException
                        resolvable.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS)
                    }
                }
            }
        }
    }

    fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == Activity.RESULT_OK) {
            getCurrentLocation(activity)
        }
    }

    @SuppressLint("MissingPermission")
    fun getCityFromLocation(context: Context, location: Location): Address? {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            return geocoder.getFromLocation(location.latitude, location.longitude, 1)[0]
        }catch (exception: Exception){}

        return null
    }
}