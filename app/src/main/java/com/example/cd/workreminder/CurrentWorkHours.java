package com.example.cd.workreminder;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static java.util.Calendar.HOUR;


//Changed from Date to app compat act on 7 - 11 - 2019
public class CurrentWorkHours extends AppCompatActivity {
    //I don't use second because the clocks aren't that accurate.
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm"); //Handle military time
    //private final SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
    //private SimpleDateFormat startHourMilitaryFormat = new SimpleDateFormat("HH"); //Added on 4 - 11 - 2019
    //private SimpleDateFormat startMinuteMilitaryFormat = new SimpleDateFormat();

    private SimpleDateFormat alarmDateFormat = new SimpleDateFormat("yyyy MM dd", Locale.US);

    //private final SimpleDateFormat IStartIn = new SimpleDateFormat("hh:mm");

    private final String PRODUCTION_TAG = "LG_WORK_WEB: ";
    private String startTime; //added on 10 - 26 - 2018
    private String currentTime; //added on 10 - 26 - 2018
    private String endTime;//added on 10 - 26 - 2018


    private long startTimeInMilliseconds;
    private long currentTimeInMilliseconds;
    private long endTimeInMilliseconds;

    public CurrentWorkHours() { ;
        //SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        timeFormat.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        //sdf.format(new Date());
    }


    //Added on 7 - 10 - 2019
    /*
      * If the shift is either 1pm - 9pm or 9pm to - 2am does 9pm represent the start of end of the shift?
     */
    private boolean isstartHour(long startTimeInMilliseconds, long endTimeInMilliseconds) {
        if (startTimeInMilliseconds < endTimeInMilliseconds) {
            return true;
        } else {
            return false;
        }
    }

    //Added on 10 - 22 - 2018. Changed to boolean on 11 - 1 - 2018
    //need to add end time
    //Change time in milliseconds to elapsed system phone time???
    public boolean doIWorkToday(Context context, int startMilitaryHour, int startMilitaryMinute, int endMilitaryHour, int endMilitaryMinute) {
        startTimeInMilliseconds = convertStartSystemTimeToMilliseconds(startMilitaryHour, startMilitaryMinute);
        currentTimeInMilliseconds = System.currentTimeMillis();
        endTimeInMilliseconds = convertEndSystemTimeToMilliseconds(endMilitaryHour, endMilitaryMinute);

        return doIStart(context, startTimeInMilliseconds, currentTimeInMilliseconds, endTimeInMilliseconds);
    }

    //Added on 10 - 25 - 2018
    protected boolean doIStart(Context context, long startTime, long currentTime, long endTime) {
        long temp = startTime - currentTime;
        long end = 0;
        //Date start = new Date(temp);
        if (isstartHour(startTime, endTime)) { //1pm - 9pm              1pm < 9pm
            if (currentTime < startTime) {
                //Log.i(PRODUCTION_TAG, "You start in: " + this.getCurrentTime());
                Log.i(PRODUCTION_TAG, "You have to be to the slave camp today(1).");
                return true;
            }

            if (currentTime < endTime && currentTime < startTime) {
                WorkNotification.notify(context, "YOU ARE SUPPOSED TO BE AT WORK.",
                        0);
                Log.i(PRODUCTION_TAG, "You're supposed to be at work(2)");
                return true; //Supposed to be at work?
            }

            if (currentTime > endTime){
                Log.i(PRODUCTION_TAG, "You're done for the day(1)");
                return false;
            }
        } else { // 9pm - 2am       9pm > 2am
            if (currentTime > endTime) { //currentTime > 2am
                Log.i(PRODUCTION_TAG, "You're done for the day(2)");
                return false;
            }

            if (currentTime < endTime && currentTime < startTime) {
                WorkNotification.notify(context, "YOU ARE SUPPOSED TO BE AT WORK.",
                        0);
                Log.i(PRODUCTION_TAG, "You're supposed to be at work(2)");
                return true; //Supposed to be at work?
            }

            if (currentTime < endTime) { //currentTme < 9pm
                Log.i(PRODUCTION_TAG, "You have to be to slave camp today(2");
                return true;
            }
        }

        return false; //else case??
    }

    //Added on 2 - 25 - 2019
    private long convertStartSystemTimeToMilliseconds(int startMilitaryHour, int startMilitaryMinute) {
        Calendar mycalendar = Calendar.getInstance();
        mycalendar.set(Calendar.HOUR_OF_DAY, startMilitaryHour);
        mycalendar.set(Calendar.MINUTE, startMilitaryMinute);
        mycalendar.set(Calendar.SECOND, 0);

        return mycalendar.getTimeInMillis();
        //return mycalendar.get(Calendar.MILLISECOND);
    }

    //Added on 7 - 10 - 2019
    private long convertEndSystemTimeToMilliseconds(int endMilitaryHour, int endMilitaryMinute) {
        Calendar mycalendar = Calendar.getInstance();
        mycalendar.set(Calendar.HOUR_OF_DAY, endMilitaryHour);
        mycalendar.set(Calendar.MINUTE, endMilitaryMinute);
        mycalendar.set(Calendar.SECOND, 0);

        return mycalendar.getTimeInMillis();
        //return mycalendar.get(Calendar.MILLISECOND);
    }



    @Override
    public String toString() {
        //return dateFormat.format(this);
        return alarmDateFormat.format(this);
    }
}
