package br.com.goin.base.utils;

import android.app.Activity;
import android.location.Address;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Geocoder {

    public static final String TAG = Geocoder.class.getSimpleName();

    public static LatLng addressToLatLng(Activity activity, String address) {

        String result = "";
        Double resultLat = null, resultLng = null;
        LatLng resultLatLng = null;

        android.location.Geocoder geocoder = new android.location.Geocoder(activity, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && addresses.size() > 0) {
                resultLat = addresses.get(0).getLatitude();
                resultLng = addresses.get(0).getLongitude();

                resultLatLng = new LatLng(resultLat, resultLng);
                Log.i(TAG, "LatLng = "+resultLatLng);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        return resultLatLng;
    }

    public static String latLngToAddress(Activity activity, Double latitude, Double longitude) {

        String addressConverted = null;
        android.location.Geocoder geocoder = new android.location.Geocoder(activity);

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            for (int i = 0; i < addresses.size(); i++) {
                if (addresses.size() > 0) {
                    addressConverted = addresses.get(i).getAddressLine(i);
                    Log.i(TAG, "Address (LAT/LNG): " + addressConverted);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }

        return addressConverted;
    }


}
