package br.com.goin.feature.event

import android.app.Activity
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.ImageView
import br.com.goin.component.model.event.Event
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import iammert.com.expandablelib.ExpandableLayout

object EventHelper {

    fun checkGPSIsActive(activity: Activity, requestCode: Int) {
        val locationManager: LocationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.let { it ->
            if (!it.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val locationRequest = LocationRequest.create()

                locationRequest
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setMaxWaitTime(1)
                        .setInterval(5000).fastestInterval = 2000

                val locationSettingsRequestBuilder: LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
                locationSettingsRequestBuilder.addLocationRequest(locationRequest)
                locationSettingsRequestBuilder.setNeedBle(true)

                val result: Task<LocationSettingsResponse> =
                        LocationServices.getSettingsClient(activity).checkLocationSettings(locationSettingsRequestBuilder.build())

                result.addOnCompleteListener {
                    try {
                        it.getResult(ApiException::class.java)
                    } catch (exception: ApiException) {
                        when (exception.statusCode) {
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                                val resolvable: ResolvableApiException = exception as ResolvableApiException
                                resolvable.startResolutionForResult(activity, requestCode)
                            } catch (exception: ApiException) {
                                Log.d("EventActivity", exception.statusCode.toString())
                            }
                        }
                    }
                }
            }
        }
    }

    fun showDialogConfirmedPresenceSuccess(context: Context) {
        val alert = AlertDialog.Builder(context)
        alert.setMessage(R.string.presence_confirmed)
        alert.setPositiveButton(R.string.alert_ok) { dialog, _ ->
            dialog.dismiss()
        }

        alert.show()
    }

    fun showErrorDialog(context: Context, error: String): AlertDialog {
        val alert = AlertDialog.Builder(context)
        alert.setMessage(error)
        alert.setPositiveButton(R.string.alert_ok) { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = alert.create()
        dialog.show()
        return dialog
    }

    fun showDialogUnconfirmPresence(like_view: View, event: Event, onConfirm: () -> Unit) {
        val alert = AlertDialog.Builder(like_view.context)
        alert.setMessage(R.string.unfollow_event_question)
        alert.setPositiveButton(R.string.yes) { dialog, _ ->
            event.isConfirmed = false

            like_view.isSelected = false

            dialog.dismiss()
            onConfirm()
        }

        alert.setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }
        alert.show()

    }

    fun settingExpandableLayout(context: Context, expandableLayout: ExpandableLayout) {
        expandableLayout.setRenderer(object : ExpandableLayout.Renderer<ExpandableParent, ExpandableChild> {

            override fun renderParent(view: View?, modelParent: ExpandableParent?, isExampadable: Boolean, parentPos: Int) {
                (view?.findViewById<View>(R.id.expandablet_text) as TextView).text = modelParent?.text
                (view.findViewById<View>(R.id.expandable_arrow) as ImageView).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.event_arrow_down))
                (view.findViewById<View>(R.id.expandable_icon)).visibility = View.VISIBLE
                (view.findViewById<View>(R.id.expandable_icon) as ImageView).setImageDrawable(ContextCompat.getDrawable(context, modelParent!!.drawableId))
            }

            override fun renderChild(view: View?, modelChild: ExpandableChild?, parentPos: Int, childPos: Int) {
                (view?.findViewById<View>(R.id.textView4) as TextView).text = modelChild?.text
            }
        })

        expandableLayout.setExpandListener<ExpandableParent> { _, _, view -> (view.findViewById<View>(R.id.expandable_arrow) as ImageView).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.event_icon_arrow_up)) }
        expandableLayout.setCollapseListener<ExpandableParent> { _, _, view -> (view.findViewById<View>(R.id.expandable_arrow) as ImageView).setImageDrawable(ContextCompat.getDrawable(context, R.drawable.event_arrow_down)) }
    }

}