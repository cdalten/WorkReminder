package com.example.cd.workreminder;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static java.util.Calendar.HOUR;

public class CurrentWorkHours extends Date {
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

    public CurrentWorkHours(long time) {
        super(time);
        //IStartIn.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
    }

    //Added on 10 - 22 - 2018. Changed to boolean on 11 - 1 - 2018
    //need to add end time
    //Change time in milliseconds to elapsed system phone time???
    public boolean doIWorkToday(String startHour, String startMinute, String startAmOrPm) {
        //startMilitaryTime = convertToMilitaryTime(startHour + ":" + startMinute + "" + startAmOrPm);
        //currentMilitaryTime = getCurrentTime(); //returns HH:mm in military time format. Convert to milliseconds??
        //currentTimeInMilliseconds= System.currentTimeMillis();
        //endMilitaryTime = convertToMilitaryTime(endTime);

        //startTimeInMilliseconds = convertSystemTimeToMilliseconds(startMilitaryTime)


        currentTimeInMilliseconds = System.currentTimeMillis();
        startTimeInMilliseconds = convertSystemTimeToMilliseconds(startHour, startMinute);

        return doIStart(startTimeInMilliseconds, currentTimeInMilliseconds, endTimeInMilliseconds);

    }

    //Added on 10 - 25 - 2018
    private boolean doIStart(long startTime, long currentTime, long endTime) {
        long temp = startTime - currentTime;
        long end = 0;
        //Date start = new Date(temp);
        if (currentTime < startTime) {
            //Log.i(PRODUCTION_TAG, "You start in: " + this.getCurrentTime());
            Log.i(PRODUCTION_TAG, "You have to be to the slave camp today.");
            return true;
        } else if (currentTime > endTime){ //bug. Need to account for midnight or change in date??
            end = currentTime - endTime;
            Log.i(PRODUCTION_TAG, "You're done for the day");
            return false;
        }
        return false; //else case??
    }

    //Added on 2 - 25 - 2019
    private long convertSystemTimeToMilliseconds(String currentHour, String currentMinute) {
        Calendar mycalendar = Calendar.getInstance();
        mycalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(currentHour));
        mycalendar.set(Calendar.MINUTE, Integer.parseInt(currentMinute));
        mycalendar.set(Calendar.SECOND, 0);

        return mycalendar.getTimeInMillis();
        //return mycalendar.get(Calendar.MILLISECOND);
    }

    //Added on 4 - 23 - 2019
    public long getCurrentTimeInMilliseconds() {
        return currentTimeInMilliseconds;
    }


    @Override
    public String toString() {
        //return dateFormat.format(this);
        return alarmDateFormat.format(this);
    }
}
