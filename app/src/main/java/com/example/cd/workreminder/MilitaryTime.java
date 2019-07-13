package com.example.cd.workreminder;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

//Because I don't have the time nor patience to install the new fangled library the device emulator.
//Maybe they shouldn't fucking dep. shit?
public class MilitaryTime extends FragmentActivity {
    private int startMilitaryHour = 0;
    private int endMilitaryHour = 0; //Need to convert back to string??
    private int startMilitaryMinute = 0;
    private int endMilitaryMinute = 0;
    Context context;

    private static MilitaryTime instance = new MilitaryTime();

    CurrentWorkHours currentWorkHours;
    private MilitaryTime () {
        currentWorkHours = new CurrentWorkHours();
    }

    public static MilitaryTime getInstance(){
        return instance;
    }


    //Added on 7 - 9 - 2019
    @TargetApi(24)
    public void convertEndCivilianTimeToMilitaryTime(String endHour, String endMinute, String endAmOrPm) {
        if (endAmOrPm.equals("PM")) {
            endMilitaryHour = Integer.parseInt(endHour);
            endMilitaryMinute = Integer.parseInt(endMinute);
            //setStartMilitaryHour(startMilitaryHour);
            //setStartMilitaryMinute(Integer.parseInt(startMinute));
        } else if (endAmOrPm.equals("AM") && endHour.equals("12")) {
            endMilitaryHour = 24;
            endMilitaryMinute = Integer.parseInt(endMinute);
        }
        else {
            try {
                endMilitaryHour = Integer.parseInt(endHour);
                endMilitaryMinute = Integer.parseInt(endMinute);
            } catch (Exception e) {
                Log.e("LG_WORK_PHONE",  "BLANK: " + e);
            }

        }
    }

    @TargetApi(24)
    public void convertStartCivilianTimeToMilitaryTime(String startHour, String startMinute, String startAmOrPm) {
        if (startAmOrPm.equals("PM")) {
            startMilitaryHour = Integer.parseInt(startHour);
            startMilitaryMinute = Integer.parseInt(startMinute);
            //setStartMilitaryHour(startMilitaryHour);
            //setStartMilitaryMinute(Integer.parseInt(startMinute));
        } else if (startAmOrPm.equals("AM") && startHour.equals("12")) {
            startMilitaryHour = 24;
            startMilitaryMinute = Integer.parseInt(startMinute);
        }
        else {
            try {
                startMilitaryHour = Integer.parseInt(startHour);
                startMilitaryMinute = Integer.parseInt(startMinute);
            } catch (Exception e) {
                Log.e("LG_WORK_PHONE",  "BLANK: " + e);
            }

        }
    }

    public int getStartMilitaryHour() {
        return this.startMilitaryHour;
    }

    public int getStartMilitaryMinute() {
        return this.startMilitaryMinute;
    }

    //Added on 7 - 9 - 2019
    public int getEndMilitaryHour() {
        return this.endMilitaryHour;
    }

    public int getEndMilitaryMinute() {
        return this.endMilitaryMinute;
    }

    //Added on 10 - 18 - 2018. True if I work. False if I'm either done working or have the day off.
    public boolean getStartingHour(int startHour, int startMinute) {

        return currentWorkHours.doIWorkToday(context, startHour, startMinute, endMilitaryHour, endMilitaryMinute);
    }

    //Added on 7 - 10 - 2019
    public boolean getHour(Context context, int startMilitaryHour, int startMilitaryMinute, int endMilitaryHour, int endMilitaryMinute) {

        return currentWorkHours.doIWorkToday(context, startMilitaryHour, startMilitaryMinute, endMilitaryHour, endMilitaryMinute);
    }
}
