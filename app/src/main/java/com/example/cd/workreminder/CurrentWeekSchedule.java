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
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CurrentWeekSchedule extends ListActivity  {

    private static final String PRODUCTION_TAG = "LG_WORK_PHONE";

    TextView text_day; //Added on 2 - 10 - 2019
    TextView text_start_hour; //Added on 2 - 10 -2019
    TextView text_separator; //Added on 3 - 19 - 2019
    TextView text_end_hour; //Added on 3 - 19 -2019

    private int currentPosition = 0; //Added on 1 - 20 - 2019
    static List<String> values; //Modified on 1 - 18 - 2019
    private WS adapter;

    ListView list;
    private Intent i;

    ArrayList<ArrayList<String>> week; //added on 2 - 24 - 2019
    private int currentDay = 0; //Added on 3 - 3 - 2019

    private String newStartDay; //Added on 3 - 12 - 2019
    private String newStartHour; //Added on 3 - 2 - 2019
    private String newStartMinute;
    private String newStartAmOrPm;
    private String newEndHour; //Added on 3 - 4 - 2019
    private String newEndMinute;
    private String newEndAmOrPm;

    private Button workSettings; //Added on 6 - 24 - 2019
    private Button logout; //Added on 7 - 2- 2019

    SharedPreferences pref;

    private AlarmManager alarmMgr; //Added on 8 - 4 - 2019
    private PendingIntent alarmIntent; //Added on 8 - 4 - 2019
    private int currentHours = 0; //Added on 10 - 17 - 2019

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Log.e(PRODUCTION_TAG, "CURRENT WEEK SCHEDULE GOT CALLED.");

        pref =  this.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);

        //Update = (Button) findViewById(R.id.Update);
        //WorkPrefences = (Button) findViewById(R.id.ferences);

        //Finish = (Button) findViewById(R.id.Finish);
        //list = (ListView) findViewById(android.R.id.list);

        dayNotification dayNotification = new dayNotification(this);
        currentHours = dayNotification.handleThirdShift();

        list = getListView();

        workSettings = (Button) findViewById(R.id.workSettings);
        logout = (Button) findViewById(R.id.logout);

        workSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(CurrentWeekSchedule.this, WorkPreferences.class), 0);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editPref = pref.edit();
                editPref.putBoolean("SAVE_PASSWORD", false);
                editPref.apply();
                startActivity(new Intent(CurrentWeekSchedule.this, Logout.class));
            }
        });
        //Send hours when I click on a particular day in the list view
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = (String)adapter.getItem(position);
                //values.add("FOO");

                //i = new Intent(CurrentWeekSchedule.this, HourFormat.class);
                currentDay = position; //Stupid hack
                currentPosition = position; //position on clicked list
                switch (position) {
                    case WorkReaderContract.WorkEntry.SUNDAY:
                        //currentPosition = 6; //handle integer wrap around case
                        //if (week.get(WorkReaderContract.WorkEntry.SUNDAY).get(0).equals("OFF")) {
                        fillListviewDropdown(
                                new Intent(CurrentWeekSchedule.this, DaySunday.class),
                                position,
                                R.string.SUNDAY,
                                R.string.SUNDAY_START_HOUR,
                                R.string.SUNDAY_START_MINUTE,
                                R.string.SUNDAY_START_AM_OR_PM,
                                R.string.SUNDAY_END_HOUR,
                                R.string.SUNDAY_END_MINUTE,
                                R.string.SUNDAY_END_AM_OR_PM
                        );
                        break;
                    case WorkReaderContract.WorkEntry.MONDAY:
                        //i = new Intent(CurrentWeekSchedule.this, DayMonday.class);
                        fillListviewDropdown(
                                new Intent(CurrentWeekSchedule.this, DayMonday.class),
                                position,
                                R.string.MONDAY,
                                R.string.MONDAY_START_HOUR,
                                R.string.MONDAY_START_MINUTE,
                                R.string.MONDAY_START_AM_OR_PM,
                                R.string.MONDAY_END_HOUR,
                                R.string.MONDAY_END_MINUTE,
                                R.string.MONDAY_END_AM_OR_PM
                        );
                        break;
                    case WorkReaderContract.WorkEntry.TUESDAY:
                        fillListviewDropdown(
                                new Intent(CurrentWeekSchedule.this, DayTuesday.class),
                                position,
                                R.string.TUESDAY,
                                R.string.TUESDAY_START_HOUR,
                                R.string.TUESDAY_START_MINUTE,
                                R.string.TUESDAY_START_AM_OR_PM,
                                R.string.TUESDAY_END_HOUR,
                                R.string.TUESDAY_END_MINUTE,
                                R.string.TUESDAY_END_AM_OR_PM
                        );
                        break;
                    case WorkReaderContract.WorkEntry.WEDNESDAY:
                        fillListviewDropdown(
                                new Intent(CurrentWeekSchedule.this, DayWednesday.class),
                                position,
                                R.string.WEDNESDAY,
                                R.string.WEDNESDAY_START_HOUR,
                                R.string.WEDNESDAY_START_MINUTE,
                                R.string.WEDNESDAY_START_AM_OR_PM,
                                R.string.WEDNESDAY_END_HOUR,
                                R.string.WEDNESDAY_END_MINUTE,
                                R.string.WEDNESDAY_END_AM_OR_PM
                        );
                        break;
                    case WorkReaderContract.WorkEntry.THURSDAY:
                        fillListviewDropdown(
                                new Intent(CurrentWeekSchedule.this, DayThursday.class),
                                position,
                                R.string.THURSDAY,
                                R.string.THURSDAY_START_HOUR,
                                R.string.THURSDAY_START_MINUTE,
                                R.string.THURSDAY_START_AM_OR_PM,
                                R.string.THURSDAY_END_HOUR,
                                R.string.THURSDAY_END_MINUTE,
                                R.string.THURSDAY_END_AM_OR_PM
                        );
                        break;
                    case WorkReaderContract.WorkEntry.FRIDAY:
                        fillListviewDropdown(
                                new Intent(CurrentWeekSchedule.this, DayFriday.class),
                                position,
                                R.string.FRIDAY,
                                R.string.FRIDAY_START_HOUR,
                                R.string.FRIDAY_START_MINUTE,
                                R.string.FRIDAY_START_AM_OR_PM,
                                R.string.FRIDAY_END_HOUR,
                                R.string.FRIDAY_END_MINUTE,
                                R.string.FRIDAY_END_AM_OR_PM
                        );
                        break;

                    case WorkReaderContract.WorkEntry.SATURDAY:
                        fillListviewDropdown(
                                new Intent(CurrentWeekSchedule.this, DaySaturday.class),
                                position,
                                R.string.SATURDAY,
                                R.string.SATURDAY_START_HOUR,
                                R.string.SATURDAY_START_MINUTE,
                                R.string.SATURDAY_START_AM_OR_PM,
                                R.string.SATURDAY_END_HOUR,
                                R.string.SATURDAY_END_MINUTE,
                                R.string.SATURDAY_END_AM_OR_PM
                        );
                        break;
                }//end

                //startActivityForResult(i, 0);
            }
        });


        storeHoursInGUI currentWeek = new storeHoursInGUI(this);
        //if (savedInstanceState == null) {
            week = currentWeek.addHours();

        //final Spinner spinner = (Spinner) findViewById(R.id.dayOfTheWeek);

        adapter = new WS(this,
                    R.layout.schedule_list, week);
            setListAdapter(adapter);

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        //spinner.setAdapter(adapter);
        //} else {
        //    week = currentWeek.addHours();
        //    adapter = new WS(this,
        //            R.layout.schedule_list, week);
        //    setListAdapter(adapter);
        //}

    } //end onCreate()

    //Added on 10 - 14 - 2019
    private void fillListviewDropdown(
                              Intent i,
                              int position,
                              int dayOfWeek,
                              int startHour,
                              int startMinute,
                              int startAmOrPm,
                              int endHour,
                              int endMinute,
                              int endAmOrPm)
    {
        //pref =  this.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);

        if ((pref.getString(getString(dayOfWeek), WorkReaderContract.WorkEntry.DAY_OFF_DEFAULT).equals(WorkReaderContract.WorkEntry.DAY_OFF_DEFAULT))) {
            i.putExtra("DAY_WEEK", position);
            i.putExtra("START_HOUR",  Integer.parseInt(WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
            i.putExtra("START_MINUTE",  Integer.parseInt(WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
            i.putExtra("START_AM_OR_PM",  WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT);

            i.putExtra("END_HOUR",  Integer.parseInt(WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
            i.putExtra("END_MINUTE",  Integer.parseInt(WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
            i.putExtra("END_AM_OR_PM",  WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT);
        } else {
            i.putExtra("DAY_WEEK", position);
            i.putExtra("START_HOUR", Integer.parseInt(pref.getString(getString(startHour),
                    WorkReaderContract.WorkEntry.START_HOUR_DEFAULT))
            );
            i.putExtra("START_MINUTE",
                    Integer.parseInt(pref.getString(getString(startMinute),
                            WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT)) / 15);
            if (pref.getString(getString(startAmOrPm),
                    WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT).equals("AM")) {
                i.putExtra("START_AM_OR_PM", 0);
            } else {
                i.putExtra("START_AM_OR_PM", 1);
            }

            i.putExtra("END_HOUR",
                    Integer.parseInt(pref.getString(getString(endHour),
                            WorkReaderContract.WorkEntry.END_HOUR_DEFAULT)));
            i.putExtra("END_MINUTE",
                    Integer.parseInt(pref.getString(getString(endMinute),
                            WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT)) / 15);
            if (pref.getString(getString(endAmOrPm),
                    WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT).equals("AM")) {
                i.putExtra("END_AM_OR_PM", 0);
            } else {
                i.putExtra("END_AM_OR_PM", 1);
            }
        }

        startActivityForResult(i, 0);

    }

    //Added on 10 - 16 - 2019
    private void saveDataOnRotation(Bundle outState,
                          String key,
                          int day,
                          int startHour,
                          int startMinute,
                          int startAmOrPM,
                          int endHour,
                          int endMinute,
                          int endAmOrPM)
    {
        outState.putString(key,  pref.getString(getString(day), getString(day)));
        outState.putString(getString(startHour),
                pref.getString(getString(startHour), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
        outState.putString(getString(startMinute),
                pref.getString(getString(startMinute), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
        outState.putString(getString(startAmOrPM),
                pref.getString(getString(startAmOrPM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
        outState.putString(getString(endHour),
                pref.getString(getString(endHour), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
        outState.putString(getString(endMinute),
                pref.getString(getString(endMinute), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
        outState.putString(getString(endAmOrPM),
                pref.getString(getString(endAmOrPM), WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));

    }

    //Added on 3 - 28 - 2019
    //Save data when screen changes from portrait to landscape
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        saveDataOnRotation(
                outState,
                "DAY_SUNDAY",
                R.string.SUNDAY,
                R.string.SUNDAY_START_HOUR,
                R.string.SUNDAY_START_MINUTE,
                R.string.SUNDAY_START_AM_OR_PM,
                R.string.SUNDAY_END_HOUR,
                R.string.SUNDAY_END_MINUTE,
                R.string.SUNDAY_END_AM_OR_PM
        );


        saveDataOnRotation(
                outState,
                "DAY_MONDAY",
                R.string.MONDAY,
                R.string.MONDAY_START_HOUR,
                R.string.MONDAY_START_MINUTE,
                R.string.MONDAY_START_AM_OR_PM,
                R.string.MONDAY_END_HOUR,
                R.string.MONDAY_END_MINUTE,
                R.string.MONDAY_END_AM_OR_PM
        );

        saveDataOnRotation(
                outState,
                "DAY_TUESDAY",
                R.string.TUESDAY,
                R.string.TUESDAY_START_HOUR,
                R.string.TUESDAY_START_MINUTE,
                R.string.TUESDAY_START_AM_OR_PM,
                R.string.TUESDAY_END_HOUR,
                R.string.TUESDAY_END_MINUTE,
                R.string.TUESDAY_END_AM_OR_PM
        );

        saveDataOnRotation(
                outState,
                "DAY_WEDNESDAY",
                R.string.WEDNESDAY,
                R.string.WEDNESDAY_START_HOUR,
                R.string.WEDNESDAY_START_MINUTE,
                R.string.WEDNESDAY_START_AM_OR_PM,
                R.string.WEDNESDAY_END_HOUR,
                R.string.WEDNESDAY_END_MINUTE,
                R.string.WEDNESDAY_END_AM_OR_PM
        );

        saveDataOnRotation(
                outState,
                "DAY_THURSDAY",
                R.string.THURSDAY,
                R.string.THURSDAY_START_HOUR,
                R.string.THURSDAY_START_MINUTE,
                R.string.THURSDAY_START_AM_OR_PM,
                R.string.THURSDAY_END_HOUR,
                R.string.THURSDAY_END_MINUTE,
                R.string.THURSDAY_END_AM_OR_PM
        );

        saveDataOnRotation(
                outState,
                "DAY_FRIDAY",
                R.string.FRIDAY,
                R.string.FRIDAY_START_HOUR,
                R.string.FRIDAY_START_MINUTE,
                R.string.FRIDAY_START_AM_OR_PM,
                R.string.FRIDAY_END_HOUR,
                R.string.FRIDAY_END_MINUTE,
                R.string.FRIDAY_END_AM_OR_PM
        );


        saveDataOnRotation(
                outState,
                "DAY_SATURDAY",
                R.string.SATURDAY,
                R.string.SATURDAY_START_HOUR,
                R.string.SATURDAY_START_MINUTE,
                R.string.SATURDAY_START_AM_OR_PM,
                R.string.SATURDAY_END_HOUR,
                R.string.SATURDAY_END_MINUTE,
                R.string.SATURDAY_END_AM_OR_PM
        );

        super.onSaveInstanceState(outState);
    }

    //I use list view because HTML fonts don't get preserved when the device changes orientation.
    private class WS<String> extends ArrayAdapter<java.lang.String> {
        private int hideItemPostion = 3; //3 is for debugging only

        WS(Context context, int resource, List<java.lang.String> Objects) {
            super(context, resource, Objects);
            //this.visibleMenuOptions = visibleMenuOptions;
        }

        public void setItemToHide(int itemToHide)
        {
            this.hideItemPostion =itemToHide;
        }

        @Override
        public int getCount() {
            //return 6;
            //return len;
            return week.size() - 1;
        }

        @Override
        public long getItemId(int position) {
            return week.get(position).hashCode();
        }

        @Override
        public java.lang.String getItem(int position) {
            return week.get(position).toString();
        }

        //Added on 6 - 24 - 201
        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            TextView tv = (TextView) view;

            if(position == 3){
                // Hide the second item from Spinner
                tv.setVisibility(View.GONE);
            }
            else {
                tv.setVisibility(View.VISIBLE);
            }
            return view;
        }

        @TargetApi(24)
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //super.getView(position, convertView, parent);
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.schedule_list, parent, false);
            }

            /*Display display;
            Point size;
            int width, height;
            float txtsize;

            display = getWindowManager().getDefaultDisplay();
            size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
            */

            //((TextView) view.findViewById(android.R.id.text1)).setText("-");
            // Need to erase values that trail "off" header byes??
            if (week.get(position).get(0).equals("SUNDAY")) {
                savePreviousSaturday();
            }

            //set list view to a blank line
            if (week.get(position).get(WorkReaderContract.WorkEntry.DAY_OF_WEEK).equals(WorkReaderContract.WorkEntry.DAY_OFF_DEFAULT)) {
                ((TextView) convertView.findViewById(R.id.dayOfWeek)).setText("");
                ((TextView) convertView.findViewById(R.id.startTime)).setText("");
                ((TextView) convertView.findViewById(R.id.hour_separator)).setText("");
                ((TextView) convertView.findViewById(R.id.endTime)).setText("");

            } else {
                //week.remove(position);
                //week.get(0).add(dayPair.get(position));
                ((TextView) convertView.findViewById(R.id.dayOfWeek))
                        .setText(week.get(position).get(WorkReaderContract.WorkEntry.DAY_OF_WEEK));
                ((TextView) convertView.findViewById(R.id.startTime))
                        .setText(week.get(position).get(WorkReaderContract.WorkEntry.START_HOUR)
                                + ":" + week.get(position).get(WorkReaderContract.WorkEntry.START_MINUTE) + " "
                                + week.get(position).get(WorkReaderContract.WorkEntry.START_AM_OR_PM));
                ((TextView) convertView.findViewById(R.id.hour_separator)).setText("to");
                ((TextView) convertView.findViewById(R.id.endTime))
                        .setText(week.get(position).get(WorkReaderContract.WorkEntry.END_HOUR) + ":"
                                + week.get(position).get(WorkReaderContract.WorkEntry.END_MINUTE) + " "
                                + week.get(position).get(WorkReaderContract.WorkEntry.END_AM_OR_PM));
            }

            text_start_hour = (TextView)convertView.findViewById(R.id.startTime);
            text_end_hour = (TextView)convertView.findViewById(R.id.endTime);
            //text_day = (TextView)convertView.findViewById(android.R.id.text1); //modified on 2 - 22 - 2019
            text_day = (TextView)convertView.findViewById(R.id.dayOfWeek);

            text_separator = (TextView)convertView.findViewById(R.id.hour_separator);

            //stupid hack
            text_start_hour.setMinHeight(0); // Min Height
            text_start_hour.setMinimumHeight(0); // Min Height
            text_start_hour.setHeight(120); // Height in pixels. Not dip?
            text_start_hour.setWidth(0);

            //text_separator.setWidth(20);

            text_end_hour.setMinHeight(0); // Min Height
            text_end_hour.setMinimumHeight(0); // Min Height
            text_end_hour.setHeight(120); // Height in pixels. Not dip?
            text_end_hour.setWidth(0);

            //text_day.setMinTimumHeight(0); // Min Height
            //text_day.setHeight(120); // Height in pixels. Not dip?


            if (currentHours == position + 1) {
                text_start_hour.setTypeface(text_start_hour.getTypeface(), Typeface.BOLD);  //vs null??
                text_separator.setTypeface(text_separator.getTypeface(), Typeface.BOLD);  //vs null??
                text_end_hour.setTypeface(text_end_hour.getTypeface(), Typeface.BOLD);  //vs null??
                text_day.setTypeface(text_day.getTypeface(), Typeface.BOLD);

            }

            //return super.getView(position, convertView, parent);
            return convertView;
        }//end method

    }//end inner class


    //Added on 6 - 23 - 2019
    //Get the previous Saturday.

    //Save previous Saturday and not the current one. Used to handle the start of the week.
    private void savePreviousSaturday() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("PREVIOUS_SATURDAY_START_HOUR", pref.getString(getString(R.string.SATURDAY_START_HOUR), ""));
        editor.putString("PREVIOUS_SATURDAY_START_MINUTE", pref.getString(getString(R.string.SATURDAY_START_MINUTE), ""));
        editor.putString("PREVIOUS_SATURDAY_START_AM_OR_PM", pref.getString(getString(R.string.SATURDAY_START_AM_OR_PM), ""));

        editor.apply();
    }
    //Added on 3 - 6 - 2019
    //Update new hours in the list view
    private void updateHours(String newStartDay, String newStartHour, String newStartMinute, String newStartAmOrPm,
                             String newEndHour, String newEndMinute, String newEndAmOrPm) {
        //int pos = list.getCheckedItemPosition();
        //week.add(currentPosition, getCurrentHours);

        ArrayList<String> updatedHour = new ArrayList<String>();
        updatedHour.clear();
        updatedHour.add(newStartDay);
        updatedHour.add(newStartHour);
        updatedHour.add(newStartMinute);
        updatedHour.add(newStartAmOrPm);
        updatedHour.add(newEndHour);
        updatedHour.add(newEndMinute);
        updatedHour.add(newEndAmOrPm);

        week.remove(currentPosition); //??
        week.add(currentPosition, updatedHour); //??

        //editor.putString(getString(R.string.com_example_cd_shiftreminder_SAVE_DAY), updatedHour);
        switch(currentPosition){
            case WorkReaderContract.WorkEntry.SUNDAY:
                saveDataToMemory(R.string.SUNDAY,
                        R.string.SUNDAY_START_HOUR,
                        R.string.SUNDAY_START_MINUTE,
                        R.string.SUNDAY_START_AM_OR_PM,
                        R.string.SUNDAY_END_HOUR,
                        R.string.SUNDAY_END_MINUTE,
                        R.string.SUNDAY_END_AM_OR_PM,
                        newStartDay,
                        newStartHour,
                        newStartMinute,
                        newStartAmOrPm,
                        newEndHour,
                        newEndMinute,
                        newEndAmOrPm);
                break;
            case WorkReaderContract.WorkEntry.MONDAY:
                saveDataToMemory(R.string.SATURDAY,
                        R.string.MONDAY_START_HOUR,
                        R.string.MONDAY_START_MINUTE,
                        R.string.MONDAY_START_AM_OR_PM,
                        R.string.MONDAY_END_HOUR,
                        R.string.MONDAY_END_MINUTE,
                        R.string.MONDAY_END_AM_OR_PM,
                        newStartDay,
                        newStartHour,
                        newStartMinute,
                        newStartAmOrPm,
                        newEndHour,
                        newEndMinute,
                        newEndAmOrPm);
                break;
            case WorkReaderContract.WorkEntry.TUESDAY:
                saveDataToMemory(R.string.TUESDAY,
                        R.string.TUESDAY_START_HOUR,
                        R.string.TUESDAY_START_MINUTE,
                        R.string.TUESDAY_START_AM_OR_PM,
                        R.string.TUESDAY_END_HOUR,
                        R.string.TUESDAY_END_MINUTE,
                        R.string.TUESDAY_END_AM_OR_PM,
                        newStartDay,
                        newStartHour,
                        newStartMinute,
                        newStartAmOrPm,
                        newEndHour,
                        newEndMinute,
                        newEndAmOrPm);
                break;
            case WorkReaderContract.WorkEntry.WEDNESDAY:
                saveDataToMemory(R.string.WEDNESDAY,
                        R.string.WEDNESDAY_START_HOUR,
                        R.string.WEDNESDAY_START_MINUTE,
                        R.string.WEDNESDAY_START_AM_OR_PM,
                        R.string.WEDNESDAY_END_HOUR,
                        R.string.WEDNESDAY_END_MINUTE,
                        R.string.WEDNESDAY_END_AM_OR_PM,
                        newStartDay,
                        newStartHour,
                        newStartMinute,
                        newStartAmOrPm,
                        newEndHour,
                        newEndMinute,
                        newEndAmOrPm);
                break;
            case WorkReaderContract.WorkEntry.THURSDAY:
                saveDataToMemory(R.string.THURSDAY,
                        R.string.THURSDAY_START_HOUR,
                        R.string.THURSDAY_START_MINUTE,
                        R.string.THURSDAY_START_AM_OR_PM,
                        R.string.THURSDAY_END_HOUR,
                        R.string.THURSDAY_END_MINUTE,
                        R.string.THURSDAY_END_AM_OR_PM,
                        newStartDay,
                        newStartHour,
                        newStartMinute,
                        newStartAmOrPm,
                        newEndHour,
                        newEndMinute,
                        newEndAmOrPm);
                break;
            case WorkReaderContract.WorkEntry.FRIDAY:
                saveDataToMemory(R.string.FRIDAY,
                        R.string.FRIDAY_START_HOUR,
                        R.string.FRIDAY_START_MINUTE,
                        R.string.FRIDAY_START_AM_OR_PM,
                        R.string.FRIDAY_END_HOUR,
                        R.string.FRIDAY_END_MINUTE,
                        R.string.FRIDAY_END_AM_OR_PM,
                        newStartDay,
                        newStartHour,
                        newStartMinute,
                        newStartAmOrPm,
                        newEndHour,
                        newEndMinute,
                        newEndAmOrPm);
                break;
            case WorkReaderContract.WorkEntry.SATURDAY:
                saveDataToMemory(R.string.SATURDAY,
                        R.string.SATURDAY_START_HOUR,
                        R.string.SATURDAY_START_MINUTE,
                        R.string.SATURDAY_START_AM_OR_PM,
                        R.string.SATURDAY_END_HOUR,
                        R.string.SATURDAY_END_MINUTE,
                        R.string.SATURDAY_END_AM_OR_PM,
                        newStartDay,
                        newStartHour,
                        newStartMinute,
                        newStartAmOrPm,
                        newEndHour,
                        newEndMinute,
                        newEndAmOrPm);
                break;
        }

        //adapter.remove("foo");
        //adapter.insert(updatedHour.toString() , currentPosition);
        adapter.notifyDataSetChanged();

    }

    //Added on 10 - 18 - 2017
    //Used by both updateHours() and after displayNotification() in onActivityResult()
    /*
      When I destroy the app, the new data will still stay in memory. However, when I reboot
      the device, the data is lost. Hence the following method corrects this behavior.
     */
    private void saveDataToMemory(
            int day,
            int startHour,
            int startMinute,
            int startAmOrPm,
            int endHour,
            int endMinute,
            int endAmOrPM,

            String dayOnOrOff,
            String newStartHour,
            String newStartMinute,
            String newStartAmOrPm,
            String newEndHour,
            String newEndMinute,
            String newEndAmOrPm

    )
    {
        SharedPreferences.Editor editor = pref.edit();

        if (!dayOnOrOff.equals(WorkReaderContract.WorkEntry.DAY_OFF_DEFAULT)) {
            editor.putString(getString(day), getString(day));
            editor.putString(getString(startHour), newStartHour);
            editor.putString(getString(startMinute), newStartMinute);
            editor.putString(getString(startAmOrPm), newStartAmOrPm);
            editor.putString(getString(endHour), newEndHour);
            editor.putString(getString(endMinute), newEndMinute);
            editor.putString(getString(endAmOrPM), newEndAmOrPm);
        } else {
            editor.putString(getString(day), WorkReaderContract.WorkEntry.DAY_OFF_DEFAULT);
            editor.putString(getString(startHour), newStartHour);
            editor.putString(getString(startMinute), newStartMinute);
            editor.putString(getString(startAmOrPm), newStartAmOrPm);
            editor.putString(getString(endHour), newEndHour);
            editor.putString(getString(endMinute), newEndMinute);
            editor.putString(getString(endAmOrPM), newEndAmOrPm);
        }

        editor.apply();
    }

    //Added on 11 - 2 - 2019
    @TargetApi(24)
    private void displayUpdatedAlarm() {
        dayNotification dayNotification = new dayNotification(this);
        long currentTime = dayNotification.getCurrentTime();
        long getNewAlarmTime = dayNotification.getNewAlarmTime();
        //dayNotification.setAlarm();

        if (getNewAlarmTime < currentTime) {

        } else {
            //display shit right away
        }
    }

    //Added on 2 - 27 - 2019
    @TargetApi(24)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e(PRODUCTION_TAG, "THE REQUEST CODE IS: " + requestCode);
        Log.e(PRODUCTION_TAG, "THE RESULT CODE IS: " + resultCode);

        int newPosition = -1; // don't enter switch
        String day = "";
        if (data != null) {
            newPosition = data.getIntExtra("CURRENT_DAY", -99);  //position in listview
            //0 - 6 represent Sun to Sat. 7 represents off. -99 is just to make it work on the hardware

            MilitaryTime militaryTime = MilitaryTime.getInstance();
            AlarmTimer alarmTimer = AlarmTimer.getInstance();
            dayNotification dayNotification = new dayNotification(getApplicationContext());

            if (resultCode == WorkReaderContract.WorkEntry.RESULT_OKAY_NO_WORK) {
                dayNotification.displayNotification("ALARM (ON ACTIVITY RESULT)");
            }

            else if (resultCode == WorkReaderContract.WorkEntry.RESULT_OK_WORK) {
                if (newPosition != -99 && newPosition != 7) {
                    newStartHour = data.getStringExtra(getString(R.string.START_HOUR)); //hardware bug??
                    newStartMinute = data.getStringExtra(getString(R.string.START_MINUTE));
                    newStartAmOrPm = data.getStringExtra(getString(R.string.START_AM_OR_PM));
                    newEndHour = data.getStringExtra(getString(R.string.END_HOUR));
                    newEndMinute = data.getStringExtra(getString(R.string.END_MINUTE));
                    newEndAmOrPm = data.getStringExtra(getString(R.string.END_AM_OR_PM));
                    day = data.getStringExtra(getString(R.string.DAY_OF_WEEK));

                    militaryTime.convertStartCivilianTimeToMilitaryTime(newStartHour, newStartMinute, newStartAmOrPm);
                    militaryTime.convertEndCivilianTimeToMilitaryTime(newEndHour, newEndMinute, newEndAmOrPm);

                    //----DUPLICATE??? ------------------------------------------------------------
                    alarmTimer.setAlarmTime(this,
                            day,
                            militaryTime.getStartMilitaryHour(),
                            militaryTime.getStartMilitaryMinute());

                    //long startTime = dayNotification.convertToStartTime(militaryTime.getStartMilitaryHour(), militaryTime.getStartMilitaryMinute());
                    //long endTime = dayNotification.convertToEndTime(militaryTime.getEndMilitaryHour(), militaryTime.getEndMilitaryMinute());
                    //long currentTime = dayNotification.getCurrentTime();
                    //dayNotification.setNewNotificationDisplay(day, startTime, endTime, currentTime);

                    /*week.get(currentHours - 1).add(newStartHour);
                    week.get(currentHours - 1).add(newStartMinute);
                    week.get(currentHours - 1).add(newStartAmOrPm);
                    week.get(currentHours - 1).add(newEndHour);
                    week.get(currentHours - 1).add(newEndMinute);
                    week.get(currentHours - 1).add(newStartAmOrPm);
                    */

                    dayNotification.displayNotification(alarmTimer, "ALARM (ON ACTIVITY RESULT)");
                    //------------------------------------------------------------------------


                } else {
                    newStartHour = week.get(currentHours - 1).get(WorkReaderContract.WorkEntry.START_HOUR);
                    newStartMinute = week.get(currentHours - 1).get(WorkReaderContract.WorkEntry.START_MINUTE);
                    newStartAmOrPm = week.get(currentHours - 1).get(WorkReaderContract.WorkEntry.START_AM_OR_PM);
                    newEndHour = week.get(currentHours - 1).get(WorkReaderContract.WorkEntry.END_HOUR);
                    newEndMinute = week.get(currentHours - 1).get(WorkReaderContract.WorkEntry.END_MINUTE);
                    newEndAmOrPm = week.get(currentHours - 1).get(WorkReaderContract.WorkEntry.END_AM_OR_PM);
                    day = week.get(currentHours - 1).get(0);

                    militaryTime.convertStartCivilianTimeToMilitaryTime(newStartHour, newStartMinute, newStartAmOrPm);
                    militaryTime.convertEndCivilianTimeToMilitaryTime(newEndHour, newEndMinute, newEndAmOrPm);
                    alarmTimer.setAlarmTime(this,
                            day,
                            militaryTime.getStartMilitaryHour(),
                            militaryTime.getStartMilitaryMinute());

                    dayNotification.displayNotification(alarmTimer, "ALARM (ON ACTIVITY RESULT)");
                    //save current schedule in shared pref?
                }
            }
        }
        //editor.apply();
        //newStartDay = week.get(weekPosition).get(0); //BUG  -- DEFAULTS TO SUNDAY
        //newStartDay = week.get(newPosition).get(0);
        //data.getStringExtra(getString(R.string.DAY_OF_WEEK)); //CORRECTED VERSION/

        //Yes I know this sucks.
            switch (newPosition) {
                case WorkReaderContract.WorkEntry.SUNDAY:
                    updateHours(
                            //pref.getString(getString(R.string.SUNDAY),getString(R.string.SUNDAY)),
                            "SUNDAY",
                            pref.getString(getString(R.string.SUNDAY_START_HOUR),
                                    WorkReaderContract.WorkEntry.START_HOUR_DEFAULT),
                            pref.getString(getString(R.string.SUNDAY_START_MINUTE),
                                    WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.SUNDAY_START_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT),
                            pref.getString(getString(R.string.SUNDAY_END_HOUR),
                                    WorkReaderContract.WorkEntry.END_HOUR_DEFAULT),
                            pref.getString(getString(R.string.SUNDAY_END_MINUTE),
                                    WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.SUNDAY_END_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));
                    break;
                case WorkReaderContract.WorkEntry.MONDAY:
                    updateHours(
                            //pref.getString(getString(R.string.MONDAY),getString(R.string.MONDAY)),
                            "MONDAY",
                            pref.getString(getString(R.string.MONDAY_START_HOUR),
                                    WorkReaderContract.WorkEntry.START_HOUR_DEFAULT),
                            pref.getString(getString(R.string.MONDAY_START_MINUTE),
                                    WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.MONDAY_START_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT),
                            pref.getString(getString(R.string.MONDAY_END_HOUR),
                                    WorkReaderContract.WorkEntry.END_HOUR_DEFAULT),
                            pref.getString(getString(R.string.MONDAY_END_MINUTE),
                                    WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.MONDAY_END_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));
                    break;
                case WorkReaderContract.WorkEntry.TUESDAY:
                    updateHours(
                            //pref.getString(getString(R.string.TUESDAY),getString(R.string.TUESDAY)),
                            "TUESDAY",
                            pref.getString(getString(R.string.TUESDAY_START_HOUR),
                                    WorkReaderContract.WorkEntry.START_HOUR_DEFAULT),
                            pref.getString(getString(R.string.TUESDAY_START_MINUTE),
                                    WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.TUESDAY_START_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT),
                            pref.getString(getString(R.string.TUESDAY_END_HOUR),
                                    WorkReaderContract.WorkEntry.END_HOUR_DEFAULT),
                            pref.getString(getString(R.string.TUESDAY_END_MINUTE),
                                    WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.TUESDAY_END_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));
                    break;
                case WorkReaderContract.WorkEntry.WEDNESDAY:
                    updateHours(
                            //pref.getString(getString(R.string.WEDNESDAY),getString(R.string.WEDNESDAY)),
                            "WEDNESDAY",
                            pref.getString(getString(R.string.WEDNESDAY_START_HOUR),
                                    WorkReaderContract.WorkEntry.START_HOUR_DEFAULT),
                            pref.getString(getString(R.string.WEDNESDAY_START_MINUTE),
                                    WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.WEDNESDAY_START_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT),
                            pref.getString(getString(R.string.WEDNESDAY_END_HOUR),
                                    WorkReaderContract.WorkEntry.END_HOUR_DEFAULT),
                            pref.getString(getString(R.string.WEDNESDAY_END_MINUTE),
                                    WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.WEDNESDAY_END_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));
                    break;
                case WorkReaderContract.WorkEntry.THURSDAY:
                    updateHours(
                            //pref.getString(getString(R.string.THURSDAY),getString(R.string.THURSDAY)),
                            "THURSDAY",
                            pref.getString(getString(R.string.THURSDAY_START_HOUR),
                                    WorkReaderContract.WorkEntry.START_HOUR_DEFAULT),
                            pref.getString(getString(R.string.THURSDAY_START_MINUTE),
                                    WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.THURSDAY_START_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT),
                            pref.getString(getString(R.string.THURSDAY_END_HOUR),
                                    WorkReaderContract.WorkEntry.END_HOUR_DEFAULT),
                            pref.getString(getString(R.string.THURSDAY_END_MINUTE),
                                    WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.THURSDAY_END_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));
                    break;
                case WorkReaderContract.WorkEntry.FRIDAY:
                    updateHours(
                            //pref.getString(getString(R.string.FRIDAY),getString(R.string.FRIDAY)),
                            "FRIDAY",
                            pref.getString(getString(R.string.FRIDAY_START_HOUR),
                                    WorkReaderContract.WorkEntry.START_HOUR_DEFAULT),
                            pref.getString(getString(R.string.FRIDAY_START_MINUTE),
                                    WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.FRIDAY_START_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT),
                            pref.getString(getString(R.string.FRIDAY_END_HOUR),
                                    WorkReaderContract.WorkEntry.END_HOUR_DEFAULT),
                            pref.getString(getString(R.string.FRIDAY_END_MINUTE),
                                    WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.FRIDAY_END_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));
                    break;
                case WorkReaderContract.WorkEntry.SATURDAY:
                    updateHours(
                            //pref.getString(getString(R.string.SATURDAY),getString(R.string.SATURDAY)),
                            "SATURDAY",
                            pref.getString(getString(R.string.SATURDAY_START_HOUR),
                                    WorkReaderContract.WorkEntry.START_HOUR_DEFAULT),
                            pref.getString(getString(R.string.SATURDAY_START_MINUTE),
                                    WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.SATURDAY_START_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT),
                            pref.getString(getString(R.string.SATURDAY_END_HOUR),
                                    WorkReaderContract.WorkEntry.END_HOUR_DEFAULT),
                            pref.getString(getString(R.string.SATURDAY_END_MINUTE),
                                    WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.SATURDAY_END_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT));
                    break;
                case 7:
                    updateHours(WorkReaderContract.WorkEntry.DAY_OFF_DEFAULT,
                            //pref.getString(getString(R.string.OFF),
                            //        WorkReaderContract.WorkEntry.START_HOUR_DEFAULT),
                            WorkReaderContract.WorkEntry.START_HOUR_DEFAULT,
                            //newWorkHours.getStartMinute(),
                            WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT,
                            //newWorkHours.getStartAmOrPm(),
                            WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT,
                            //newWorkHours.getEndHour(),
                            WorkReaderContract.WorkEntry.END_HOUR_DEFAULT,
                            //newWorkHours.getEndMinute(),
                            WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT,
                            //newWorkHours.getEndAmOrPm()
                            WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT);
            }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}//End class
