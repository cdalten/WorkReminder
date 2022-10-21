/*
 Copyright © 2017-2019 Chad Altenburg <cdalten@PumpingDansHotLookingStepMom.com>

 Permission to use, copy, modify, distribute, and sell this software and its
 documentation for any purpose is hereby granted without fee, provided that
 the above copyright notice appear in all copies and that both that
 copyright notice and this permission notice appear in supporting
 documentation.  No representations are made about the suitability of this
 software for any purpose.  It is provided "as is" without express or
 implied warranty.
*/
package com.example.cd.workreminder;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//Because I don't have the time nor patience to install the new fangled library on my device emulator.
//Maybe they shouldn't fucking reinvent shit?
public class MilitaryTime extends FragmentActivity {
    private int startMilitaryHour = 0;
    private int endMilitaryHour = 0; //Need to convert back to string??
    private String startAmOrPm = ""; //added on 12 - 18 - 2019
    private int startMilitaryMinute = 0;
    private int endMilitaryMinute = 0;
    private String endAmOrPm = ""; //Added on 12 - 18 - 2019
    Context context;

    private static MilitaryTime instance = new MilitaryTime();

    //CurrentWorkHours currentWorkHours;
    private MilitaryTime () {
    //    currentWorkHours = new CurrentWorkHours();
    }

    public static MilitaryTime getInstance(){
        return instance;
    }


    //Added on 7 - 9 - 2019
    public void convertEndCivilianTimeToMilitaryTime(String endHour, String endMinute, String endAmOrPm) {
        String timeFormat = endHour + " " + endMinute  + " " + endAmOrPm;
        DateFormat df = new SimpleDateFormat("hh mm aa");
        String[] newTime = new String[3];
        newTime[0] = "0";
        newTime[1] = "0";

        Date date = null;
        try {
            date = df.parse(timeFormat);
        } catch (Exception e) {
            //pass

        }

        try {
            String time[] = date.toString().split(" ");
            newTime = time[3].split(":");
        } catch (Exception e) {
            //pass
        }
        endMilitaryHour = Integer.parseInt(newTime[0]);
        endMilitaryMinute = Integer.parseInt(newTime[1]);
        this.endAmOrPm = endAmOrPm;

    }

    public void convertStartCivilianTimeToMilitaryTime(String startHour, String startMinute, String startAmOrPm) {
        String timeFormat = startHour + " " + startMinute  + " " + startAmOrPm;
        DateFormat df = new SimpleDateFormat("hh mm aa");
        //String militaryTime = null;
        Date date = null;
        String[] newTime = new String[3];
        newTime[0] = "0";
        newTime[1] = "0";

        try {
            date = df.parse(timeFormat);
        } catch (Exception e) {
            //pass
        }

        try {
            String time[] = date.toString().split(" ");
            newTime = time[3].split(":");
        } catch (Exception e) {
            //pass
        }
        startMilitaryHour = Integer.parseInt(newTime[0]);
        startMilitaryMinute = Integer.parseInt(newTime[1]);
        this.startAmOrPm = startAmOrPm;

        /*if (startAmOrPm.equals("PM") && !startHour.equals("12")) {
            startMilitaryHour = Integer.parseInt(startHour) + 12;
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
        */
    }

    public int getStartMilitaryHour() {
        return this.startMilitaryHour;
    }

    public int getStartMilitaryMinute() {
        return this.startMilitaryMinute;
    }

    //Added on 12 - 18 - 2019
    public String getStartAmOrPm() {
        return this.startAmOrPm;
    }

    //Added on 12 - 19 - 2019
    public String getEndAmOrPm() {
        return this.endAmOrPm;
    }
    //Added on 7 - 9 - 2019
    public int getEndMilitaryHour() {
        return this.endMilitaryHour;
    }

    public int getEndMilitaryMinute() {
        return this.endMilitaryMinute;
    }

    //Added on 10 - 18 - 2018. True if I work. False if I'm either done working or have the day off.
    /*public boolean getStartingHour(int startHour, int startMinute) {

        return currentWorkHours.doIWorkToday(context, startHour, startMinute, endMilitaryHour, endMilitaryMinute);
    }
    */
    //Added on 7 - 10 - 2019
    /*public boolean getHour(Context context, int startMilitaryHour, int startMilitaryMinute, int endMilitaryHour, int endMilitaryMinute) {

        return currentWorkHours.doIWorkToday(context, startMilitaryHour, startMilitaryMinute, endMilitaryHour, endMilitaryMinute);
    }*/
}
