package com.example.cd.workreminder;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.icu.util.Calendar;
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
    private ArrayAdapter<String> adapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        pref = this.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);

        //Update = (Button) findViewById(R.id.Update);
        //WorkPrefences = (Button) findViewById(R.id.ferences);

        //Finish = (Button) findViewById(R.id.Finish);
        //list = (ListView) findViewById(android.R.id.list);

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

                i = new Intent(CurrentWeekSchedule.this, HourFormat.class);
                currentDay = position; //Stupid hack
                currentPosition = position; //position on clicked list
                //i = new Intent(CurrentWeekSchedule.this, HourFormat.class);
                switch (position) {
                    case WorkReaderContract.WorkEntry.SUNDAY:
                        //currentPosition = 6; //handle integer wrap around case
                        //if (week.get(WorkReaderContract.WorkEntry.SUNDAY).get(0).equals("OFF")) {
                        if ((pref.getString(getString(R.string.SUNDAY), "OFF").equals("OFF"))){
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR",  Integer.parseInt(WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
                            i.putExtra("START_MINUTE",  Integer.parseInt(WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
                            i.putExtra("START_AM_OR_PM",  WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT);

                            i.putExtra("END_HOUR",  Integer.parseInt(WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
                            i.putExtra("END_MINUTE",  Integer.parseInt(WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
                            i.putExtra("END_AM_OR_PM",  WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT);
                        } else {
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR",
                                    Integer.parseInt( pref.getString(getString(R.string.SUNDAY_START_HOUR),
                                            WorkReaderContract.WorkEntry.START_HOUR_DEFAULT)));
                            i.putExtra("START_MINUTE",
                                    Integer.parseInt( pref.getString(getString(R.string.SUNDAY_START_MINUTE),
                                            WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT)) / 15);
                            if ( pref.getString(getString(R.string.SUNDAY_START_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT).equals("AM")) {
                                i.putExtra("START_AM_OR_PM", 0);
                            } else {
                                i.putExtra("START_AM_OR_PM", 1);
                            }

                            i.putExtra("END_HOUR",
                                    Integer.parseInt(pref.getString(getString(R.string.SUNDAY_END_HOUR),
                                            WorkReaderContract.WorkEntry.END_HOUR_DEFAULT)));
                            i.putExtra("END_MINUTE",
                                    Integer.parseInt(pref.getString(getString(R.string.SUNDAY_END_MINUTE),
                                            WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT)) / 15);
                            if (pref.getString(getString(R.string.SUNDAY_END_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT).equals("AM")) {
                                i.putExtra("END_AM_OR_PM", 0);
                            } else {
                                i.putExtra("END_AM_OR_PM", 1);
                            }
                        }
                        break;
                    case WorkReaderContract.WorkEntry.MONDAY:
                        // pref.getString(getString(R.string.SUNDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT
                        //if (week.get(WorkReaderContract.WorkEntry.MONDAY).get(0).equals("OFF")) {
                        if ((pref.getString(getString(R.string.MONDAY), "OFF").equals("OFF"))) {
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR",  Integer.parseInt(WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
                            i.putExtra("START_MINUTE",  Integer.parseInt(WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
                            i.putExtra("START_AM_OR_PM",  WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT);

                            i.putExtra("END_HOUR",  Integer.parseInt(WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
                            i.putExtra("END_MINUTE",  Integer.parseInt(WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
                            i.putExtra("END_AM_OR_PM",  WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT);

                        } else {
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR",
                                    Integer.parseInt( pref.getString(getString(R.string.MONDAY_START_HOUR),
                                            WorkReaderContract.WorkEntry.START_HOUR_DEFAULT)));
                            i.putExtra("START_MINUTE",
                                    Integer.parseInt( pref.getString(getString(R.string.MONDAY_START_MINUTE),
                                            WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT)) / 15);
                            if ( pref.getString(getString(R.string.MONDAY_START_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT).equals("AM")) {
                                i.putExtra("START_AM_OR_PM", 0);
                            } else {
                                i.putExtra("START_AM_OR_PM", 1);
                            }

                            i.putExtra("END_HOUR",
                                    Integer.parseInt(pref.getString(getString(R.string.MONDAY_END_HOUR),
                                            WorkReaderContract.WorkEntry.END_HOUR_DEFAULT)));
                            i.putExtra("END_MINUTE",
                                    Integer.parseInt(pref.getString(getString(R.string.MONDAY_END_MINUTE),
                                            WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT)) / 15);
                            if (pref.getString(getString(R.string.MONDAY_END_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT).equals("AM")) {
                                i.putExtra("END_AM_OR_PM", 0);
                            } else {
                                i.putExtra("END_AM_OR_PM", 1);
                            }
                        }
                        break;
                    case WorkReaderContract.WorkEntry.TUESDAY:
                        //if (week.get(WorkReaderContract.WorkEntry.TUESDAY).get(0).equals("OFF")) {
                        if ((pref.getString(getString(R.string.TUESDAY), "OFF").equals("OFF"))) {
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR",
                                    Integer.parseInt(WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
                            i.putExtra("START_MINUTE",
                                    Integer.parseInt(WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
                            i.putExtra("START_AM_OR_PM", WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT);

                            i.putExtra("END_HOUR",
                                    Integer.parseInt(WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
                            i.putExtra("END_MINUTE",
                                    Integer.parseInt(WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
                            i.putExtra("END_AM_OR_PM", WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT);
                        } else {
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR",
                                    Integer.parseInt( pref.getString(getString(R.string.TUESDAY_START_HOUR),
                                            WorkReaderContract.WorkEntry.START_HOUR_DEFAULT)));
                            i.putExtra("START_MINUTE",
                                    Integer.parseInt( pref.getString(getString(R.string.TUESDAY_START_MINUTE),
                                            WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT)) / 15);
                            if ( pref.getString(getString(R.string.TUESDAY_START_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT).equals("AM")) {
                                i.putExtra("START_AM_OR_PM", 0);
                            } else {
                                i.putExtra("START_AM_OR_PM", 1);
                            }

                            i.putExtra("END_HOUR",
                                    Integer.parseInt(pref.getString(getString(R.string.TUESDAY_END_HOUR),
                                            WorkReaderContract.WorkEntry.END_HOUR_DEFAULT)));
                            i.putExtra("END_MINUTE",
                                    Integer.parseInt(pref.getString(getString(R.string.TUESDAY_END_MINUTE),
                                            WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT)) / 15);
                            if (pref.getString(getString(R.string.TUESDAY_END_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT).equals("AM")) {
                                i.putExtra("END_AM_OR_PM", 0);
                            } else {
                                i.putExtra("END_AM_OR_PM", 1);
                            }
                        }
                        break;
                    case WorkReaderContract.WorkEntry.WEDNESDAY:
                        //if (week.get(WorkReaderContract.WorkEntry.WEDNESDAY).get(0).equals("OFF")) {
                        if ((pref.getString(getString(R.string.WEDNESDAY), "OFF").equals("OFF"))) {
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR", Integer.parseInt(WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
                            i.putExtra("START_MINUTE", Integer.parseInt(WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
                            i.putExtra("START_AM_OR_PM",  WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT);

                            i.putExtra("END_HOUR",  Integer.parseInt(WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
                            i.putExtra("END_MINUTE",  Integer.parseInt(WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
                            i.putExtra("END_AM_OR_PM",  WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT);
                        } else {
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR",
                                    Integer.parseInt( pref.getString(getString(R.string.WEDNESDAY_START_HOUR),
                                            WorkReaderContract.WorkEntry.START_HOUR_DEFAULT)));
                            i.putExtra("START_MINUTE",
                                    Integer.parseInt( pref.getString(getString(R.string.WEDNESDAY_START_MINUTE),
                                            WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT)) / 15);
                            if ( pref.getString(getString(R.string.WEDNESDAY_START_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT).equals("AM")) {
                                i.putExtra("START_AM_OR_PM", 0);
                            } else {
                                i.putExtra("START_AM_OR_PM", 1);
                            }

                            i.putExtra("END_HOUR",
                                    Integer.parseInt(pref.getString(getString(R.string.WEDNESDAY_END_HOUR),
                                            WorkReaderContract.WorkEntry.END_HOUR_DEFAULT)));
                            i.putExtra("END_MINUTE",
                                    Integer.parseInt(pref.getString(getString(R.string.WEDNESDAY_END_MINUTE),
                                            WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT)) / 15);
                            if (pref.getString(getString(R.string.WEDNESDAY_END_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT).equals("AM")) {
                                i.putExtra("END_AM_OR_PM", 0);
                            } else {
                                i.putExtra("END_AM_OR_PM", 1);
                            }
                        }
                        break;
                    case WorkReaderContract.WorkEntry.THURSDAY:
                        //if (week.get(WorkReaderContract.WorkEntry.THURSDAY).get(0).equals("OFF")){
                        if ((pref.getString(getString(R.string.THURSDAY), "OFF").equals("OFF"))) {
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR",  Integer.parseInt(WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
                            i.putExtra("START_MINUTE",  Integer.parseInt(WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
                            i.putExtra("START_AM_OR_PM",  WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT);

                            i.putExtra("END_HOUR",  Integer.parseInt(WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
                            i.putExtra("END_MINUTE",  Integer.parseInt(WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
                            i.putExtra("END_AM_OR_PM",  WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT);
                        } else {
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR",
                                    Integer.parseInt( pref.getString(getString(R.string.THURSDAY_START_HOUR),
                                            WorkReaderContract.WorkEntry.START_HOUR_DEFAULT)));
                            i.putExtra("START_MINUTE",
                                    Integer.parseInt( pref.getString(getString(R.string.THURSDAY_START_MINUTE),
                                            WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT)) / 15);
                            if ( pref.getString(getString(R.string.THURSDAY_START_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT).equals("AM")) {
                                i.putExtra("START_AM_OR_PM", 0);
                            } else {
                                i.putExtra("START_AM_OR_PM", 1);
                            }

                            i.putExtra("END_HOUR",
                                    Integer.parseInt(pref.getString(getString(R.string.THURSDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT)));
                            i.putExtra("END_MINUTE", Integer.parseInt(pref.getString(getString(R.string.THURSDAY_END_MINUTE),
                                    WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT)) / 15);
                            if (pref.getString(getString(R.string.FRIDAY_END_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT).equals("AM")) {
                                i.putExtra("END_AM_OR_PM", 0);
                            } else {
                                i.putExtra("END_AM_OR_PM", 1);
                            }
                        }
                        break;
                    case WorkReaderContract.WorkEntry.FRIDAY:
                        //if (week.get(WorkReaderContract.WorkEntry.FRIDAY).get(0).equals("OFF")){
                        if ((pref.getString(getString(R.string.FRIDAY), "OFF").equals("OFF"))) {
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR",  Integer.parseInt(WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
                            i.putExtra("START_MINUTE",  Integer.parseInt(WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
                            i.putExtra("START_AM_OR_PM",  WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT);

                            i.putExtra("END_HOUR",  Integer.parseInt(WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
                            i.putExtra("END_MINUTE",  Integer.parseInt(WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
                            i.putExtra("END_AM_OR_PM",  WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT);
                        } else {
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR",
                                    Integer.parseInt( pref.getString(getString(R.string.FRIDAY_START_HOUR),
                                            WorkReaderContract.WorkEntry.START_HOUR_DEFAULT)));
                            i.putExtra("START_MINUTE", Integer.parseInt( pref.getString(getString(R.string.FRIDAY_START_MINUTE),
                                    WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT)) / 15);
                            if ( pref.getString(getString(R.string.FRIDAY_START_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT).equals("AM")) {
                                i.putExtra("START_AM_OR_PM", 0);
                            } else {
                                i.putExtra("START_AM_OR_PM", 1);
                            }

                            i.putExtra("END_HOUR",
                                    Integer.parseInt(pref.getString(getString(R.string.FRIDAY_END_HOUR),
                                            WorkReaderContract.WorkEntry.END_HOUR_DEFAULT)));
                            i.putExtra("END_MINUTE",
                                    Integer.parseInt(pref.getString(getString(R.string.FRIDAY_END_MINUTE),
                                            WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT)) / 15);
                            if (pref.getString(getString(R.string.FRIDAY_END_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT).equals("AM")) {
                                i.putExtra("END_AM_OR_PM", 0);
                            } else {
                                i.putExtra("END_AM_OR_PM", 1);
                            }
                        }
                        break;

                    case WorkReaderContract.WorkEntry.SATURDAY:
                        //if (week.get(WorkReaderContract.WorkEntry.SATURDAY).get(0).equals("OFF")) {
                        if ((pref.getString(getString(R.string.SATURDAY), "OFF").equals("OFF"))) {
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR",  Integer.parseInt(WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
                            i.putExtra("START_MINUTE",  Integer.parseInt(WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
                            i.putExtra("START_AM_OR_PM",  WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT);

                            i.putExtra("END_HOUR",  Integer.parseInt(WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
                            i.putExtra("END_MINUTE",  Integer.parseInt(WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
                            i.putExtra("END_AM_OR_PM",  WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT);
                        } else {
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR",
                                    Integer.parseInt( pref.getString(getString(R.string.SATURDAY_START_HOUR),
                                            WorkReaderContract.WorkEntry.START_HOUR_DEFAULT)));
                            i.putExtra("START_MINUTE",
                                    Integer.parseInt( pref.getString(getString(R.string.SATURDAY_START_MINUTE),
                                            WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT)) / 15);
                            if ( pref.getString(getString(R.string.SATURDAY_START_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT).equals("AM")) {
                                i.putExtra("START_AM_OR_PM", 0);
                            } else {
                                i.putExtra("START_AM_OR_PM", 1);
                            }

                            i.putExtra("END_HOUR",
                                    Integer.parseInt(pref.getString(getString(R.string.SATURDAY_END_HOUR),
                                            WorkReaderContract.WorkEntry.END_HOUR_DEFAULT)));
                            i.putExtra("END_MINUTE",
                                    Integer.parseInt(pref.getString(getString(R.string.SATURDAY_END_MINUTE),
                                            WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT)) / 15);
                            if (pref.getString(getString(R.string.SATURDAY_END_AM_OR_PM),
                                    WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT).equals("AM")) {
                                i.putExtra("END_AM_OR_PM", 0);
                            } else {
                                i.putExtra("END_AM_OR_PM", 1);
                            }
                        }
                        break;
                }//end


                //pipe shit forward

                Log.e(PRODUCTION_TAG, "TEXT AT: " + position + " IS: " + value);
                //startActivity(i);
                startActivityForResult(i, 0); //does writepar save the data??? Need to test for Thursday case.
            }
        });

        storeHoursInGUI currentWeek = new storeHoursInGUI(this);
        if (savedInstanceState == null) {
            week = currentWeek.addHours();
            adapter = new WS(this,
                    R.layout.schedule_list, week);

            setListAdapter(adapter);
            //addHours();
        } else {
            week = currentWeek.addHours();
            adapter = new WS(this,
                    R.layout.schedule_list, week);
            setListAdapter(adapter);
            //addHours();

            //mondayHours.setDayofWeek("OFF");
            //week.get(1).remove(0);
            //week.get(1).add(0, mondayHours.getDayOfWeek());

        }
    } //end onCreate()


    //Added on 3 - 28 - 2019
    //Save data when screen changes from portrait to landscape
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        outState.putString("DAY_SUNDAY",  pref.getString(getString(R.string.SUNDAY), getString(R.string.SUNDAY)));
        outState.putString(getString(R.string.SUNDAY_START_HOUR),
                pref.getString(getString(R.string.SUNDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
        outState.putString(getString(R.string.SUNDAY_START_MINUTE),
                pref.getString(getString(R.string.SUNDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
        outState.putString(getString(R.string.SUNDAY_START_AM_OR_PM),
                pref.getString(getString(R.string.SUNDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
        outState.putString(getString(R.string.SUNDAY_END_HOUR),
                pref.getString(getString(R.string.SUNDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
        outState.putString(getString(R.string.SUNDAY_END_MINUTE),
                pref.getString(getString(R.string.SUNDAY_END_MINUTE), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
        outState.putString(getString(R.string.SUNDAY_END_AM_OR_PM),
                pref.getString(getString(R.string.SUNDAY_END_AM_OR_PM), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));

        outState.putString("DAY_MONDAY",  pref.getString(getString(R.string.MONDAY), getString(R.string.MONDAY)));
        outState.putString(getString(R.string.MONDAY_START_HOUR),
                pref.getString(getString(R.string.MONDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
        outState.putString(getString(R.string.MONDAY_START_MINUTE),
                pref.getString(getString(R.string.MONDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
        outState.putString(getString(R.string.MONDAY_START_AM_OR_PM),
                pref.getString(getString(R.string.MONDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
        outState.putString(getString(R.string.MONDAY_END_HOUR),
                pref.getString(getString(R.string.MONDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
        outState.putString(getString(R.string.MONDAY_END_MINUTE),
                pref.getString(getString(R.string.MONDAY_END_MINUTE), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
        outState.putString(getString(R.string.MONDAY_END_AM_OR_PM),
                pref.getString(getString(R.string.MONDAY_END_AM_OR_PM), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));

        outState.putString("DAY_TUESDAY",  pref.getString(getString(R.string.TUESDAY), getString(R.string.TUESDAY)));
        outState.putString(getString(R.string.TUESDAY_START_HOUR),
                pref.getString(getString(R.string.TUESDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
        outState.putString(getString(R.string.TUESDAY_START_MINUTE),
                pref.getString(getString(R.string.TUESDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
        outState.putString(getString(R.string.TUESDAY_START_AM_OR_PM),
                pref.getString(getString(R.string.TUESDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
        outState.putString(getString(R.string.TUESDAY_END_HOUR),
                pref.getString(getString(R.string.TUESDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
        outState.putString(getString(R.string.TUESDAY_END_MINUTE),
                pref.getString(getString(R.string.TUESDAY_END_MINUTE), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
        outState.putString(getString(R.string.TUESDAY_END_AM_OR_PM),
                pref.getString(getString(R.string.TUESDAY_END_AM_OR_PM), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));

        outState.putString("DAY_WEDNESDAY", pref.getString(getString(R.string.WEDNESDAY), getString(R.string.WEDNESDAY)));
        outState.putString(getString(R.string.WEDNESDAY_START_HOUR),
                pref.getString(getString(R.string.WEDNESDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
        outState.putString(getString(R.string.WEDNESDAY_START_MINUTE),
                pref.getString(getString(R.string.WEDNESDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
        outState.putString(getString(R.string.WEDNESDAY_START_AM_OR_PM),
                pref.getString(getString(R.string.WEDNESDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
        outState.putString(getString(R.string.WEDNESDAY_END_HOUR),
                pref.getString(getString(R.string.WEDNESDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
        outState.putString(getString(R.string.WEDNESDAY_END_MINUTE),
                pref.getString(getString(R.string.WEDNESDAY_END_MINUTE), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
        outState.putString(getString(R.string.WEDNESDAY_END_AM_OR_PM),
                pref.getString(getString(R.string.WEDNESDAY_END_AM_OR_PM), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));

        outState.putString("DAY_THURSDAY", pref.getString(getString(R.string.THURSDAY), getString(R.string.THURSDAY)));
        outState.putString(getString(R.string.THURSDAY_START_HOUR),
                pref.getString(getString(R.string.THURSDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
        outState.putString(getString(R.string.THURSDAY_START_MINUTE),
                pref.getString(getString(R.string.THURSDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
        outState.putString(getString(R.string.THURSDAY_START_AM_OR_PM),
                pref.getString(getString(R.string.THURSDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
        outState.putString(getString(R.string.THURSDAY_END_HOUR),
                pref.getString(getString(R.string.THURSDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
        outState.putString(getString(R.string.THURSDAY_END_MINUTE),
                pref.getString(getString(R.string.THURSDAY_END_MINUTE), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
        outState.putString(getString(R.string.THURSDAY_END_AM_OR_PM),
                pref.getString(getString(R.string.THURSDAY_END_AM_OR_PM), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));

        outState.putString("DAY_FRIDAY", pref.getString(getString(R.string.FRIDAY), getString(R.string.FRIDAY)));
        outState.putString(getString(R.string.FRIDAY_START_HOUR),
                pref.getString(getString(R.string.FRIDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
        outState.putString(getString(R.string.FRIDAY_START_MINUTE),
                pref.getString(getString(R.string.FRIDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
        outState.putString(getString(R.string.FRIDAY_START_AM_OR_PM),
                pref.getString(getString(R.string.FRIDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
        outState.putString(getString(R.string.FRIDAY_END_HOUR),
                pref.getString(getString(R.string.FRIDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
        outState.putString(getString(R.string.FRIDAY_END_MINUTE),
                pref.getString(getString(R.string.FRIDAY_END_MINUTE), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
        outState.putString(getString(R.string.FRIDAY_END_AM_OR_PM),
                pref.getString(getString(R.string.FRIDAY_END_AM_OR_PM), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));

        outState.putString("DAY_SATURDAY", pref.getString(getString(R.string.SATURDAY), getString(R.string.SATURDAY)));
        outState.putString(getString(R.string.SATURDAY_START_HOUR),
                pref.getString(getString(R.string.SATURDAY_START_HOUR), WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
        outState.putString(getString(R.string.SATURDAY_START_MINUTE),
                pref.getString(getString(R.string.SATURDAY_START_MINUTE), WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
        outState.putString(getString(R.string.SATURDAY_START_AM_OR_PM),
                pref.getString(getString(R.string.SATURDAY_START_AM_OR_PM), WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT));
        outState.putString(getString(R.string.SATURDAY_END_HOUR),
                pref.getString(getString(R.string.SATURDAY_END_HOUR), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
        outState.putString(getString(R.string.SATURDAY_END_MINUTE),
                pref.getString(getString(R.string.SATURDAY_END_MINUTE), WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
        outState.putString(getString(R.string.SATURDAY_END_AM_OR_PM),
                pref.getString(getString(R.string.SATURDAY_END_AM_OR_PM), WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));

        super.onSaveInstanceState(outState);
    }

    //I use list view because HTML fonts don't get preserved when the device changes orientation.
    private class WS<String> extends ArrayAdapter<java.lang.String> {
        private int visibleMenuOptions = 3; //3 is for debugging only

        WS(Context context, int resource, List<java.lang.String> Objects) {
            super(context, resource, Objects);
            this.visibleMenuOptions = visibleMenuOptions;
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
            //return super.getDropDownView(position, convertView, parent);
            Log.e(PRODUCTION_TAG, "MENU GOT HIDDEN AT POSITION: " + position);
            View v = null;
            if (position == visibleMenuOptions) {
                Log.e(PRODUCTION_TAG, "MENU GOT HIDDEN AT POSITION: " + position);
                TextView tv = new TextView(getContext());
                tv.setVisibility(View.GONE);
                v = tv;
            } else {
                v = super.getDropDownView(position, null, parent);
            }
            return v;
        }

        @TargetApi(24)
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //super.getView(position, convertView, parent);
            if (convertView == null) {
                //Log.e(PRODUCTION_TAG, "CONVERT VIEW IS NULL");
                convertView = getLayoutInflater().inflate(R.layout.schedule_list, parent, false);
                //convertView = getLayoutInflater().inflate(android.R.layout.simple_expandable_list_item_2, parent, false);
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
            if (week.get(position).get(0).equals("OFF")) {
                ((TextView) convertView.findViewById(R.id.dayOfWeek)).setText("");
                ((TextView) convertView.findViewById(R.id.startTime)).setText("");
                ((TextView) convertView.findViewById(R.id.hour_separator)).setText("");
                ((TextView) convertView.findViewById(R.id.endTime)).setText("");

            } else {
                //week.remove(position);
                //week.get(0).add(dayPair.get(position));
                ((TextView) convertView.findViewById(R.id.dayOfWeek)).setText(week.get(position).get(0));
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

            //text_separator.setMinHeight(0); // Min Height
            //text_separator.setMinimumHeight(0); // Min Height
            //text_separator.setHeight(120); // Height in pixels. Not dip?
            //text_separator.setWidth(20);

            text_end_hour.setMinHeight(0); // Min Height
            text_end_hour.setMinimumHeight(0); // Min Height
            text_end_hour.setHeight(120); // Height in pixels. Not dip?
            text_end_hour.setWidth(0);

            //text_day.setMinHeight(0); // Min Height
            //text_day.setMinTimumHeight(0); // Min Height
            //text_day.setHeight(120); // Height in pixels. Not dip?

            dayNotification dayNotification = new dayNotification();
            if (dayNotification.handleThirdShift() == position + 1) {
                text_start_hour.setTypeface(text_start_hour.getTypeface(), Typeface.BOLD);  //vs null??
                text_separator.setTypeface(text_separator.getTypeface(), Typeface.BOLD);  //vs null??
                text_end_hour.setTypeface(text_end_hour.getTypeface(), Typeface.BOLD);  //vs null??
                text_day.setTypeface(text_day.getTypeface(), Typeface.BOLD);

            }

            //return super.getView(position, convertView, parent);
            return convertView;
            //return  view;
        }//end method

    }//end inner class


    //Added on 8 - 4 - 2019
    @TargetApi(24)
    private void setCurrentAlarm(int startMilitaryHour, int startMilitaryMinute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, startMilitaryHour);
        calendar.set(Calendar.MINUTE, startMilitaryMinute);
        Log.e(PRODUCTION_TAG, "CURRENT MILITARY HOUR IS: " + startMilitaryHour);
        Log.e(PRODUCTION_TAG,"SET CURRENT ALARM GOT CALLED");
        alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(CurrentWeekSchedule.this, WorkAlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
// With setInexactRepeating(), you have to use one of the AlarmManager interval
// constants--in this case, AlarmManager.INTERVAL_DAY.
        //alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
        //        AlarmManager.INTERVAL_DAY, alarmIntent);
        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
    }

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

        SharedPreferences.Editor editor = pref.edit();
        //editor.putString(getString(R.string.com_example_cd_shiftreminder_SAVE_DAY), updatedHour);
        switch(currentPosition){

            case WorkReaderContract.WorkEntry.SUNDAY:
                /*sundayHours.setStartHour(newStartHour);
                sundayHours.setStartMinute(newStartMinute);
                sundayHours.setStartAmOrPm(newStartAmOrPm);
                sundayHours.setEndHour(newEndHour);
                sundayHours.setEndMinute(newEndMinute);
                sundayHours.setEndAmOrPm(newEndAmOrPm);
                */
                if (pref.getString(getString(R.string.SUNDAY),"")!= "OFF"){
                editor.putString(getString(R.string.SUNDAY), getString(R.string.SUNDAY));
                editor.putString(getString(R.string.SUNDAY_START_HOUR), newStartHour);
                editor.putString(getString(R.string.SUNDAY_START_MINUTE), newStartMinute);
                editor.putString(getString(R.string.SUNDAY_START_AM_OR_PM), newStartAmOrPm);
                editor.putString(getString(R.string.SUNDAY_END_HOUR), newEndHour);
                editor.putString(getString(R.string.SUNDAY_END_MINUTE), newEndMinute);
                editor.putString(getString(R.string.SUNDAY_END_AM_OR_PM), newEndAmOrPm);
            }
                break;
            case WorkReaderContract.WorkEntry.MONDAY:
                if (pref.getString(getString(R.string.MONDAY),"")!= "OFF") {
                    editor.putString(getString(R.string.MONDAY), getString(R.string.MONDAY));
                    editor.putString(getString(R.string.MONDAY_START_HOUR), newStartHour);
                    editor.putString(getString(R.string.MONDAY_START_MINUTE), newStartMinute);
                    editor.putString(getString(R.string.MONDAY_START_AM_OR_PM), newStartAmOrPm);
                    editor.putString(getString(R.string.MONDAY_END_HOUR), newEndHour);
                    editor.putString(getString(R.string.MONDAY_END_MINUTE), newEndMinute);
                    editor.putString(getString(R.string.MONDAY_END_AM_OR_PM), newEndAmOrPm);
                }
                break;
            case WorkReaderContract.WorkEntry.TUESDAY:

                if (pref.getString(getString(R.string.TUESDAY),"")!= "OFF") {
                    editor.putString(getString(R.string.TUESDAY), getString(R.string.TUESDAY));
                    editor.putString(getString(R.string.TUESDAY_START_HOUR), newStartHour);
                    editor.putString(getString(R.string.TUESDAY_START_MINUTE), newStartMinute);
                    editor.putString(getString(R.string.TUESDAY_START_AM_OR_PM), newStartAmOrPm);
                    editor.putString(getString(R.string.TUESDAY_END_HOUR), newEndHour);
                    editor.putString(getString(R.string.TUESDAY_END_MINUTE), newEndMinute);
                    editor.putString(getString(R.string.TUESDAY_END_AM_OR_PM), newEndAmOrPm);
                }
                break;
            case WorkReaderContract.WorkEntry.WEDNESDAY:

                if (pref.getString(getString(R.string.WEDNESDAY),"")!= "OFF") {
                    editor.putString(getString(R.string.WEDNESDAY), getString(R.string.WEDNESDAY));
                    editor.putString(getString(R.string.WEDNESDAY_START_HOUR), newStartHour);
                    editor.putString(getString(R.string.WEDNESDAY_START_MINUTE), newStartMinute);
                    editor.putString(getString(R.string.WEDNESDAY_START_AM_OR_PM), newStartAmOrPm);
                    editor.putString(getString(R.string.WEDNESDAY_END_HOUR), newEndHour);
                    editor.putString(getString(R.string.WEDNESDAY_END_MINUTE), newEndMinute);
                    editor.putString(getString(R.string.WEDNESDAY_END_AM_OR_PM), newEndAmOrPm);
                }

                break;
            case WorkReaderContract.WorkEntry.THURSDAY:

                if (pref.getString(getString(R.string.THURSDAY),"")!= "OFF") {
                    editor.putString(getString(R.string.THURSDAY), "THURSDAY");
                    editor.putString(getString(R.string.THURSDAY_START_HOUR), newStartHour);
                    editor.putString(getString(R.string.THURSDAY_START_MINUTE), newStartMinute);
                    editor.putString(getString(R.string.THURSDAY_START_AM_OR_PM), newStartAmOrPm);
                    editor.putString(getString(R.string.THURSDAY_END_HOUR), newEndHour);
                    editor.putString(getString(R.string.THURSDAY_END_MINUTE), newEndMinute);
                    editor.putString(getString(R.string.THURSDAY_END_AM_OR_PM), newEndAmOrPm);
                } else {
                    editor.putString(getString(R.string.THURSDAY), "OFF");
                    editor.putString(getString(R.string.THURSDAY_START_HOUR), "");
                    editor.putString(getString(R.string.THURSDAY_START_MINUTE), "");
                    editor.putString(getString(R.string.THURSDAY_START_AM_OR_PM), "");
                    editor.putString(getString(R.string.THURSDAY_END_HOUR), "");
                    editor.putString(getString(R.string.THURSDAY_END_MINUTE), "");
                    editor.putString(getString(R.string.THURSDAY_END_AM_OR_PM), "");
                }
                break;
            case WorkReaderContract.WorkEntry.FRIDAY:
                //Emulate Intent SEND. Fuck Java.
                //intent.setAction(Intent.ACTION_SEND);
                //intent.putStringArrayListExtra(getString(R.string.com_example_cd_shiftreminder_FRIDAY), updateCurrrentHour)
                //editor.putString(getString(R.string.FRIDAY), "FRIDAY");

                if ( pref.getString(getString(R.string.FRIDAY),"")!= "OFF") {
                    editor.putString(getString(R.string.FRIDAY), getString(R.string.FRIDAY));
                    editor.putString(getString(R.string.FRIDAY_START_HOUR), newStartHour);
                    editor.putString(getString(R.string.FRIDAY_START_MINUTE), newStartMinute);
                    editor.putString(getString(R.string.FRIDAY_START_AM_OR_PM), newStartAmOrPm);
                    editor.putString(getString(R.string.FRIDAY_END_HOUR), newEndHour);
                    editor.putString(getString(R.string.FRIDAY_END_MINUTE), newEndMinute);
                    editor.putString(getString(R.string.FRIDAY_END_AM_OR_PM), newEndAmOrPm);
                }

                break;
            case WorkReaderContract.WorkEntry.SATURDAY:
                if (pref.getString(getString(R.string.SATURDAY),"")!= "OFF") {
                    editor.putString(getString(R.string.SATURDAY), getString(R.string.SATURDAY));
                    editor.putString(getString(R.string.SATURDAY_START_HOUR), newStartHour);
                    editor.putString(getString(R.string.SATURDAY_START_MINUTE), newStartMinute);
                    editor.putString(getString(R.string.SATURDAY_START_AM_OR_PM), newStartAmOrPm);
                    editor.putString(getString(R.string.SATURDAY_END_HOUR), newEndHour);
                    editor.putString(getString(R.string.SATURDAY_END_MINUTE), newEndMinute);
                    editor.putString(getString(R.string.SATURDAY_END_AM_OR_PM), newEndAmOrPm);
                }
                break;
        }

        editor.apply();
        //adapter.remove("foo");
        //adapter.insert(updatedHour.toString() , currentPosition);
        adapter.notifyDataSetChanged();

    }
    //Added on 2 - 27 - 2019
    @TargetApi(24)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int newPosition = -1; // don't enter switch
        int minutes = 0;
        String day = "";
        if (data != null) {
            AlarmTimer alarmTimer = AlarmTimer.getInstance();
            dayNotification dayNotification = new dayNotification();
            dayNotification.displayNotification(//pref.getInt("ALARM_HOUR", 0)
                    alarmTimer.getUpdatedHour()
                            + ":" +
                                    //week.get(position).get(WorkReaderContract.WorkEntry.START_MINUTE)
                            alarmTimer.getUpdatedMinute()
                                    //pref.getInt("MINUTES", 0)
                            + " "
                            + alarmTimer.getAMorPM(), "ALARM");

            newPosition = data.getIntExtra("CURRENT_DAY", -99);  //position in listview
            if (newPosition != -99) {
                newStartHour = data.getStringExtra(getString(R.string.START_HOUR)); //hardware bug??
                newStartMinute = data.getStringExtra(getString(R.string.START_MINUTE));
                newStartAmOrPm = data.getStringExtra(getString(R.string.START_AM_OR_PM));
                newEndHour = data.getStringExtra(getString(R.string.END_HOUR));
                newEndMinute = data.getStringExtra(getString(R.string.END_MINUTE));
                newEndAmOrPm = data.getStringExtra(getString(R.string.END_AM_OR_PM));
                day = data.getStringExtra(getString(R.string.DAY_OF_WEEK));

                MilitaryTime militaryTime = MilitaryTime.getInstance();
                militaryTime.convertStartCivilianTimeToMilitaryTime(newStartHour, newStartMinute, newStartAmOrPm);
                alarmTimer = AlarmTimer.getInstance();
                alarmTimer.setAlarmTime(this, militaryTime.getStartMilitaryHour(),
                        militaryTime.getStartMilitaryMinute(),
                        pref.getInt(getString(R.string.ALARM_MINUTES), WorkReaderContract.WorkEntry.ALARM_DEFAULT));
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
                    updateHours("OFF",
                            pref.getString(getString(R.string.OFF),
                                    WorkReaderContract.WorkEntry.START_HOUR_DEFAULT),
                            //newWorkHours.getStartMinute(),
                            "",
                            //newWorkHours.getStartAmOrPm(),
                            "",
                            //newWorkHours.getEndHour(),
                            "",
                            //newWorkHours.getEndMinute(),
                            "",
                            //newWorkHours.getEndAmOrPm()
                            "");
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
