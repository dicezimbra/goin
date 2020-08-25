package br.com.legacy.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import br.com.R;
import br.com.goin.base.utils.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by appsimples on 9/18/17.
 */

public class DateFormatter {

    public static Long stringToTimestampAmerican(String dateString) {
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date = formatter.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Converting string from webservice Timestamp to brazilian date
     * Returning: dd/mm/yyyy
     * Example: 01/01/2018
     */
    public static String stringToTimestampBrazilian(Long timestamp) {
        Date date = new Date(timestamp);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat timeSDF = new SimpleDateFormat("HH:mm"); // the format of your time
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateSDF = new SimpleDateFormat("dd/MM/yyyy"); // the format of your date

        timeSDF.setTimeZone(TimeZone.getTimeZone("GMT-2"));

        String finalTime = dateSDF.format(date) + " às " + timeSDF.format(date);
        return finalTime;
    }

    public static Long stringToTimestamp(String dateString) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(Utils.INSTANCE.getDateFormat());
        Date date = formatter.parse(dateString);
        return date.getTime();
    }

    public static String getTimeDifText(Long timestamp, Context context) {
        Date now = Calendar.getInstance().getTime();
        Long seconds = now.getTime() / 1000l - timestamp / 1000l;
        Long minutes = seconds / 60l;

        if (seconds < 0) {
            return (context.getResources().getQuantityString(R.plurals.seconds_ago, seconds.intValue(), 0));
        }

        if (seconds < 60) {
            return (context.getResources().getQuantityString(R.plurals.seconds_ago, seconds.intValue(), seconds));
        }
        if (minutes >= 1 && minutes < 60) {
            return (context.getResources().getQuantityString(R.plurals.minutes_ago, minutes.intValue(), minutes));
        }
        Long hours = minutes / 60l;
        if (hours >= 1 && hours < 24) {
            return (context.getResources().getQuantityString(R.plurals.hours_ago, hours.intValue(), hours));
        }
        Long days = hours / 24l;
        if (days >= 1 && days < 365) {
            return (context.getResources().getQuantityString(R.plurals.days_ago, days.intValue(), days));
        }
        return context.getString(R.string.long_time_ago);
    }

}
