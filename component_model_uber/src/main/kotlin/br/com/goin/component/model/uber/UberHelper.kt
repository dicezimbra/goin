package br.com.goin.component.model.uber

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.uber.sdk.android.rides.RequestDeeplink
import com.uber.sdk.android.rides.RideParameters
import com.uber.sdk.rides.client.SessionConfiguration

object UberHelper {

    fun openUberDeeplink(currentLocation: LatLng?, location: LatLng,nickname: String , address: String,  context: Context) {
        val rideBuilder = RideParameters.Builder()
                .setDropoffLocation(location.latitude, location.longitude, nickname, address)
                .setProductId("a1111c8c-c720-46c3-8534-2fcdd730040d")

        currentLocation?.let {
            rideBuilder.setPickupLocation(currentLocation.latitude, currentLocation.longitude, "", "")
        }

        val rideParams = rideBuilder.build()

        val config = SessionConfiguration.Builder()
                .setClientId("jVa2_52ynlZabGCZjo9eL6MWKyOx9gui")
                .setServerToken("0rdzwbkE37saPIdLiKo-Co3njKIL3vvRSdyXaD93")
                .build()

        val deeplink = RequestDeeplink.Builder(context)
                .setSessionConfiguration(config)
                .setRideParameters(rideParams)
                .build()

        deeplink.execute()
    }
}
