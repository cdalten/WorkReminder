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

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class storeHoursInGUI {
    ArrayList<ArrayList<String>> week;
    SharedPreferences pref;
    Context context; //Added on 10 - 8 - 2019

    public storeHoursInGUI(Context context) {
        this.context = context;
    }

    @TargetApi(24)
    public ArrayList addHours() {
        pref = context.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        week = new ArrayList<ArrayList<String>>();
        //week.clear(); //stupid hack. Don't ask.

        week.add(WorkReaderContract.WorkEntry.SUNDAY, new ArrayList());
        week.add(WorkReaderContract.WorkEntry.MONDAY, new ArrayList());
        week.add(WorkReaderContract.WorkEntry.TUESDAY, new ArrayList());
        week.add(WorkReaderContract.WorkEntry.WEDNESDAY, new ArrayList());
        week.add(WorkReaderContract.WorkEntry.THURSDAY, new ArrayList());
        week.add(WorkReaderContract.WorkEntry.FRIDAY, new ArrayList());
        week.add(WorkReaderContract.WorkEntry.SATURDAY, new ArrayList());
        week.add(WorkReaderContract.WorkEntry.OFF, new ArrayList());


        week.get(WorkReaderContract.WorkEntry.SUNDAY);
        week.get(WorkReaderContract.WorkEntry.SUNDAY).add(0,
                pref.getString(context.getString(R.string.SUNDAY), context.getString(R.string.SUNDAY)));
        week.get(WorkReaderContract.WorkEntry.SUNDAY)
                .add(WorkReaderContract.WorkEntry.START_HOUR,
                        pref.getString(context.getString(R.string.SUNDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.SUNDAY)
                .add(WorkReaderContract.WorkEntry.START_MINUTE,
                        pref.getString(context.getString(R.string.SUNDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.SUNDAY)
                .add(WorkReaderContract.WorkEntry.START_AM_OR_PM,
                        pref.getString(context.getString(R.string.SUNDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.SUNDAY)
                .add(WorkReaderContract.WorkEntry.END_HOUR,
                        pref.getString(context.getString(R.string.SUNDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.SUNDAY)
                .add(WorkReaderContract.WorkEntry.END_MINUTE,
                        pref.getString(context.getString(R.string.SUNDAY_END_MINUTE), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.SUNDAY)
                .add(WorkReaderContract.WorkEntry.END_AM_OR_PM,
                        pref.getString(context.getString(R.string.SUNDAY_END_AM_OR_PM), WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));

        week.get(WorkReaderContract.WorkEntry.MONDAY).add(0,
                pref.getString(context.getString(R.string.MONDAY), context.getString(R.string.MONDAY)));
        week.get(WorkReaderContract.WorkEntry.MONDAY)
                .add(WorkReaderContract.WorkEntry.START_HOUR,
                        pref.getString(context.getString(R.string.MONDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.MONDAY)
                .add(WorkReaderContract.WorkEntry.START_MINUTE,
                        pref.getString(context.getString(R.string.MONDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.MONDAY)
                .add(WorkReaderContract.WorkEntry.START_AM_OR_PM,
                        pref.getString(context.getString(R.string.MONDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.MONDAY)
                .add(WorkReaderContract.WorkEntry.END_HOUR,
                        pref.getString(context.getString(R.string.MONDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.MONDAY)
                .add(WorkReaderContract.WorkEntry.END_MINUTE,
                        pref.getString(context.getString(R.string.MONDAY_END_MINUTE), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.MONDAY)
                .add(WorkReaderContract.WorkEntry.END_AM_OR_PM,
                        pref.getString(context.getString(R.string.MONDAY_END_AM_OR_PM), WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));

        week.get(WorkReaderContract.WorkEntry.TUESDAY).add(0,
                pref.getString(context.getString(R.string.TUESDAY), context.getString(R.string.TUESDAY)));
        week.get(WorkReaderContract.WorkEntry.TUESDAY)
                .add(WorkReaderContract.WorkEntry.START_HOUR,
                        pref.getString(context.getString(R.string.TUESDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.TUESDAY)
                .add(WorkReaderContract.WorkEntry.START_MINUTE,
                        pref.getString(context.getString(R.string.TUESDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.TUESDAY)
                .add(WorkReaderContract.WorkEntry.START_AM_OR_PM,
                        pref.getString(context.getString(R.string.TUESDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.TUESDAY)
                .add(WorkReaderContract.WorkEntry.END_HOUR,
                        pref.getString(context.getString(R.string.TUESDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.TUESDAY)
                .add(WorkReaderContract.WorkEntry.END_MINUTE,
                        pref.getString(context.getString(R.string.TUESDAY_END_MINUTE), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.TUESDAY)
                .add(WorkReaderContract.WorkEntry.END_AM_OR_PM,
                        pref.getString(context.getString(R.string.TUESDAY_END_AM_OR_PM), WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));


        week.get(WorkReaderContract.WorkEntry.WEDNESDAY).add(0,
                pref.getString(context.getString(R.string.WEDNESDAY), context.getString(R.string.WEDNESDAY)));
        week.get(WorkReaderContract.WorkEntry.WEDNESDAY)
                .add(WorkReaderContract.WorkEntry.START_HOUR,
                        pref.getString(context.getString(R.string.WEDNESDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.WEDNESDAY)
                .add(WorkReaderContract.WorkEntry.START_MINUTE,
                        pref.getString(context.getString(R.string.WEDNESDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.WEDNESDAY)
                .add(WorkReaderContract.WorkEntry.START_AM_OR_PM,
                        pref.getString(context.getString(R.string.WEDNESDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.WEDNESDAY)
                .add(WorkReaderContract.WorkEntry.END_HOUR,
                        pref.getString(context.getString(R.string.WEDNESDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.WEDNESDAY)
                .add(WorkReaderContract.WorkEntry.END_MINUTE,
                        pref.getString(context.getString(R.string.WEDNESDAY_END_MINUTE), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.WEDNESDAY)
                .add(WorkReaderContract.WorkEntry.END_AM_OR_PM,
                        pref.getString(context.getString(R.string.WEDNESDAY_END_AM_OR_PM), WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));


        week.get(WorkReaderContract.WorkEntry.THURSDAY).add(0,
                pref.getString(context.getString(R.string.THURSDAY), context.getString(R.string.THURSDAY)));
        week.get(WorkReaderContract.WorkEntry.THURSDAY)
                .add(WorkReaderContract.WorkEntry.START_HOUR,
                        pref.getString(context.getString(R.string.THURSDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.THURSDAY)
                .add(WorkReaderContract.WorkEntry.START_MINUTE,
                        pref.getString(context.getString(R.string.THURSDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.THURSDAY)
                .add(WorkReaderContract.WorkEntry.START_AM_OR_PM,
                        pref.getString(context.getString(R.string.THURSDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.THURSDAY)
                .add(WorkReaderContract.WorkEntry.END_HOUR,
                        pref.getString(context.getString(R.string.THURSDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.THURSDAY)
                .add(WorkReaderContract.WorkEntry.END_MINUTE,
                        pref.getString(context.getString(R.string.THURSDAY_END_MINUTE), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.THURSDAY)
                .add(WorkReaderContract.WorkEntry.END_AM_OR_PM,
                        pref.getString(context.getString(R.string.THURSDAY_END_AM_OR_PM), WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));


        week.get(WorkReaderContract.WorkEntry.FRIDAY).add(0,
                pref.getString(context.getString(R.string.FRIDAY), context.getString(R.string.FRIDAY)));
        week.get(WorkReaderContract.WorkEntry.FRIDAY)
                .add(WorkReaderContract.WorkEntry.START_HOUR,
                        pref.getString(context.getString(R.string.FRIDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.FRIDAY)
                .add(WorkReaderContract.WorkEntry.START_MINUTE,
                        pref.getString(context.getString(R.string.FRIDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.FRIDAY)
                .add(WorkReaderContract.WorkEntry.START_AM_OR_PM,
                        pref.getString(context.getString(R.string.FRIDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.FRIDAY)
                .add(WorkReaderContract.WorkEntry.END_HOUR,
                        pref.getString(context.getString(R.string.FRIDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.FRIDAY)
                .add(WorkReaderContract.WorkEntry.END_MINUTE,
                        pref.getString(context.getString(R.string.FRIDAY_END_MINUTE), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.FRIDAY)
                .add(WorkReaderContract.WorkEntry.END_AM_OR_PM,
                        pref.getString(context.getString(R.string.FRIDAY_END_AM_OR_PM), WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));


        week.get(WorkReaderContract.WorkEntry.SATURDAY).add(0,
                pref.getString(context.getString(R.string.SATURDAY), context.getString(R.string.SATURDAY)));
        week.get(WorkReaderContract.WorkEntry.SATURDAY)
                .add(WorkReaderContract.WorkEntry.START_HOUR,
                        pref.getString(context.getString(R.string.SATURDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.SATURDAY)
                .add(WorkReaderContract.WorkEntry.START_MINUTE,
                        pref.getString(context.getString(R.string.SATURDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.SATURDAY)
                .add(WorkReaderContract.WorkEntry.START_AM_OR_PM,
                        pref.getString(context.getString(R.string.SATURDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.SATURDAY)
                .add(WorkReaderContract.WorkEntry.END_HOUR,
                        pref.getString(context.getString(R.string.SATURDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.SATURDAY)
                .add(WorkReaderContract.WorkEntry.END_MINUTE,
                        pref.getString(context.getString(R.string.SATURDAY_END_MINUTE), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.SATURDAY)
                .add(WorkReaderContract.WorkEntry.END_AM_OR_PM,
                        pref.getString(context.getString(R.string.SATURDAY_END_AM_OR_PM), WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));

        //Start new week
        final Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.DAY_OF_WEEK) == java.util.Calendar.SUNDAY) {
            week.get(WorkReaderContract.WorkEntry.OFF).add(0, "NEW_SUNDAY");
            week.get(WorkReaderContract.WorkEntry.OFF).add(WorkReaderContract.WorkEntry.START_HOUR, WorkReaderContract.WorkEntry.START_HOUR_DEFAULT);
            week.get(WorkReaderContract.WorkEntry.OFF).add(WorkReaderContract.WorkEntry.START_MINUTE, WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT);
            week.get(WorkReaderContract.WorkEntry.OFF).add(WorkReaderContract.WorkEntry.START_AM_OR_PM, WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT);
            week.get(WorkReaderContract.WorkEntry.OFF).add(WorkReaderContract.WorkEntry.END_HOUR, WorkReaderContract.WorkEntry.END_HOUR_DEFAULT);
            week.get(WorkReaderContract.WorkEntry.OFF).add(WorkReaderContract.WorkEntry.END_MINUTE, WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT);
            week.get(WorkReaderContract.WorkEntry.OFF).add(WorkReaderContract.WorkEntry.END_AM_OR_PM, WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT);
        }

        return week;
    }


}//ned storeHoursInGUI

