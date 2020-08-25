package br.com.goin.features.ticketinfo

import android.app.AlertDialog;
import android.content.Context


class FullticketActivity : AlertDialog.Builder {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, themeResId: Int) : super(context, themeResId)


   /* override fun show(mTicketModel: TicketModel): AlertDialog {

        val viewDialog = LayoutInflater.from(context).inflate(R.layout.dialog_information_ticket, null)

        val textViewTitle: TextView = viewDialog.findViewById(R.id.textViewTitle)
        val txtEventAddressFullticket: TextView = viewDialog.findViewById(R.id.txtEventAddressFullticket)
        val txtDateEventFullticket: TextView = viewDialog.findViewById(R.id.txtDateEventFullticket)
        val textViewPlaceName: TextView = viewDialog.findViewById(R.id.textViewPlaceName)
        val textViewObs: TextView = viewDialog.findViewById(R.id.textViewObs)
        val textViewAnotherObs: TextView = viewDialog.findViewById(R.id.textViewAnotherObs)
        val txtDialogOK: TextView = viewDialog.findViewById(R.id.txtDialogOK)
        val txtAddEventToCalendar: TextView = viewDialog.findViewById(R.id.txtAddEventToCalendar)
        val btnCheckFullticketPolicy: Button = viewDialog.findViewById(R.id.btnCheckFullticketPolicy)
        val txtSeeOnGoogleMaps: TextView = viewDialog.findViewById(R.id.txtSeeOnGoogleMaps)
        val partnersIcon: ImageView = viewDialog.findViewById(R.id.dialog_info_partners)

        //Partners logo
        when (mTicketModel?.originType) {

            TicketPaymentType.ALOINGRESSOS.type -> {

                partnersIcon.setImageResource(R.drawable.icone_alo_ingresso)
            }
            TicketPaymentType.INGRESSE.type -> {

                partnersIcon.setImageResource(R.drawable.logo_vertical_claro_ingresse)
            }
            TicketPaymentType.INGRESSORAPIDO.type -> {

                partnersIcon.setImageResource(R.drawable.logo_ingresso_rapido)
            }
        }

        if (mTicketModel?.isValid == false) {
            txtAddEventToCalendar.visibility = View.GONE
        }

        //Applying custom font
        //Temps receiving model's values
        val dayTemp = mTicketModel!!.date.get(Calendar.DAY_OF_MONTH)
        val monthTemp = mTicketModel!!.date.get(Calendar.MONTH)
        val yearTemp = mTicketModel!!.date.get(Calendar.YEAR)

        //Formating temp's values
        val dayEventTemp = SimpleDateFormat("EEEE", Locale.getDefault()).format(mTicketModel!!.date!!.time)
        val monthTempName = calendar!!.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        val yearName = calendar!!.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
        val monthTempShortName = SimpleDateFormat("MMM", Locale.getDefault()).format(mTicketModel!!.date!!.time)



        val eventStartDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
        eventStartDateCalendar.timeZone = TimeZone.getTimeZone("GMT")
        eventStartDateCalendar.timeInMillis = mTicketModel!!.event.startDate
        val startHour = eventStartDateCalendar.get(Calendar.HOUR_OF_DAY)
        val startMinute = eventStartDateCalendar.get(Calendar.MINUTE)
        startTime = String.format("%02dh%02d", startHour, startMinute)


        if(mTicketModel!!.event.endDate != null) {
            val eventEndDateCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
            eventEndDateCalendar.timeZone = TimeZone.getTimeZone("GMT")
            eventEndDateCalendar.timeInMillis = mTicketModel!!.event.endDate
            val endHour = eventEndDateCalendar.get(Calendar.HOUR_OF_DAY)
            val endMinute = eventEndDateCalendar.get(Calendar.MINUTE)
            endTime = String.format("%02dh%02d", endHour, endMinute)
        }



        //setting values from model inside fields
        textViewTitle.text = mTicketModel?.eventName
        txtEventAddressFullticket.text = mTicketModel?.club


        if (startTime == endTime) {
            txtDateEventFullticket.text = "$dayEventTemp, " +
                    "$dayTemp de " +
                    "$monthTempShortName às " +
                    "$startTime"
        } else {
            txtDateEventFullticket.text = "$dayEventTemp, " +
                    "$dayTemp de " +
                    "$monthTempShortName das " +
                    "$startTime às " +
                    "$endTime"
        }
        textViewPlaceName.text = Geocoder.latLngToAddress(this,
                mTicketModel!!.event?.latitude?.toDouble(),
                mTicketModel!!.event?.longitude?.toDouble())

        if (mTicketModel!!.vipCapacity > 0) {
            textViewObs.text = "Capacidade de lugares VIP: ${mTicketModel!!.vipCapacity}"
            textViewObs.visibility = View.VISIBLE
        }
        if (mTicketModel!!.isHalfPrice != null) {
            textViewAnotherObs.text = "Seu ingresso é metade do preço! (R$ ${mTicketModel!!.isHalfPrice})"
            textViewAnotherObs.visibility = View.VISIBLE
        }

        btnCheckFullticketPolicy.setAllCaps(false)

        txtDialogOK.setOnClickListener {
            dismiss()
        }

        txtAddEventToCalendar.setOnClickListener {
            //Verify if permissions granted
            checkCalendarPermissions()
        }

        txtSeeOnGoogleMaps.setOnClickListener { seeOnGoogleMaps(textViewPlaceName.text.toString()) }

        //Opens another dialog Ticket Policy
        btnCheckFullticketPolicy.setOnClickListener {
            val alertDialogBuilder = androidx.appcompat.app.AlertDialog.Builder(this, R.style.DialogThemeTicketPolicy)
            val alertDialogTicketPolicy = alertDialogBuilder.create()
            @SuppressLint("InflateParams") val viewDialogTicketPolicy = layoutInflater.inflate(R.layout.dialog_ticket_policy, null)

            val btnBackTicketPolicy: ImageView = viewDialogTicketPolicy.findViewById(R.id.toolbar_left_icon_ticket_policy)

            btnBackTicketPolicy.setOnClickListener { _ ->
                alertDialogTicketPolicy.dismiss()
            }

            alertDialogTicketPolicy.setView(viewDialogTicketPolicy)
            alertDialogTicketPolicy.show()
        }

        setView(viewDialog)
        return super.show()
    }

    private fun checkCalendarPermissions(context: Activity) {

        val calendarPermission = ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.WRITE_CALENDAR)

        if (calendarPermission != PackageManager.PERMISSION_GRANTED) {
            Utils.makeRequestPermissions(context)
        } else {
            try {
                Utils.addEventToCalendar()
            } catch (e: Exception) {
                Toast.makeText(context, "Calendário indisponível!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun seeOnGoogleMaps(address: String) {

        Geocoder.addressToLatLng(this, address)

        var strUri = ""

        if (Geocoder.addressToLatLng(this, address) != null) {

            strUri = "http://maps.google.com/maps?q=loc:" + Geocoder.addressToLatLng(this, address)!!.toString()
                    .replace("(", "")
                    .replace(")", "")
                    .replace(":", "")
                    .replace("lat/lng", "")
        }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
        startActivity(intent)
    }
*/
}