package com.example.cd.workreminder;

import java.util.ArrayList;

public class storeHoursInGUI {

    //ArrayList week;
    ArrayList<ArrayList<String>> week;
    //Don't use constructor
    public void addHours() {
        week = new ArrayList<ArrayList<String>>();
        //week.clear(); //stupid hack. Don't ask.

        week.add(WorkReaderContract.WorkEntry.SUNDAY, new myArrayList());
        week.add(WorkReaderContract.WorkEntry.MONDAY, new ArrayList());
        week.add(WorkReaderContract.WorkEntry.TUESDAY, new ArrayList());
        week.add(WorkReaderContract.WorkEntry.WEDNESDAY, new ArrayList());
        week.add(WorkReaderContract.WorkEntry.THURSDAY, new ArrayList());
        week.add(WorkReaderContract.WorkEntry.FRIDAY, new ArrayList());
        week.add(WorkReaderContract.WorkEntry.SATURDAY, new ArrayList());
        week.add(WorkReaderContract.WorkEntry.OFF, new ArrayList());


        week.get(WorkReaderContract.WorkEntry.SUNDAY);
        /*week.get(WorkReaderContract.WorkEntry.SUNDAY).add(0,
                pref.getString(getString(R.string.SUNDAY), getString(R.string.SUNDAY)));
        week.get(WorkReaderContract.WorkEntry.SUNDAY)
                .add(WorkReaderContract.WorkEntry.START_HOUR,
                        pref.getString(getString(R.string.SUNDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.SUNDAY)
                .add(WorkReaderContract.WorkEntry.START_MINUTE,
                        pref.getString(getString(R.string.SUNDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.SUNDAY)
                .add(WorkReaderContract.WorkEntry.START_AM_OR_PM,
                        pref.getString(getString(R.string.SUNDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.SUNDAY)
                .add(WorkReaderContract.WorkEntry.END_HOUR,
                        pref.getString(getString(R.string.SUNDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.SUNDAY)
                .add(WorkReaderContract.WorkEntry.END_MINUTE,
                        pref.getString(getString(R.string.SUNDAY_END_MINUTE), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
        week.get(WorkReaderContract.WorkEntry.SUNDAY)
                .add(WorkReaderContract.WorkEntry.END_AM_OR_PM,
                        pref.getString(getString(R.string.SUNDAY_END_AM_OR_PM), WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));
        */

    }

    private class myArrayList extends ArrayList {
        @Override
        public String toString() {
            return "MIT MAGIC-COOKIE";
        }
    }
}//ned storeHoursInGUI

