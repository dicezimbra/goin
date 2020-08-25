package br.com.goin.utils

import android.app.Activity
import android.content.Intent
import android.provider.CalendarContract
import androidx.core.app.ActivityCompat
import br.com.goin.base.utils.Constants
import br.com.goin.features.wallet.model.TicketModel

class Utils{
    companion object {
        fun makeRequestPermissions(context: Activity) {

            ActivityCompat.requestPermissions(context,
                    arrayOf(android.Manifest.permission.WRITE_CALENDAR, android.Manifest.permission.READ_CALENDAR),
                    Constants.REQUEST_PERMISSIONS_CALENDAR)
        }

        fun addEventToCalendar(act: Activity, mTicketModel: TicketModel, startTime: String, endTime: String) {


            val intent = Intent(Intent.ACTION_EDIT)
            //Event
            intent.data = CalendarContract.Events.CONTENT_URI
            intent.type = "vnd.android.cursor.item_success_dialog/event"
            intent.putExtra(CalendarContract.Events.TITLE, mTicketModel.eventName)
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
            intent.putExtra(CalendarContract.Events.ALL_DAY, true)
            intent.putExtra(CalendarContract.Events.DESCRIPTION, mTicketModel.event.description)

            //Reminder
            intent.putExtra(CalendarContract.Reminders.TITLE, mTicketModel.eventName)
            intent.putExtra(CalendarContract.Reminders.DURATION, 10)
            intent.putExtra(CalendarContract.Reminders.DESCRIPTION, mTicketModel.event.description)
            intent.putExtra(CalendarContract.Reminders.ALL_DAY, true)

            act.startActivity(intent)

//        val intent = Intent(Intent.ACTION_EDIT)
//        intent.type = "vnd.android.cursor.item_success_dialog/event"


        }
    }


}