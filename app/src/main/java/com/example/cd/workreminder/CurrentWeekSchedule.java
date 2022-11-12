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
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.ListActivity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CurrentWeekSchedule extends ListActivity {

    private final String PRODUCTION_TAG = "CURRENT_WEEK_SCHEDULE";

    TextView text_day; //Added on 2 - 10 - 2019
    TextView text_start_hour; //Added on 2 - 10 -2019
    TextView text_separator; //Added on 3 - 19 - 2019
    TextView text_end_hour; //Added on 3 - 19 -2019

    private int currentPosition = 0; //Added on 1 - 20 - 2019
    static List<String> values; //Modified on 1 - 18 - 2019
    private WS adapter;

    ListView list;

    ArrayList<ArrayList<String>> week; //added on 2 - 24 - 2019
    private int currentDay = 0; //Added on 3 - 3 - 2019
    private String day = ""; //Added on 12 - 17 - 2019
    private String newStartHour; //Added on 3 - 2 - 2019
    private String newStartMinute;
    private String newStartAmOrPm;
    private String newEndHour; //Added on 3 - 4 - 2019
    private String newEndMinute;
    private String newEndAmOrPm;

    private Button workSettings; //Added on 6 - 24 - 2019
    public static boolean amEnabled = false; // Added on 10 - 29 - 2022
    SharedPreferences pref;
    private Intent closedIntent;

    private int currentHours = 0; //Added on 10 - 17 - 2019


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.login);
        //ListActivity listActivity = new ListActivity();

        setContentView(R.layout.login);
        Log.d(PRODUCTION_TAG, "CURRENT WEEK SCHEDULE GOT CALLED.");
        enableBootReceiver();
        enableWorkNotificationReceiver();

        pref =  this.getSharedPreferences("BECAUSE_INTENTS_SUCK_MASSIVE_DICK", MODE_PRIVATE);

        //Update = (Button) findViewById(R.id.Update);


        final SetAlarm SetAlarm = new SetAlarm(this);
        currentHours = SetAlarm.handleThirdShift();

        workSettings = (Button) findViewById(R.id.workSettings);
        //logout = (Button) findViewById(R.id.logout);

        workSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent workPreferencesIntent = new Intent(CurrentWeekSchedule.this, WorkPreferences.class);
                if (workPreferencesIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(workPreferencesIntent, 0);
                }
            }
        });

        /*logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editPref = pref.edit();
                editPref.putBoolean("SAVE_PASSWORD", false);
                editPref.apply();
                startActivity(new Intent(CurrentWeekSchedule.this, ogout.class));
            }
        });
        */
        //Send hours when I click on a particular day in the list view
        list = getListView();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentDay = position; //Stupid hack
                currentPosition = position; //position on clicked list
                switch (position) {
                    case WorkReaderContract.SUNDAY:
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
                    case WorkReaderContract.MONDAY:
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
                    case WorkReaderContract.TUESDAY:
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
                    case WorkReaderContract.WEDNESDAY:
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
                    case WorkReaderContract.THURSDAY:
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
                    case WorkReaderContract.FRIDAY:
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

                    case WorkReaderContract.SATURDAY:
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
            }
        });


        storeHoursInGUI currentWeek = new storeHoursInGUI(this);
        week = currentWeek.addHours();

        adapter = new WS(this,
                    R.layout.schedule_list, week);
            setListAdapter(adapter);

    } //end onCreate()


    //Added on 2 - 28 - 2020
    private void enableBootReceiver() {
        ComponentName receiver = new ComponentName(this, BootReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }


    private void enableWorkNotificationReceiver() {
        ComponentName receiver = new ComponentName(this, WorkNotificationReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }


    //Added on 10 - 14 - 2019
    private void fillListviewDropdown(
                              Intent dayIntent,
                              int position,
                              int dayOfWeek,
                              int startHour,
                              int startMinute,
                              int startAmOrPm,
                              int endHour,
                              int endMinute,
                              int endAmOrPm)
    {
        if ((pref.getString(getString(dayOfWeek), WorkReaderContract.DAY_OFF_DEFAULT).equals(WorkReaderContract.DAY_OFF_DEFAULT))) {
            dayIntent.putExtra("DAY_WEEK", position);
            dayIntent.putExtra("START_HOUR",  Integer.parseInt(WorkReaderContract.START_HOUR_DEFAULT));
            dayIntent.putExtra("START_MINUTE",  Integer.parseInt(WorkReaderContract.START_MINUTE_DEFAULT));
            dayIntent.putExtra("START_AM_OR_PM",  WorkReaderContract.START_AM_OR_PM_DEFAULT);

            dayIntent.putExtra("END_HOUR",  Integer.parseInt(WorkReaderContract.END_HOUR_DEFAULT));
            dayIntent.putExtra("END_MINUTE",  Integer.parseInt(WorkReaderContract.END_MINUTE_DEFAULT));
            dayIntent.putExtra("END_AM_OR_PM",  WorkReaderContract.END_AM_OR_PM_DEFAULT);
        } else {
            dayIntent.putExtra("DAY_WEEK", position);
            dayIntent.putExtra("START_HOUR", Integer.parseInt(pref.getString(getString(startHour),
                    WorkReaderContract.START_HOUR_DEFAULT))
            );
            dayIntent.putExtra("START_MINUTE",
                    Integer.parseInt(pref.getString(getString(startMinute),
                            WorkReaderContract.START_MINUTE_DEFAULT)) / 15);
            if (pref.getString(getString(startAmOrPm),
                    WorkReaderContract.START_AM_OR_PM_DEFAULT).equals("AM")) {
                dayIntent.putExtra("START_AM_OR_PM", 0);
            } else {
                dayIntent.putExtra("START_AM_OR_PM", 1);
            }

            dayIntent.putExtra("END_HOUR",
                    Integer.parseInt(pref.getString(getString(endHour),
                            WorkReaderContract.END_HOUR_DEFAULT)));
            dayIntent.putExtra("END_MINUTE",
                    Integer.parseInt(pref.getString(getString(endMinute),
                            WorkReaderContract.END_MINUTE_DEFAULT)) / 15);
            if (pref.getString(getString(endAmOrPm),
                    WorkReaderContract.END_AM_OR_PM_DEFAULT).equals("AM")) {
                dayIntent.putExtra("END_AM_OR_PM", 0);
            } else {
                dayIntent.putExtra("END_AM_OR_PM", 1);
            }
        }

        if (dayIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(dayIntent, 0);
        }

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
        DataToMemory dataToMemory = new DataToMemory(getApplicationContext());
        dataToMemory.saveDataOnRotation(outState, key, day, startHour, startMinute, startAmOrPM,
                endHour, endMinute, endAmOrPM
                );

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
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //super.getView(position, convertView, parent);
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.schedule_list, parent, false);
            }
            // Need to erase values that trail "off" header byes??
            if (week.get(position).get(0).equals("SUNDAY")) {
                savePreviousSaturday();
            }

            //set list view to a blank line
            if (week.get(position).get(WorkReaderContract.DAY_OF_WEEK).equals(WorkReaderContract.DAY_OFF_DEFAULT)) {
                ((TextView) convertView.findViewById(R.id.dayOfWeek)).setText("");
                ((TextView) convertView.findViewById(R.id.startTime)).setText("");
                ((TextView) convertView.findViewById(R.id.hour_separator)).setText("");
                ((TextView) convertView.findViewById(R.id.endTime)).setText("");

            } else {
                //week.remove(position);
                //week.get(0).add(dayPair.get(position));
                ((TextView) convertView.findViewById(R.id.dayOfWeek))
                        .setText(week.get(position).get(WorkReaderContract.DAY_OF_WEEK));
                ((TextView) convertView.findViewById(R.id.startTime))
                        .setText(week.get(position).get(WorkReaderContract.START_HOUR)
                                + ":" + week.get(position).get(WorkReaderContract.START_MINUTE) + " "
                                + week.get(position).get(WorkReaderContract.START_AM_OR_PM));
                ((TextView) convertView.findViewById(R.id.hour_separator)).setText("to");
                ((TextView) convertView.findViewById(R.id.endTime))
                        .setText(week.get(position).get(WorkReaderContract.END_HOUR) + ":"
                                + week.get(position).get(WorkReaderContract.END_MINUTE) + " "
                                + week.get(position).get(WorkReaderContract.END_AM_OR_PM));
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


            if (currentHours == position) {
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        //builder.setMessage("ARE YOU SURE YOU WANT TO EXIT?");
        builder.setPositiveButton("", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                //if (savedInstanceState f null) {
                android.os.Process.killProcess(android.os.Process.myPid());
                //finish();
                //finishAffinity();

                //System.exit(0);
            }

        });
        }
//Added on 3 - 6 - 2019
//Update new hours in the list view
private void updateHours(String newStartDay, String newStartHour, String newStartMinute, String newStartAmOrPm,
        String newEndHour, String newEndMinute, String newEndAmOrPm) {
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

        switch(currentPosition){
            case WorkReaderContract.SUNDAY:
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
            case WorkReaderContract.MONDAY:
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
            case WorkReaderContract.TUESDAY:
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
            case WorkReaderContract.WEDNESDAY:
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
            case WorkReaderContract.THURSDAY:
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
            case WorkReaderContract.FRIDAY:
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
            case WorkReaderContract.SATURDAY:
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
        DataToMemory dataToMemory = new DataToMemory(getApplicationContext());
        dataToMemory.saveDataToMemory(
         day, startHour, startMinute, startAmOrPm, endHour, endMinute, endAmOrPM,
                dayOnOrOff, newStartHour, newStartMinute, newStartAmOrPm, newEndHour,
                newEndMinute, newEndAmOrPm
        );

    }

    //Added on 2 - 27 - 2019
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e(PRODUCTION_TAG, "THE REQUEST CODE IS: " + requestCode);
        Log.e(PRODUCTION_TAG, "THE RESULT CODE IS: " + resultCode);

        int newPosition = -1; // don't enter switch
        final AlarmTimer alarmTimer = AlarmTimer.getInstance();


        //String day = "";
        final MilitaryTime militaryTime = MilitaryTime.getInstance();
        if (data != null) {
            newPosition = data.getIntExtra("CURRENT_DAY", -99);  //position in listview
            //0 - 6 represent Sun to Sat. 7 represents off. -99 is just to make it work on the hardware
            if (resultCode == WorkReaderContract.RESULT_OKAY_NO_WORK) {
                //copied from Stackoverflow
                try{
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancelAll();
                }catch (Exception e){
                    e.printStackTrace();
                }
                Log.d(PRODUCTION_TAG, "ALARM CAN'T BE SET");
            }

            if (resultCode == WorkReaderContract.RESULT_OK_WORK) {
                newStartHour = data.getStringExtra("START_HOUR"); //hardware bug??
                newStartMinute = data.getStringExtra("START_MINUTE");
                newStartAmOrPm = data.getStringExtra("START_AM_OR_PM");
                newEndHour = data.getStringExtra("END_HOUR");
                newEndMinute = data.getStringExtra("END_MINUTE");
                newEndAmOrPm = data.getStringExtra("END_AM_OR_PM");
                day = data.getStringExtra("DAY_WEEK");

                militaryTime.convertStartCivilianTimeToMilitaryTime(newStartHour, newStartMinute, newStartAmOrPm);
                militaryTime.convertEndCivilianTimeToMilitaryTime(newEndHour, newEndMinute, newEndAmOrPm);

                SetAlarm SetAlarm = new SetAlarm(getApplicationContext());
                SetAlarm.setNotificationDisplay(getApplicationContext(), militaryTime);


            } else if (resultCode == WorkReaderContract.RESULT_OKAY_UPDATE_WORK_ALARM_TIME) {
                /*
                  I don't get the current time because I assume when a person sets the alarm to
                  something like 20 minutes before their shift, they mean for day in question, and not
                  say, three shifts from now.
                 */
                SetAlarm SetAlarm = new SetAlarm(
                        getApplicationContext());
                currentHours = SetAlarm.handleThirdShift();
            }

        }

        //Yes I know this sucks.
            switch (newPosition) {
                case WorkReaderContract.SUNDAY:
                    updateHours(
                            "SUNDAY",
                            pref.getString(getString(R.string.SUNDAY_START_HOUR),
                                    WorkReaderContract.START_HOUR_DEFAULT),
                            pref.getString(getString(R.string.SUNDAY_START_MINUTE),
                                    WorkReaderContract.START_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.SUNDAY_START_AM_OR_PM),
                                    WorkReaderContract.START_AM_OR_PM_DEFAULT),
                            pref.getString(getString(R.string.SUNDAY_END_HOUR),
                                    WorkReaderContract.END_HOUR_DEFAULT),
                            pref.getString(getString(R.string.SUNDAY_END_MINUTE),
                                    WorkReaderContract.END_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.SUNDAY_END_AM_OR_PM),
                                    WorkReaderContract.END_AM_OR_PM_DEFAULT));
                    break;
                case WorkReaderContract.MONDAY:
                    updateHours(
                            "MONDAY",
                            pref.getString(getString(R.string.MONDAY_START_HOUR),
                                    WorkReaderContract.START_HOUR_DEFAULT),
                            pref.getString(getString(R.string.MONDAY_START_MINUTE),
                                    WorkReaderContract.START_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.MONDAY_START_AM_OR_PM),
                                    WorkReaderContract.START_AM_OR_PM_DEFAULT),
                            pref.getString(getString(R.string.MONDAY_END_HOUR),
                                    WorkReaderContract.END_HOUR_DEFAULT),
                            pref.getString(getString(R.string.MONDAY_END_MINUTE),
                                    WorkReaderContract.END_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.MONDAY_END_AM_OR_PM),
                                    WorkReaderContract.END_AM_OR_PM_DEFAULT));
                    break;
                case WorkReaderContract.TUESDAY:
                    updateHours(
                            "TUESDAY",
                            pref.getString(getString(R.string.TUESDAY_START_HOUR),
                                    WorkReaderContract.START_HOUR_DEFAULT),
                            pref.getString(getString(R.string.TUESDAY_START_MINUTE),
                                    WorkReaderContract.START_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.TUESDAY_START_AM_OR_PM),
                                    WorkReaderContract.START_AM_OR_PM_DEFAULT),
                            pref.getString(getString(R.string.TUESDAY_END_HOUR),
                                    WorkReaderContract.END_HOUR_DEFAULT),
                            pref.getString(getString(R.string.TUESDAY_END_MINUTE),
                                    WorkReaderContract.END_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.TUESDAY_END_AM_OR_PM),
                                    WorkReaderContract.END_AM_OR_PM_DEFAULT));
                    break;
                case WorkReaderContract.WEDNESDAY:
                    updateHours(
                            //pref.getString(getString(R.string.WEDNESDAY),getString(R.string.WEDNESDAY)),
                            "WEDNESDAY",
                            pref.getString(getString(R.string.WEDNESDAY_START_HOUR),
                                    WorkReaderContract.START_HOUR_DEFAULT),
                            pref.getString(getString(R.string.WEDNESDAY_START_MINUTE),
                                    WorkReaderContract.START_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.WEDNESDAY_START_AM_OR_PM),
                                    WorkReaderContract.START_AM_OR_PM_DEFAULT),
                            pref.getString(getString(R.string.WEDNESDAY_END_HOUR),
                                    WorkReaderContract.END_HOUR_DEFAULT),
                            pref.getString(getString(R.string.WEDNESDAY_END_MINUTE),
                                    WorkReaderContract.END_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.WEDNESDAY_END_AM_OR_PM),
                                    WorkReaderContract.END_AM_OR_PM_DEFAULT));
                    break;
                case WorkReaderContract.THURSDAY:
                    updateHours(
                            "THURSDAY",
                            pref.getString(getString(R.string.THURSDAY_START_HOUR),
                                    WorkReaderContract.START_HOUR_DEFAULT),
                            pref.getString(getString(R.string.THURSDAY_START_MINUTE),
                                    WorkReaderContract.START_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.THURSDAY_START_AM_OR_PM),
                                    WorkReaderContract.START_AM_OR_PM_DEFAULT),
                            pref.getString(getString(R.string.THURSDAY_END_HOUR),
                                    WorkReaderContract.END_HOUR_DEFAULT),
                            pref.getString(getString(R.string.THURSDAY_END_MINUTE),
                                    WorkReaderContract.END_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.THURSDAY_END_AM_OR_PM),
                                    WorkReaderContract.END_AM_OR_PM_DEFAULT));
                    break;
                case WorkReaderContract.FRIDAY:
                    updateHours(
                            "FRIDAY",
                            pref.getString(getString(R.string.FRIDAY_START_HOUR),
                                    WorkReaderContract.START_HOUR_DEFAULT),
                            pref.getString(getString(R.string.FRIDAY_START_MINUTE),
                                    WorkReaderContract.START_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.FRIDAY_START_AM_OR_PM),
                                    WorkReaderContract.START_AM_OR_PM_DEFAULT),
                            pref.getString(getString(R.string.FRIDAY_END_HOUR),
                                    WorkReaderContract.END_HOUR_DEFAULT),
                            pref.getString(getString(R.string.FRIDAY_END_MINUTE),
                                    WorkReaderContract.END_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.FRIDAY_END_AM_OR_PM),
                                    WorkReaderContract.END_AM_OR_PM_DEFAULT));
                    break;
                case WorkReaderContract.SATURDAY:
                    updateHours(
                            "SATURDAY",
                            pref.getString(getString(R.string.SATURDAY_START_HOUR),
                                    WorkReaderContract.START_HOUR_DEFAULT),
                            pref.getString(getString(R.string.SATURDAY_START_MINUTE),
                                    WorkReaderContract.START_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.SATURDAY_START_AM_OR_PM),
                                    WorkReaderContract.START_AM_OR_PM_DEFAULT),
                            pref.getString(getString(R.string.SATURDAY_END_HOUR),
                                    WorkReaderContract.END_HOUR_DEFAULT),
                            pref.getString(getString(R.string.SATURDAY_END_MINUTE),
                                    WorkReaderContract.END_MINUTE_DEFAULT),
                            pref.getString(getString(R.string.SATURDAY_END_AM_OR_PM),
                                    WorkReaderContract.END_AM_OR_PM_DEFAULT));
                    break;
                case 7:
                    updateHours(WorkReaderContract.DAY_OFF_DEFAULT,
                            WorkReaderContract.START_HOUR_DEFAULT,
                            WorkReaderContract.START_MINUTE_DEFAULT,
                            WorkReaderContract.START_AM_OR_PM_DEFAULT,
                            WorkReaderContract.END_HOUR_DEFAULT,
                            WorkReaderContract.END_AM_OR_PM_DEFAULT,
                            WorkReaderContract.END_AM_OR_PM_DEFAULT);
            }
    }


    @Override
    protected void onPause() {
        super.onPause();
        /*WorkReaderContract.START_HOUR_DEFAULT =null;
        WorkReaderContract.START_MINUTE_DEFAULT = null;
        WorkReaderContract.START_AM_OR_PM_DEFAULT = null;
        WorkReaderContract.END_HOUR_DEFAULT = null;
        WorkReaderContract.END_MINUTE_DEFAULT = null;
        WorkReaderContract.END_AM_OR_PM_DEFAULT = null;
        */

        Log.d(PRODUCTION_TAG, "ON PAUSE GOT CALLED");
        /*if (amEnabled == true) {
            //disableBootReceiver();
            //disableWorkNoticationReceiver();
           //amEnabled = false;
            //Log.d("CWS", "ON PAUSE GOT CALLED");
        }

        else if (amEnabled == false) {
            enableBootReceiver();
            enableWorkNotificationReceiver();
            amEnabled = true;
        }*/
        Log.d(PRODUCTION_TAG, "AM ENABLED? " + amEnabled);
    }

    @Override
    protected void onStop() {
        super.onStop();
        closedIntent = new Intent();
        if (WorkReaderContract.amAconnected == false) {
            closedIntent.putExtra("AMCLOSED", true);
            sendBroadcast(closedIntent);
        }
        //    Intent intent = new Intent(this, AlarmNotification.class);
         //   startActivity(intent);
        //}
        //Log.d(PRODUCTION_TAG, "AM I CONNECTED? " + WorkReaderContract.amAconnected);
        //Intent intent = new Intent(this, CurrentWeekSchedule.class);
        //this.startActivity(intent);

        //super.onBackPressed();
    }

}//End class
