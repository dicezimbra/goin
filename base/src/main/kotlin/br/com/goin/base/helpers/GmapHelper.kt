package br.com.goin.base.helpers

import android.content.Context
import android.content.Intent
import android.net.Uri

object GmapHelper {

    fun openMapsInPosition(context: Context, lat: Double?, lng: Double?) {
        val uri = "google.navigation:q=${lat?.toFloat()},${lng?.toFloat()}"
        val directionsIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        directionsIntent.setPackage("com.google.android.apps.maps")
        if (directionsIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(directionsIntent)
        }
    }

    fun openMapsRoute(context: Context, myLat: Double?, myLng: Double?, lat: Float?, lng: Float?) {
        val uri = "http://maps.google.com/maps?saddr=$myLat,$myLng&daddr=$lat,$lng&mode=driving"
        val directionsIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        directionsIntent.setPackage("com.google.android.apps.maps")
        if (directionsIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(directionsIntent)
        }
    }
}