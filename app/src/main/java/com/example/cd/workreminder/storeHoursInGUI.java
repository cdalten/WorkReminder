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
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class storeHoursInGUI extends FragmentActivity{
    ArrayList<ArrayList<String>> week;
    SharedPreferences pref;
    Context context; //Added on 10 - 8 - 2019

    public storeHoursInGUI(Context context) {
        this.context = context;
    }

    public ArrayList addHours() {
        pref = context.getSharedPreferences("BECAUSE_INTENTS_SUCK_MASSIVE_DICK", MODE_PRIVATE);
        week = new ArrayList<ArrayList<String>>();
        //week.clear(); //stupid hack. Don't ask.

        week.add(WorkReaderContract.SUNDAY, new ArrayList());
        week.add(WorkReaderContract.MONDAY, new ArrayList());
        week.add(WorkReaderContract.TUESDAY, new ArrayList());
        week.add(WorkReaderContract.WEDNESDAY, new ArrayList());
        week.add(WorkReaderContract.THURSDAY, new ArrayList());
        week.add(WorkReaderContract.FRIDAY, new ArrayList());
        week.add(WorkReaderContract.SATURDAY, new ArrayList());
        week.add(WorkReaderContract.OFF, new ArrayList());


        week.get(WorkReaderContract.SUNDAY);
        week.get(WorkReaderContract.SUNDAY).add(0,
                pref.getString(context.getString(R.string.SUNDAY_HOURS), context.getString(R.string.SUNDAY_HOURS)));
        week.get(WorkReaderContract.SUNDAY)
                .add(WorkReaderContract.START_HOUR,
                        pref.getString(context.getString(R.string.SUNDAY_START_HOUR), WorkReaderContract.START_HOUR_DEFAULT));
        week.get(WorkReaderContract.SUNDAY)
                .add(WorkReaderContract.START_MINUTE,
                        pref.getString(context.getString(R.string.SUNDAY_START_MINUTE), WorkReaderContract.START_MINUTE_DEFAULT));
        week.get(WorkReaderContract.SUNDAY)
                .add(WorkReaderContract.START_AM_OR_PM,
                        pref.getString(context.getString(R.string.SUNDAY_START_AM_OR_PM), WorkReaderContract.START_AM_OR_PM_DEFAULT));
        week.get(WorkReaderContract.SUNDAY)
                .add(WorkReaderContract.END_HOUR,
                        pref.getString(context.getString(R.string.SUNDAY_END_HOUR), WorkReaderContract.END_HOUR_DEFAULT));
        week.get(WorkReaderContract.SUNDAY)
                .add(WorkReaderContract.END_MINUTE,
                        pref.getString(context.getString(R.string.SUNDAY_END_MINUTE), WorkReaderContract.END_MINUTE_DEFAULT));
        week.get(WorkReaderContract.SUNDAY)
                .add(WorkReaderContract.END_AM_OR_PM,
                        pref.getString(context.getString(R.string.SUNDAY_END_AM_OR_PM), WorkReaderContract.END_AM_OR_PM_DEFAULT));

        week.get(WorkReaderContract.MONDAY).add(0,
                pref.getString(context.getString(R.string.MONDAY_HOURS), context.getString(R.string.MONDAY_HOURS)));
        week.get(WorkReaderContract.MONDAY)
                .add(WorkReaderContract.START_HOUR,
                        pref.getString(context.getString(R.string.MONDAY_START_HOUR), WorkReaderContract.START_HOUR_DEFAULT));
        week.get(WorkReaderContract.MONDAY)
                .add(WorkReaderContract.START_MINUTE,
                        pref.getString(context.getString(R.string.MONDAY_START_MINUTE), WorkReaderContract.START_MINUTE_DEFAULT));
        week.get(WorkReaderContract.MONDAY)
                .add(WorkReaderContract.START_AM_OR_PM,
                        pref.getString(context.getString(R.string.MONDAY_START_AM_OR_PM), WorkReaderContract.START_AM_OR_PM_DEFAULT));
        week.get(WorkReaderContract.MONDAY)
                .add(WorkReaderContract.END_HOUR,
                        pref.getString(context.getString(R.string.MONDAY_END_HOUR), WorkReaderContract.END_HOUR_DEFAULT));
        week.get(WorkReaderContract.MONDAY)
                .add(WorkReaderContract.END_MINUTE,
                        pref.getString(context.getString(R.string.MONDAY_END_MINUTE), WorkReaderContract.END_MINUTE_DEFAULT));
        week.get(WorkReaderContract.MONDAY)
                .add(WorkReaderContract.END_AM_OR_PM,
                        pref.getString(context.getString(R.string.MONDAY_END_AM_OR_PM), WorkReaderContract.END_AM_OR_PM_DEFAULT));

        week.get(WorkReaderContract.TUESDAY).add(0,
                pref.getString(context.getString(R.string.TUESDAY_HOURS), context.getString(R.string.TUESDAY_HOURS)));
        week.get(WorkReaderContract.TUESDAY)
                .add(WorkReaderContract.START_HOUR,
                        pref.getString(context.getString(R.string.TUESDAY_START_HOUR), WorkReaderContract.START_HOUR_DEFAULT));
        week.get(WorkReaderContract.TUESDAY)
                .add(WorkReaderContract.START_MINUTE,
                        pref.getString(context.getString(R.string.TUESDAY_START_MINUTE), WorkReaderContract.START_MINUTE_DEFAULT));
        week.get(WorkReaderContract.TUESDAY)
                .add(WorkReaderContract.START_AM_OR_PM,
                        pref.getString(context.getString(R.string.TUESDAY_START_AM_OR_PM), WorkReaderContract.START_AM_OR_PM_DEFAULT));
        week.get(WorkReaderContract.TUESDAY)
                .add(WorkReaderContract.END_HOUR,
                        pref.getString(context.getString(R.string.TUESDAY_END_HOUR), WorkReaderContract.END_HOUR_DEFAULT));
        week.get(WorkReaderContract.TUESDAY)
                .add(WorkReaderContract.END_MINUTE,
                        pref.getString(context.getString(R.string.TUESDAY_END_MINUTE), WorkReaderContract.END_MINUTE_DEFAULT));
        week.get(WorkReaderContract.TUESDAY)
                .add(WorkReaderContract.END_AM_OR_PM,
                        pref.getString(context.getString(R.string.TUESDAY_END_AM_OR_PM), WorkReaderContract.END_AM_OR_PM_DEFAULT));


        week.get(WorkReaderContract.WEDNESDAY).add(0,
                pref.getString(context.getString(R.string.WEDNESDAY_HOURS), context.getString(R.string.WEDNESDAY_HOURS)));
        week.get(WorkReaderContract.WEDNESDAY)
                .add(WorkReaderContract.START_HOUR,
                        pref.getString(context.getString(R.string.WEDNESDAY_START_HOUR), WorkReaderContract.START_HOUR_DEFAULT));
        week.get(WorkReaderContract.WEDNESDAY)
                .add(WorkReaderContract.START_MINUTE,
                        pref.getString(context.getString(R.string.WEDNESDAY_START_MINUTE), WorkReaderContract.START_MINUTE_DEFAULT));
        week.get(WorkReaderContract.WEDNESDAY)
                .add(WorkReaderContract.START_AM_OR_PM,
                        pref.getString(context.getString(R.string.WEDNESDAY_START_AM_OR_PM), WorkReaderContract.START_AM_OR_PM_DEFAULT));
        week.get(WorkReaderContract.WEDNESDAY)
                .add(WorkReaderContract.END_HOUR,
                        pref.getString(context.getString(R.string.WEDNESDAY_END_HOUR), WorkReaderContract.END_HOUR_DEFAULT));
        week.get(WorkReaderContract.WEDNESDAY)
                .add(WorkReaderContract.END_MINUTE,
                        pref.getString(context.getString(R.string.WEDNESDAY_END_MINUTE), WorkReaderContract.END_MINUTE_DEFAULT));
        week.get(WorkReaderContract.WEDNESDAY)
                .add(WorkReaderContract.END_AM_OR_PM,
                        pref.getString(context.getString(R.string.WEDNESDAY_END_AM_OR_PM), WorkReaderContract.END_AM_OR_PM_DEFAULT));


        week.get(WorkReaderContract.THURSDAY).add(0,
                pref.getString(context.getString(R.string.THURSDAY_HOURS), context.getString(R.string.THURSDAY_HOURS)));
        week.get(WorkReaderContract.THURSDAY)
                .add(WorkReaderContract.START_HOUR,
                        pref.getString(context.getString(R.string.THURSDAY_START_HOUR), WorkReaderContract.START_HOUR_DEFAULT));
        week.get(WorkReaderContract.THURSDAY)
                .add(WorkReaderContract.START_MINUTE,
                        pref.getString(context.getString(R.string.THURSDAY_START_MINUTE), WorkReaderContract.START_MINUTE_DEFAULT));
        week.get(WorkReaderContract.THURSDAY)
                .add(WorkReaderContract.START_AM_OR_PM,
                        pref.getString(context.getString(R.string.THURSDAY_START_AM_OR_PM), WorkReaderContract.START_AM_OR_PM_DEFAULT));
        week.get(WorkReaderContract.THURSDAY)
                .add(WorkReaderContract.END_HOUR,
                        pref.getString(context.getString(R.string.THURSDAY_END_HOUR), WorkReaderContract.END_HOUR_DEFAULT));
        week.get(WorkReaderContract.THURSDAY)
                .add(WorkReaderContract.END_MINUTE,
                        pref.getString(context.getString(R.string.THURSDAY_END_MINUTE), WorkReaderContract.END_MINUTE_DEFAULT));
        week.get(WorkReaderContract.THURSDAY)
                .add(WorkReaderContract.END_AM_OR_PM,
                        pref.getString(context.getString(R.string.THURSDAY_END_AM_OR_PM), WorkReaderContract.END_AM_OR_PM_DEFAULT));


        week.get(WorkReaderContract.FRIDAY).add(0,
                pref.getString(context.getString(R.string.FRIDAY_HOURS), context.getString(R.string.FRIDAY_HOURS)));
        week.get(WorkReaderContract.FRIDAY)
                .add(WorkReaderContract.START_HOUR,
                        pref.getString(context.getString(R.string.FRIDAY_START_HOUR), WorkReaderContract.START_HOUR_DEFAULT));
        week.get(WorkReaderContract.FRIDAY)
                .add(WorkReaderContract.START_MINUTE,
                        pref.getString(context.getString(R.string.FRIDAY_START_MINUTE), WorkReaderContract.START_MINUTE_DEFAULT));
        week.get(WorkReaderContract.FRIDAY)
                .add(WorkReaderContract.START_AM_OR_PM,
                        pref.getString(context.getString(R.string.FRIDAY_START_AM_OR_PM), WorkReaderContract.START_AM_OR_PM_DEFAULT));
        week.get(WorkReaderContract.FRIDAY)
                .add(WorkReaderContract.END_HOUR,
                        pref.getString(context.getString(R.string.FRIDAY_END_HOUR), WorkReaderContract.END_HOUR_DEFAULT));
        week.get(WorkReaderContract.FRIDAY)
                .add(WorkReaderContract.END_MINUTE,
                        pref.getString(context.getString(R.string.FRIDAY_END_MINUTE), WorkReaderContract.END_MINUTE_DEFAULT));
        week.get(WorkReaderContract.FRIDAY)
                .add(WorkReaderContract.END_AM_OR_PM,
                        pref.getString(context.getString(R.string.FRIDAY_END_AM_OR_PM), WorkReaderContract.END_AM_OR_PM_DEFAULT));


        week.get(WorkReaderContract.SATURDAY).add(0,
                pref.getString(context.getString(R.string.SATURDAY_HOURS), context.getString(R.string.SATURDAY_HOURS)));
        week.get(WorkReaderContract.SATURDAY)
                .add(WorkReaderContract.START_HOUR,
                        pref.getString(context.getString(R.string.SATURDAY_START_HOUR), WorkReaderContract.START_HOUR_DEFAULT));
        week.get(WorkReaderContract.SATURDAY)
                .add(WorkReaderContract.START_MINUTE,
                        pref.getString(context.getString(R.string.SATURDAY_START_MINUTE), WorkReaderContract.START_MINUTE_DEFAULT));
        week.get(WorkReaderContract.SATURDAY)
                .add(WorkReaderContract.START_AM_OR_PM,
                        pref.getString(context.getString(R.string.SATURDAY_START_AM_OR_PM), WorkReaderContract.START_AM_OR_PM_DEFAULT));
        week.get(WorkReaderContract.SATURDAY)
                .add(WorkReaderContract.END_HOUR,
                        pref.getString(context.getString(R.string.SATURDAY_END_HOUR), WorkReaderContract.END_HOUR_DEFAULT));
        week.get(WorkReaderContract.SATURDAY)
                .add(WorkReaderContract.END_MINUTE,
                        pref.getString(context.getString(R.string.SATURDAY_END_MINUTE), WorkReaderContract.END_MINUTE_DEFAULT));
        week.get(WorkReaderContract.SATURDAY)
                .add(WorkReaderContract.END_AM_OR_PM,
                        pref.getString(context.getString(R.string.SATURDAY_END_AM_OR_PM), WorkReaderContract.END_AM_OR_PM_DEFAULT));

        //Start new week
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            final android.icu.util.Calendar cal = Calendar.getInstance();
            if (cal.get(android.icu.util.Calendar.DAY_OF_WEEK) == android.icu.util.Calendar.SUNDAY) {
                week.get(WorkReaderContract.OFF).add(0, "NEW_SUNDAY");
                week.get(WorkReaderContract.OFF).add(WorkReaderContract.START_HOUR, WorkReaderContract.START_HOUR_DEFAULT);
                week.get(WorkReaderContract.OFF).add(WorkReaderContract.START_MINUTE, WorkReaderContract.START_MINUTE_DEFAULT);
                week.get(WorkReaderContract.OFF).add(WorkReaderContract.START_AM_OR_PM, WorkReaderContract.START_AM_OR_PM_DEFAULT);
                week.get(WorkReaderContract.OFF).add(WorkReaderContract.END_HOUR, WorkReaderContract.END_HOUR_DEFAULT);
                week.get(WorkReaderContract.OFF).add(WorkReaderContract.END_MINUTE, WorkReaderContract.END_MINUTE_DEFAULT);
                week.get(WorkReaderContract.OFF).add(WorkReaderContract.END_AM_OR_PM, WorkReaderContract.END_AM_OR_PM_DEFAULT);
            }
        } else {
            final java.util.Calendar cal = java.util.Calendar.getInstance();
            if (cal.get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.SUNDAY) {
                week.get(WorkReaderContract.OFF).add("NEW_SUNDAY");
                week.get(WorkReaderContract.OFF).add(WorkReaderContract.START_HOUR, WorkReaderContract.START_HOUR_DEFAULT);
                week.get(WorkReaderContract.OFF).add(WorkReaderContract.START_MINUTE, WorkReaderContract.START_MINUTE_DEFAULT);
                week.get(WorkReaderContract.OFF).add(WorkReaderContract.START_AM_OR_PM, WorkReaderContract.START_AM_OR_PM_DEFAULT);
                week.get(WorkReaderContract.OFF).add(WorkReaderContract.END_HOUR, WorkReaderContract.END_HOUR_DEFAULT);
                week.get(WorkReaderContract.OFF).add(WorkReaderContract.END_MINUTE, WorkReaderContract.END_MINUTE_DEFAULT);
                week.get(WorkReaderContract.OFF).add(WorkReaderContract.END_AM_OR_PM, WorkReaderContract.END_AM_OR_PM_DEFAULT);
            }
        }

        return week;
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*WorkReaderContract.START_HOUR_DEFAULT = null;
        WorkReaderContract.START_MINUTE_DEFAULT = null;
        WorkReaderContract.START_AM_OR_PM_DEFAULT = null;
        WorkReaderContract.END_HOUR_DEFAULT = null;
        WorkReaderContract.END_MINUTE_DEFAULT = null;
        WorkReaderContract.END_AM_OR_PM_DEFAULT = null;
        */
    }
}//ned storeHoursInGUI

