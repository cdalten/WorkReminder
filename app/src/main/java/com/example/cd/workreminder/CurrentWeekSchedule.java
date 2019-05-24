package com.example.cd.workreminder;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CurrentWeekSchedule extends ListActivity {
//public class CurrentWeekSchedule extends ListFragment {

    private static final String PRODUCTION_TAG = "LG_WORK_PHONE";
    Intent intent; //??

    TextView text_day; //Added on 2 - 10 - 2019
    TextView text_start_hour; //Added on 2 - 10 -2019
    TextView text_separator; //Added on 3 - 19 - 2019
    TextView text_end_hour; //Added on 3 - 19 -2019

    Button Update; //Added on 12 - 2 - 2018
    Button Finish; //Added on 12 - 2 - 2018
    Button WorkPrefences; //Added on 4 - 3 - 2019

    LinearLayout WorkHours; //Added on 12 - 6 - 2018
    private int currentPosition = 0; //Added on 1 - 20 - 2019
    static List<String> values; //Modified on 1 - 18 - 2019
    private ArrayAdapter<String> adapter;

    ListView list;
    private Intent i;
    private Intent workPreferenceIntent; //Added on 4 - 3 - 2019

    ArrayList<ArrayList<String>> week; //added on 2 - 24 - 2019
    private int currentDay = 0; //Added on 3 - 3 - 2019

    private int newPosition; //Added on 3 - 4 - 2019 Need??
    private String newStartDay; //Added on 3 - 12 - 2019
    private String newStartHour; //Added on 3 - 2 - 2019
    private String newStartMinute;
    private String newStartAmOrPm;
    private String newEndHour; //Added on 3 - 4 - 2019
    private String newEndMinute;
    private String newEndAmOrPm;

    private String getCurrentHours;  //Added on 3 - 6 - 2019
    private WorkAlarmReceiver workAlarmReceiver; //Added on 5 - 22 - 2019
    CurrentWorkWeek newWorkHours; //Added on 3 - 2 - 2019
    SharedPreferences pref;

    HashMap<Integer, String> dayPair;
    ArrayList <HashMap<Integer,String>> dayList;
    private boolean saveChanges = false; //Added on 3 - 27 - 2019

    CurrentWorkWeek sundayHours; //Added on 3 - 27 - 2019
    CurrentWorkWeek mondayHours;
    CurrentWorkWeek tuesdayHours;
    CurrentWorkWeek wednesdayHours;
    CurrentWorkWeek thursdayHours;
    CurrentWorkWeek fridayHours;
    CurrentWorkWeek saturdayHours;

    //Need to eventually remove
    private String[] days = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"}; //Added on 2 - 10 - 2019

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        pref = this.getSharedPreferences("BECAUSE INTENTS SUCK MASSIVE DICK", MODE_PRIVATE);
        //week = new String[7];
        //values = new ArrayList<>(Arrays.asList(week));
        //String thursdayStartHour = pref.getString(Days.THURSDAY_START_HOUR, "OFF"); //empty string represents default value
        //Log.e(PRODUCTION_TAG, "THURSDAY START HOUR IS: " + thursdayStartHour);

        //Update = (Button) findViewById(R.id.Update);
        //WorkPrefences = (Button) findViewById(R.id.ferences);

        //Finish = (Button) findViewById(R.id.Finish);
        //list = (ListView) findViewById(android.R.id.list);
        list = getListView();
        i = new Intent(CurrentWeekSchedule.this, HourFormat.class);

       /* WorkPrefences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workPreferenceIntent = new Intent(CurrentWeekSchedule.this, WorkPreferences.class);
                workPreferenceIntent.putExtra("ALARM_MINUTES", 0);
                workPreferenceIntent.putExtra("NEW_DOWNLOAD_DATE", 0);
                startActivityForResult(workPreferenceIntent, 1);
            }
        });
        */


        IntentFilter intentFilter = new IntentFilter();
        workAlarmReceiver = new WorkAlarmReceiver();
        registerReceiver(workAlarmReceiver, intentFilter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = (String)adapter.getItem(position);
                //values.add("FOO");

                currentDay = position; //Stupid hack
                currentPosition = position; //position on clicked list
                //i = new Intent(CurrentWeekSchedule.this, HourFormat.class);
                switch (position) {
                    case WorkReaderContract.WorkEntry.SUNDAY:
                        //currentPosition = 6; //handle integer wrap around case
                        if (week.get(WorkReaderContract.WorkEntry.SUNDAY).get(0).equals("OFF")) {
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR",  Integer.parseInt(WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
                            i.putExtra("START_MINUTE",  Integer.parseInt(WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
                            i.putExtra("START_AM_OR_PM",  WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT);

                            i.putExtra("END_HOUR",  Integer.parseInt(WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
                            i.putExtra("END_MINUTE",  Integer.parseInt(WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
                            i.putExtra("END_AM_OR_PM",  WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT);
                        } else {
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR", Integer.parseInt(sundayHours.getStartHour()));
                            i.putExtra("START_MINUTE", Integer.parseInt(sundayHours.getStartMinute()) / 15);
                            if (sundayHours.getStartAmOrPm().equals("AM")) {
                                i.putExtra("START_AM_OR_PM", 0);
                            } else {
                                i.putExtra("START_AM_OR_PM", 1);
                            }

                            i.putExtra("END_HOUR", Integer.parseInt(sundayHours.getEndHour()));
                            i.putExtra("END_MINUTE", Integer.parseInt(sundayHours.getStartMinute()) / 15);
                            if (sundayHours.getEndAmOrPm().equals("AM")) {
                                i.putExtra("END_AM_OR_PM", 0);
                            } else {
                                i.putExtra("END_AM_OR_PM", 1);
                            }
                        }
                        break;
                    case WorkReaderContract.WorkEntry.MONDAY:
                        if (week.get(WorkReaderContract.WorkEntry.MONDAY).get(0).equals("OFF")) {
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR",  Integer.parseInt(WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
                            i.putExtra("START_MINUTE",  Integer.parseInt(WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
                            i.putExtra("START_AM_OR_PM",  WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT);

                            i.putExtra("END_HOUR",  Integer.parseInt(WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
                            i.putExtra("END_MINUTE",  Integer.parseInt(WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
                            i.putExtra("END_AM_OR_PM",  WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT);

                        } else {
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR", Integer.parseInt(mondayHours.getStartHour()));
                            i.putExtra("START_MINUTE", Integer.parseInt(mondayHours.getStartMinute()) / 15);
                            if (mondayHours.getStartAmOrPm().equals("AM")) {
                                i.putExtra("START_AM_OR_PM", 0);
                            } else {
                                i.putExtra("START_AM_OR_PM", 1);
                            }

                            i.putExtra("END_HOUR", Integer.parseInt(mondayHours.getEndHour()));
                            i.putExtra("END_MINUTE", Integer.parseInt(mondayHours.getEndMinute()) / 15);
                            if (mondayHours.getEndAmOrPm().equals("AM")) {
                                i.putExtra("END_AM_OR_PM", 0);
                            } else {
                                i.putExtra("END_AM_OR_PM", 1);
                            }
                        }
                        break;
                    case WorkReaderContract.WorkEntry.TUESDAY:
                        if (week.get(WorkReaderContract.WorkEntry.TUESDAY).get(0).equals("OFF")) {
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
                            i.putExtra("START_HOUR", Integer.parseInt(tuesdayHours.getStartHour()));
                            i.putExtra("START_MINUTE", Integer.parseInt(tuesdayHours.getStartMinute()) / 15);
                            if (tuesdayHours.getStartAmOrPm().equals("AM")) {
                                i.putExtra("START_AM_OR_PM", 0);
                            } else {
                                i.putExtra("START_AM_OR_PM", 1);
                            }

                            i.putExtra("END_HOUR", Integer.parseInt(tuesdayHours.getEndHour()));
                            i.putExtra("END_MINUTE", Integer.parseInt(tuesdayHours.getEndMinute()) / 15);
                            if (tuesdayHours.getEndAmOrPm().equals("AM")) {
                                i.putExtra("END_AM_OR_PM", 0);
                            } else {
                                i.putExtra("END_AM_OR_PM", 1);
                            }
                        }
                        break;
                    case WorkReaderContract.WorkEntry.WEDNESAY:
                        if (week.get(WorkReaderContract.WorkEntry.WEDNESAY).get(0).equals("OFF")) {
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR", Integer.parseInt(WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
                            i.putExtra("START_MINUTE", Integer.parseInt(WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
                            i.putExtra("START_AM_OR_PM",  WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT);

                            i.putExtra("END_HOUR",  Integer.parseInt(WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
                            i.putExtra("END_MINUTE",  Integer.parseInt(WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
                            i.putExtra("END_AM_OR_PM",  WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT);
                        } else {
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR", Integer.parseInt(wednesdayHours.getStartHour()));
                            i.putExtra("START_MINUTE", Integer.parseInt(wednesdayHours.getStartMinute()) / 15);
                            if (wednesdayHours.getStartAmOrPm().equals("AM")) {
                                i.putExtra("START_AM_OR_PM", 0);
                            } else {
                                i.putExtra("START_AM_OR_PM", 1);
                            }

                            i.putExtra("END_HOUR", Integer.parseInt(wednesdayHours.getEndHour()));
                            i.putExtra("END_MINUTE", Integer.parseInt(wednesdayHours.getEndMinute()) / 15);
                            if (wednesdayHours.getEndAmOrPm().equals("AM")) {
                                i.putExtra("END_AM_OR_PM", 0);
                            } else {
                                i.putExtra("END_AM_OR_PM", 1);
                            }
                        }
                        break;
                    case WorkReaderContract.WorkEntry.THURSDAY:
                        if (week.get(WorkReaderContract.WorkEntry.THURSDAY).get(0).equals("OFF")){
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR",  Integer.parseInt(WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
                            i.putExtra("START_MINUTE",  Integer.parseInt(WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
                            i.putExtra("START_AM_OR_PM",  WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT);

                            i.putExtra("END_HOUR",  Integer.parseInt(WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
                            i.putExtra("END_MINUTE",  Integer.parseInt(WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
                            i.putExtra("END_AM_OR_PM",  WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT);
                        } else {
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR", Integer.parseInt(thursdayHours.getStartHour()));
                            i.putExtra("START_MINUTE", Integer.parseInt(thursdayHours.getStartMinute()) / 15);
                            if (thursdayHours.getStartAmOrPm().equals("AM")) {
                                i.putExtra("START_AM_OR_PM", 0);
                            } else {
                                i.putExtra("START_AM_OR_PM", 1);
                            }

                            i.putExtra("END_HOUR", Integer.parseInt(thursdayHours.getEndHour()));
                            i.putExtra("END_MINUTE", Integer.parseInt(thursdayHours.getEndMinute()) / 15);
                            if (thursdayHours.getEndAmOrPm().equals("AM")) {
                                i.putExtra("END_AM_OR_PM", 0);
                            } else {
                                i.putExtra("END_AM_OR_PM", 1);
                            }
                        }
                        break;
                    case WorkReaderContract.WorkEntry.FRIDAY:
                        if (week.get(WorkReaderContract.WorkEntry.FRIDAY).get(0).equals("OFF")){
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR",  Integer.parseInt(WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
                            i.putExtra("START_MINUTE",  Integer.parseInt(WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
                            i.putExtra("START_AM_OR_PM",  WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT);

                            i.putExtra("END_HOUR",  Integer.parseInt(WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
                            i.putExtra("END_MINUTE",  Integer.parseInt(WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
                            i.putExtra("END_AM_OR_PM",  WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT);
                        } else {
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR", Integer.parseInt(fridayHours.getStartHour()));
                            i.putExtra("START_MINUTE", Integer.parseInt(fridayHours.getStartMinute()) / 15);
                            if (fridayHours.getStartAmOrPm().equals("AM")) {
                                i.putExtra("START_AM_OR_PM", 0);
                            } else {
                                i.putExtra("START_AM_OR_PM", 1);
                            }

                            i.putExtra("END_HOUR", Integer.parseInt(fridayHours.getEndHour()));
                            i.putExtra("END_MINUTE", Integer.parseInt(fridayHours.getEndMinute()) / 15);
                            if (fridayHours.getEndAmOrPm().equals("AM")) {
                                i.putExtra("END_AM_OR_PM", 0);
                            } else {
                                i.putExtra("END_AM_OR_PM", 1);
                            }
                        }
                        break;

                    case WorkReaderContract.WorkEntry.SATURDAY:
                        if (week.get(WorkReaderContract.WorkEntry.SATURDAY).get(0).equals("OFF")) {
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR",  Integer.parseInt(WorkReaderContract.WorkEntry.START_HOUR_DEFAULT));
                            i.putExtra("START_MINUTE",  Integer.parseInt(WorkReaderContract.WorkEntry.START_MINUTE_DEFAULT));
                            i.putExtra("START_AM_OR_PM",  WorkReaderContract.WorkEntry.START_AM_OR_PM_DEFAULT);

                            i.putExtra("END_HOUR",  Integer.parseInt(WorkReaderContract.WorkEntry.END_HOUR_DEFAULT));
                            i.putExtra("END_MINUTE",  Integer.parseInt(WorkReaderContract.WorkEntry.END_MINUTE_DEFAULT));
                            i.putExtra("END_AM_OR_PM",  WorkReaderContract.WorkEntry.END_AM_OR_PM_DEFAULT);
                        } else {
                            i.putExtra("DAY_WEEK", position);
                            i.putExtra("START_HOUR", Integer.parseInt(saturdayHours.getStartHour()));
                            i.putExtra("START_MINUTE", Integer.parseInt(saturdayHours.getStartMinute()) / 15);
                            if (saturdayHours.getStartAmOrPm().equals("AM")) {
                                i.putExtra("START_AM_OR_PM", 0);
                            } else {
                                i.putExtra("START_AM_OR_PM", 1);
                            }

                            i.putExtra("END_HOUR", Integer.parseInt(saturdayHours.getEndHour()));
                            i.putExtra("END_MINUTE", Integer.parseInt(saturdayHours.getEndMinute()) / 15);
                            if (saturdayHours.getEndAmOrPm().equals("AM")) {
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

        //Save changes
        /*Update.setOnClickListener(new View.OnClickListener() {
            //String saveUpdatedHours;
            @Override
            public void onClick(View v) {
                com.example.cd.shiftreminder.WorkReaderDbHelper workReaderDbHelper = new com.example.cd.shiftreminder.WorkReaderDbHelper(getBaseContext());

                // Gets the data repository in write mode
                SQLiteDatabase db = workReaderDbHelper.getWritableDatabase();

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(WorkReaderContract.WorkEntry.COLUMN_NAME_START_HOUR, "");
                values.put(WorkReaderContract.WorkEntry.COLUMN_NAME_START_MINUTE, "");
                values.put(WorkReaderContract.WorkEntry.COLUMN_NAME_START_AM_OR_PM, "");
                values.put(WorkReaderContract.WorkEntry.COLUMN_NAME_END_HOUR, "");
                values.put(WorkReaderContract.WorkEntry.COLUMN_NAME_END_MINUTE, "");
                values.put(WorkReaderContract.WorkEntry.COLUMN_NAME_END_AM_OR_PM, "");

                long newRowId = db.insert(FeedEntry.TABLE_NAME, null, values);
            }
        });
        */

        intent = getIntent();


        if (savedInstanceState == null) {
            //week[0] = "THE GOOGLE CALENDAR API SUCKS MASSIVE DICK"
            week = new ArrayList<ArrayList<String>>();
            //week.clear(); //stupid hack. Don't ask.

            week.add(WorkReaderContract.WorkEntry.SUNDAY, new myArrayList());
            week.add(WorkReaderContract.WorkEntry.MONDAY, new myArrayList());
            week.add(WorkReaderContract.WorkEntry.TUESDAY, new myArrayList());
            week.add(WorkReaderContract.WorkEntry.WEDNESAY, new myArrayList());
            week.add(WorkReaderContract.WorkEntry.THURSDAY, new myArrayList());
            week.add(WorkReaderContract.WorkEntry.FRIDAY, new myArrayList());
            week.add(WorkReaderContract.WorkEntry.SATURDAY, new myArrayList());

            sundayHours = intent.getParcelableExtra("SundayHours");
            week.get(WorkReaderContract.WorkEntry.SUNDAY).add(0, sundayHours.getDayOfWeek());
            week.get(WorkReaderContract.WorkEntry.SUNDAY).add(1, sundayHours.getStartHour());
            week.get(WorkReaderContract.WorkEntry.SUNDAY).add(2, sundayHours.getStartMinute());
            week.get(WorkReaderContract.WorkEntry.SUNDAY).add(3, sundayHours.getStartAmOrPm());
            week.get(WorkReaderContract.WorkEntry.SUNDAY).add(4, sundayHours.getEndHour());
            week.get(WorkReaderContract.WorkEntry.SUNDAY).add(5, sundayHours.getEndMinute());
            week.get(WorkReaderContract.WorkEntry.SUNDAY).add(6, sundayHours.getStartAmOrPm());

            mondayHours = intent.getParcelableExtra("MondayHours");
            week.get(WorkReaderContract.WorkEntry.MONDAY).add(0, mondayHours.getDayOfWeek());
            week.get(WorkReaderContract.WorkEntry.MONDAY).add(1, mondayHours.getStartHour());
            week.get(WorkReaderContract.WorkEntry.MONDAY).add(2, mondayHours.getStartMinute());
            week.get(WorkReaderContract.WorkEntry.MONDAY).add(3, mondayHours.getStartAmOrPm());
            week.get(WorkReaderContract.WorkEntry.MONDAY).add(4, mondayHours.getEndHour());
            week.get(WorkReaderContract.WorkEntry.MONDAY).add(5, mondayHours.getEndMinute());
            week.get(WorkReaderContract.WorkEntry.MONDAY).add(6, mondayHours.getStartAmOrPm());

            tuesdayHours = intent.getParcelableExtra("TuesdayHours");
            week.get(WorkReaderContract.WorkEntry.TUESDAY).add(0, tuesdayHours.getDayOfWeek());
            week.get(WorkReaderContract.WorkEntry.TUESDAY).add(1, tuesdayHours.getStartHour());
            week.get(WorkReaderContract.WorkEntry.TUESDAY).add(2, tuesdayHours.getStartMinute());
            week.get(WorkReaderContract.WorkEntry.TUESDAY).add(3, tuesdayHours.getStartAmOrPm());
            week.get(WorkReaderContract.WorkEntry.TUESDAY).add(4, tuesdayHours.getEndHour());
            week.get(WorkReaderContract.WorkEntry.TUESDAY).add(5, tuesdayHours.getEndMinute());
            week.get(WorkReaderContract.WorkEntry.TUESDAY).add(6, tuesdayHours.getStartAmOrPm());

            wednesdayHours = intent.getParcelableExtra("WednesdayHours");
            week.get(WorkReaderContract.WorkEntry.WEDNESAY).add(0, wednesdayHours.getDayOfWeek());
            week.get(WorkReaderContract.WorkEntry.WEDNESAY).add(1, wednesdayHours.getStartHour());
            week.get(WorkReaderContract.WorkEntry.WEDNESAY).add(2, wednesdayHours.getStartMinute());
            week.get(WorkReaderContract.WorkEntry.WEDNESAY).add(3, wednesdayHours.getStartAmOrPm());
            week.get(WorkReaderContract.WorkEntry.WEDNESAY).add(4, wednesdayHours.getEndHour());
            week.get(WorkReaderContract.WorkEntry.WEDNESAY).add(5, wednesdayHours.getEndMinute());
            week.get(WorkReaderContract.WorkEntry.WEDNESAY).add(6, wednesdayHours.getStartAmOrPm());

            thursdayHours = intent.getParcelableExtra("ThursdayHours");
            week.get(WorkReaderContract.WorkEntry.THURSDAY).add(0, thursdayHours.getDayOfWeek());
            week.get(WorkReaderContract.WorkEntry.THURSDAY).add(1, thursdayHours.getStartHour());
            week.get(WorkReaderContract.WorkEntry.THURSDAY).add(2, thursdayHours.getStartMinute());
            week.get(WorkReaderContract.WorkEntry.THURSDAY).add(3, thursdayHours.getStartAmOrPm());
            week.get(WorkReaderContract.WorkEntry.THURSDAY).add(4, thursdayHours.getEndHour());
            week.get(WorkReaderContract.WorkEntry.THURSDAY).add(5, thursdayHours.getEndMinute());
            week.get(WorkReaderContract.WorkEntry.THURSDAY).add(6, thursdayHours.getStartAmOrPm());

            fridayHours = intent.getParcelableExtra("FridayHours");
            week.get(WorkReaderContract.WorkEntry.FRIDAY).add(0, fridayHours.getDayOfWeek());
            week.get(WorkReaderContract.WorkEntry.FRIDAY).add(1, fridayHours.getStartHour());
            week.get(WorkReaderContract.WorkEntry.FRIDAY).add(2, fridayHours.getStartMinute());
            week.get(WorkReaderContract.WorkEntry.FRIDAY).add(3, fridayHours.getStartAmOrPm());
            week.get(WorkReaderContract.WorkEntry.FRIDAY).add(4, fridayHours.getEndHour());
            week.get(WorkReaderContract.WorkEntry.FRIDAY).add(5, fridayHours.getEndMinute());
            week.get(WorkReaderContract.WorkEntry.FRIDAY).add(6, fridayHours.getStartAmOrPm());

            saturdayHours = intent.getParcelableExtra("SaturdayHours");
            week.get(WorkReaderContract.WorkEntry.SATURDAY).add(0, saturdayHours.getDayOfWeek());
            week.get(WorkReaderContract.WorkEntry.SATURDAY).add(1, saturdayHours.getStartHour());
            week.get(WorkReaderContract.WorkEntry.SATURDAY).add(2, saturdayHours.getStartMinute());
            week.get(WorkReaderContract.WorkEntry.SATURDAY).add(3, saturdayHours.getStartAmOrPm());
            week.get(WorkReaderContract.WorkEntry.SATURDAY).add(4, saturdayHours.getEndHour());
            week.get(WorkReaderContract.WorkEntry.SATURDAY).add(5, saturdayHours.getEndMinute());
            week.get(WorkReaderContract.WorkEntry.SATURDAY).add(6, saturdayHours.getStartAmOrPm());


            //Force debug mode
            //mondayHours.setDayofWeek("OFF");
            //week.get(1).remove(0);
            //week.get(1).add(0, mondayHours.getDayOfWeek());

            dayPair = new HashMap<>();
            dayList = new ArrayList();
            dayPair.put(WorkReaderContract.WorkEntry.SUNDAY, sundayHours.getDayOfWeek());
            dayPair.put(WorkReaderContract.WorkEntry.MONDAY, mondayHours.getDayOfWeek());
            dayPair.put(WorkReaderContract.WorkEntry.TUESDAY, tuesdayHours.getDayOfWeek());
            dayPair.put(WorkReaderContract.WorkEntry.WEDNESAY, wednesdayHours.getDayOfWeek());
            dayPair.put(WorkReaderContract.WorkEntry.THURSDAY, thursdayHours.getDayOfWeek());
            dayPair.put(WorkReaderContract.WorkEntry.FRIDAY, fridayHours.getDayOfWeek());
            dayPair.put(WorkReaderContract.WorkEntry.SATURDAY, saturdayHours.getDayOfWeek());

            dayList.add(WorkReaderContract.WorkEntry.SUNDAY, dayPair);
            dayList.add(WorkReaderContract.WorkEntry.MONDAY, dayPair);
            dayList.add(WorkReaderContract.WorkEntry.TUESDAY, dayPair);
            dayList.add(WorkReaderContract.WorkEntry.WEDNESAY, dayPair);
            dayList.add(WorkReaderContract.WorkEntry.THURSDAY, dayPair);
            dayList.add(WorkReaderContract.WorkEntry.FRIDAY, dayPair);
            dayList.add(WorkReaderContract.WorkEntry.SATURDAY, dayPair);


            //if (savedInstanceState == null) {
            //adapter = new WS<String>(this,
            //        R.layout.schedule_list, values); //Bug in values because values in null

            adapter = new WS(this,
                    R.layout.schedule_list, week); //Bug in values because values in null!!!

            //adapter.notifyDataSetChanged(); //added on 3 - 4 - 2019 for debugging purposes
            setListAdapter(adapter);
        } else {

            week = new ArrayList<ArrayList<String>>();
            //week.clear(); //stupid hack. Don't ask.

            week.add(0, new myArrayList());
            week.add(1, new myArrayList());
            week.add(2, new myArrayList());
            week.add(3, new myArrayList());
            week.add(4, new myArrayList());
            week.add(5, new myArrayList());
            week.add(6, new myArrayList());

            sundayHours = intent.getParcelableExtra("SundayHours");
            week.get(0).add(0, savedInstanceState.getString("DAY_SUNDAY"));
            week.get(0).add(1, savedInstanceState.getString("SUNDAY_START_HOUR"));
            week.get(0).add(2, savedInstanceState.getString("SUNDAY_START_MINUTE"));
            week.get(0).add(3, savedInstanceState.getString("SUNDAY_START_AM_OR_PM"));
            week.get(0).add(4, savedInstanceState.getString("SUNDAY_END_HOUR"));
            week.get(0).add(5, savedInstanceState.getString("SUNDAY_END_MINUTE"));
            week.get(0).add(6, savedInstanceState.getString("SUNDAY_END_AM_OR_PM"));

            mondayHours = intent.getParcelableExtra("MondayHours");
            week.get(1).add(0, savedInstanceState.getString("DAY_MONDAY"));
            week.get(1).add(1, savedInstanceState.getString("MONDAY_START_HOUR"));
            week.get(1).add(2, savedInstanceState.getString("MONDAY_START_MINUTE"));
            week.get(1).add(3, savedInstanceState.getString("MONDAY_START_AM_OR_PM"));
            week.get(1).add(4, savedInstanceState.getString("MONDAY_END_HOUR"));
            week.get(1).add(5, savedInstanceState.getString("MONDAY_END_MINUTE"));
            week.get(1).add(6, savedInstanceState.getString("MONDAY_END_AM_OR_PM"));

            tuesdayHours = intent.getParcelableExtra("TuesdayHours");
            week.get(2).add(0, savedInstanceState.getString("DAY_TUESDAY"));
            week.get(2).add(1, savedInstanceState.getString("TUESDAY_START_HOUR"));
            week.get(2).add(2, savedInstanceState.getString("TUESDAY_START_MINUTE"));
            week.get(2).add(3, savedInstanceState.getString("TUESDAY_START_AM_OR_PM"));
            week.get(2).add(4, savedInstanceState.getString("TUESDAY_END_HOUR"));
            week.get(2).add(5, savedInstanceState.getString("TUESDAY_END_MINUTE"));
            week.get(2).add(6, savedInstanceState.getString("TUESDAY_END_AM_OR_PM"));

            wednesdayHours = intent.getParcelableExtra("WednesdayHours");
            week.get(3).add(0, savedInstanceState.getString("DAY_WEDNESDAY"));
            week.get(3).add(1, savedInstanceState.getString("WEDNESDAY_START_HOUR"));
            week.get(3).add(2, savedInstanceState.getString("WEDNESDAY_START_MINUTE"));
            week.get(3).add(3, savedInstanceState.getString("WEDNESDAY_START_AM_OR_PM"));
            week.get(3).add(4, savedInstanceState.getString("WEDNESDAY_END_HOUR"));
            week.get(3).add(5, savedInstanceState.getString("WEDNESDAY_END_MINUTE"));
            week.get(3).add(6, savedInstanceState.getString("WEDNESDAY_END_AM_OR_PM"));

            thursdayHours = intent.getParcelableExtra("ThursdayHours");
            week.get(4).add(0, savedInstanceState.getString("DAY_THURSDAY"));
            week.get(4).add(1, savedInstanceState.getString("THURSDAY_START_HOUR"));
            week.get(4).add(2, savedInstanceState.getString("THURSDAY_START_MINUTE"));
            week.get(4).add(3, savedInstanceState.getString("THURSDAY_START_AM_OR_PM"));
            week.get(4).add(4, savedInstanceState.getString("THURSDAY_END_HOUR"));
            week.get(4).add(5, savedInstanceState.getString("THURSDAY_END_MINUTE"));
            week.get(4).add(6, savedInstanceState.getString("THURSDAY_END_AM_OR_PM"));

            fridayHours = intent.getParcelableExtra("FridayHours");
            week.get(5).add(0, savedInstanceState.getString("DAY_FRIDAY"));
            week.get(5).add(1, savedInstanceState.getString("FRIDAY_START_HOUR"));
            week.get(5).add(2, savedInstanceState.getString("FRIDAY_START_MINUTE"));
            week.get(5).add(3, savedInstanceState.getString("FRIDAY_START_AM_OR_PM"));
            week.get(5).add(4, savedInstanceState.getString("FRIDAY_END_HOUR"));
            week.get(5).add(5, savedInstanceState.getString("FRIDAY_END_MINUTE"));
            week.get(5).add(6, savedInstanceState.getString("FRIDAY_END_AM_OR_PM"));

            saturdayHours = intent.getParcelableExtra("SaturdayHours");
            week.get(6).add(0, savedInstanceState.getString("DAY_SATURDAY"));
            week.get(6).add(1, savedInstanceState.getString("SATURDAY_START_HOUR"));
            week.get(6).add(2, savedInstanceState.getString("SATURDAY_START_MINUTE"));
            week.get(6).add(3, savedInstanceState.getString("SATURDAY_START_AM_OR_PM"));
            week.get(6).add(4, savedInstanceState.getString("SATURDAY_END_HOUR"));
            week.get(6).add(5, savedInstanceState.getString("SATURDAY_END_MINUTE"));
            week.get(6).add(6, savedInstanceState.getString("SATURDAY_END_AM_OR_PM"));

            //mondayHours.setDayofWeek("OFF");
            //week.get(1).remove(0);
            //week.get(1).add(0, mondayHours.getDayOfWeek());

            dayPair = new HashMap<>();
            dayList = new ArrayList();
            /*dayPair.put(0, sundayHours.getDayOfWeek());
            dayPair.put(1, mondayHours.getDayOfWeek());
            dayPair.put(2, tuesdayHours.getDayOfWeek());
            dayPair.put(3, wednesdayHours.getDayOfWeek());
            dayPair.put(4, thursdayHours.getDayOfWeek());
            dayPair.put(5, fridayHours.getDayOfWeek());
            dayPair.put(6, saturdayHours.getDayOfWeek());
            */

            dayPair.put(0, savedInstanceState.getString("DAY_SUNDAY"));
            dayPair.put(1, savedInstanceState.getString("DAY_MONDAY"));
            dayPair.put(2, savedInstanceState.getString("DAY_TUESDAY"));
            dayPair.put(3, savedInstanceState.getString("DAY_WEDNESDAY"));
            dayPair.put(4, savedInstanceState.getString("DAY_THURSDAY"));
            dayPair.put(5, savedInstanceState.getString("DAY_FRIDAY"));
            dayPair.put(6, savedInstanceState.getString("DAY_SATURDAY"));
            dayList.add(0, dayPair);
            dayList.add(1, dayPair);
            dayList.add(2, dayPair);
            dayList.add(3, dayPair);
            dayList.add(4, dayPair);
            dayList.add(5, dayPair);
            dayList.add(6, dayPair);

            adapter = new WS(this,
                    R.layout.schedule_list, week); //Bug in values because values in null!!!

            //adapter.notifyDataSetChanged(); //added on 3 - 4 - 2019 for debugging purposes
            setListAdapter(adapter);


        }
    } //end onCreate()

    //Added on 3 - 28 - 2019
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
        //Bundle map = new Bundle();
        outState.putString("DAY_SUNDAY", sundayHours.getDayOfWeek());
        outState.putString("SUNDAY_START_HOUR", sundayHours.getStartHour());
        outState.putString("SUNDAY_START_MINUTE", sundayHours.getStartMinute());
        outState.putString("SUNDAY_START_AM_OR_PM", sundayHours.getStartAmOrPm());
        outState.putString("SUNDAY_END_HOUR", sundayHours.getEndHour());
        outState.putString("SUNDAY_END_MINUTE", sundayHours.getEndMinute());
        outState.putString("SUNDAY_END_AM_OR_PM", sundayHours.getEndAmOrPm());

        /*map.putString(outState.getString("DAY_SUNDAY"), "N/A"); //
        map.putString(outState.getString("SUNDAY_START_HOUR"), "N/A");
        map.putString(outState.getString("SUNDAY_START_MINUTE"), "N/A");
        map.putString(outState.getString("SUNDAY_START_AM_OR_MAP"), "N/A");
        outState.putBundle("SAVED_WEEK", map);
        */

        outState.putString("DAY_MONDAY", mondayHours.getDayOfWeek());
        outState.putString("MONDAY_START_HOUR", mondayHours.getStartHour());
        outState.putString("MONDAY_START_MINUTE", mondayHours.getStartMinute());
        outState.putString("MONDAY_START_AM_OR_PM", mondayHours.getStartAmOrPm());
        outState.putString("MONDAY_END_HOUR", mondayHours.getEndHour());
        outState.putString("MONDAY_END_MINUTE", mondayHours.getEndMinute());
        outState.putString("MONDAY_END_AM_OR_PM", mondayHours.getEndAmOrPm());

        outState.putString("DAY_TUESDAY", tuesdayHours.getDayOfWeek());
        outState.putString("TUESDAY_START_HOUR", tuesdayHours.getStartHour());
        outState.putString("TUESDAY_START_MINUTE", tuesdayHours.getStartMinute());
        outState.putString("TUESDAY_START_AM_OR_PM", tuesdayHours.getStartAmOrPm());
        outState.putString("TUESDAY_END_HOUR", tuesdayHours.getEndHour());
        outState.putString("TUESDAY_END_MINUTE", tuesdayHours.getEndMinute());
        outState.putString("TUESDAY_END_AM_OR_PM", tuesdayHours.getEndAmOrPm());

        outState.putString("DAY_WEDNESDAY", wednesdayHours.getDayOfWeek());
        outState.putString("WEDNESDAY_START_HOUR", wednesdayHours.getStartHour());
        outState.putString("WEDNESDAY_START_MINUTE", wednesdayHours.getStartMinute());
        outState.putString("WEDNESDAY_START_AM_OR_PM", wednesdayHours.getStartAmOrPm());
        outState.putString("WEDNESDAY_END_HOUR", wednesdayHours.getEndHour());
        outState.putString("WEDNESDAY_END_MINUTE", wednesdayHours.getEndMinute());
        outState.putString("WEDNESDAY_END_AM_OR_PM", wednesdayHours.getEndAmOrPm());

        outState.putString("DAY_THURSDAY", thursdayHours.getDayOfWeek());
        outState.putString("THURSDAY_START_HOUR", thursdayHours.getStartHour());
        outState.putString("THURSDAY_START_MINUTE", thursdayHours.getStartMinute());
        outState.putString("THURSDAY_START_AM_OR_PM", thursdayHours.getStartAmOrPm());
        outState.putString("THURSDAY_END_HOUR", thursdayHours.getEndHour());
        outState.putString("THURSDAY_END_MINUTE", thursdayHours.getEndMinute());
        outState.putString("THURSDAY_END_AM_OR_PM", thursdayHours.getEndAmOrPm());

        outState.putString("DAY_FRIDAY", fridayHours.getDayOfWeek());
        outState.putString("FRIDAY_START_HOUR", fridayHours.getStartHour());
        outState.putString("FRIDAY_START_MINUTE", fridayHours.getStartMinute());
        outState.putString("FRIDAY_START_AM_OR_PM", fridayHours.getStartAmOrPm());
        outState.putString("FRIDAY_END_HOUR", fridayHours.getEndHour());
        outState.putString("FRIDAY_END_MINUTE", fridayHours.getEndMinute());
        outState.putString("FRIDAY_END_AM_OR_PM", fridayHours.getEndAmOrPm());

        outState.putString("DAY_SATURDAY", saturdayHours.getDayOfWeek());
        outState.putString("SATURDAY_START_HOUR", saturdayHours.getStartHour());
        outState.putString("SATURDAY_START_MINUTE", saturdayHours.getStartMinute());
        outState.putString("SATURDAY_START_AM_OR_PM", saturdayHours.getStartAmOrPm());
        outState.putString("SATURDAY_END_HOUR", saturdayHours.getEndHour());
        outState.putString("SATURDAY_END_MINUTE", saturdayHours.getEndMinute());
        outState.putString("SATURDAY_END_AM_OR_PM", saturdayHours.getEndAmOrPm());

        super.onSaveInstanceState(outState);
    }

    //Added on 3 - 26 - 2019
    private class myArrayList extends ArrayList {
        @Override
        public String toString() {
            return "MIT MAGIC-COOKIE";
        }
    }

    private class WS<String> extends ArrayAdapter<java.lang.String> {
        WS(Context context, int resource, List<java.lang.String> Objects) {
            super(context, resource, Objects);
        }

        @Override
        public int getCount() {
            //return 6;
            //return len;
            return week.size();
            //return Days.days.length;
        }

        @Override
        public long getItemId(int position) {

            //return week[position].hashCode();
            return week.get(position).hashCode();
        }

        @Override
        public java.lang.String getItem(int position) {
            //return week[position];
            return week.get(position).toString();
        }

        @TargetApi(24)
        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            //super.getView(position, convertView, parent);
            View view;
            Log.e(PRODUCTION_TAG, "THE CURRENT POSITION IS: " + position);

            if (convertView == null) {
                //Log.e(PRODUCTION_TAG, "CONVERT VIEW IS NULL");

                convertView = getLayoutInflater().inflate(R.layout.schedule_list, parent, false);
                //convertView = getLayoutInflater().inflate(android.R.layout.simple_expandable_list_item_2, parent, false);
            } /*else {
                view = convertView;
                Log.e(PRODUCTION_TAG, "NOT NULL AT POSITION: " + position);
            }
            */

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


            //((TextView) convertView.findViewById(android.R.id.text1)).setText(days[position]);

            //((TextView) view.findViewById(android.R.id.text1)).setText("-");
            // Need to erase values that trail "off" header byes??
            if (week.get(position).get(0).equals("OFF")) {
                ((TextView) convertView.findViewById(R.id.dayOfWeek)).setText(days[position]);
                ((TextView) convertView.findViewById(R.id.startTime)).setText("");
                ((TextView) convertView.findViewById(R.id.hour_separator)).setText("");
                ((TextView) convertView.findViewById(R.id.endTime)).setText("");


            } else {
                //week.remove(position);
                //week.get(0).add(dayPair.get(position));
                ((TextView) convertView.findViewById(R.id.dayOfWeek)).setText(days[position]);
                ((TextView) convertView.findViewById(R.id.startTime))
                        .setText(week.get(position).get(1) + ":" + week.get(position).get(2) + " " + week.get(position).get(3));
                ((TextView) convertView.findViewById(R.id.hour_separator)).setText("to");
                ((TextView) convertView.findViewById(R.id.endTime))
                        .setText(week.get(position).get(4) + ":" + week.get(position).get(5) + " " + week.get(position).get(6));
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
            //text_day.setMinimumHeight(0); // Min Height
            //text_day.setHeight(120); // Height in pixels. Not dip?

            //text_day.setText(days[position]);

            //Update selected day
            //if (intent.getIntExtra(getString(R.string.com_example_cd_shiftreminder_POSITION), 0)  == position) {
            //    Log.e(PRODUCTION_TAG, "THE SELECTED DAY FOR UPDATING IS: " + days[position]);
            //}

            Calendar cal = Calendar.getInstance();
            if (cal.get(Calendar.DAY_OF_WEEK) == getCurrentDay(position)){
                Log.e(PRODUCTION_TAG, "THE CURRENT POSITION IS: " + currentPosition);
                if (intent.getStringExtra(getString(R.string.I_WORK_TODAY)) != null) {


                    WorkNotification.notify(getContext(), week.get(position).get(0) + " " +
                            intent.getStringExtra(getString(R.string.I_WORK_TODAY)), 0);
                    //WorkNotification.notify(getContext(), "10:40 AM", 0);
                    //WorkNotification.notify(getContext(),
                            //"Upcoming alarm", 0, "
                            //week.get(position).get(0),
                            //        intent.getStringExtra(
                            //
                            //                getString(R.string.I_WORK_TODAY))
                   //         , 0);


                    text_start_hour.setTypeface(text_start_hour.getTypeface(), Typeface.BOLD);  //vs null??
                    text_separator.setTypeface(text_separator.getTypeface(), Typeface.BOLD);  //vs null??
                    text_end_hour.setTypeface(text_end_hour.getTypeface(), Typeface.BOLD);  //vs null??
                    text_day.setTypeface(text_day.getTypeface(), Typeface.BOLD);
                    Log.e(PRODUCTION_TAG, "BOLD TEXT GOT CALLED");

                    //text_hours.setText(Html.fromHtml("<b> " + getItem(position) + "</b>"));
                    //text_day.setText(Html.fromHtml("<b> " + days[position] + "</b>"));
                }
            }

            //return super.getView(position, convertView, parent);
            return convertView;
            //return  view;
        }//end method

    }//end inner class

    //Added on 3 - 6 - 2019
    //Need to figure out how to save updated changes.
    //private void updateHours(ArrayList<String> updateCurrrentHour) {
    private void updateHours(String newStartDay, String newStartHour, String newStartMinute, String newStartAmOrPm,
                             String newEndHour, String newEndMinute, String newEndAmOrPm) {
        //int pos = list.getCheckedItemPosition();
        //week.add(currentPosition, getCurrentHours);

        ArrayList<String> updatedHour = new ArrayList<String>();
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
            case 0:
                sundayHours.setStartHour(newStartHour);
                sundayHours.setStartMinute(newStartMinute);
                sundayHours.setStartAmOrPm(newStartAmOrPm);
                sundayHours.setEndHour(newEndHour);
                sundayHours.setEndMinute(newEndMinute);
                sundayHours.setEndAmOrPm(newEndAmOrPm);
                break;
            case 1:
                mondayHours.setStartHour(newStartHour);
                mondayHours.setStartMinute(newStartMinute);
                mondayHours.setStartAmOrPm(newStartAmOrPm);
                mondayHours.setEndHour(newEndHour);
                mondayHours.setEndMinute(newEndMinute);
                mondayHours.setEndAmOrPm(newEndAmOrPm);
                break;
            case 2:
                tuesdayHours.setStartHour(newStartHour);
                tuesdayHours.setStartMinute(newStartMinute);
                tuesdayHours.setStartAmOrPm(newStartAmOrPm);
                tuesdayHours.setEndHour(newEndHour);
                tuesdayHours.setEndMinute(newEndMinute);
                tuesdayHours.setEndAmOrPm(newEndAmOrPm);
                break;
            case 3:
                wednesdayHours.setStartHour(newStartHour);
                wednesdayHours.setStartMinute(newStartMinute);
                wednesdayHours.setStartAmOrPm(newStartAmOrPm);
                wednesdayHours.setEndHour(newEndHour);
                wednesdayHours.setEndMinute(newEndMinute);
                wednesdayHours.setEndAmOrPm(newEndAmOrPm);
                break;
            case 4:
                thursdayHours.setStartHour(newStartHour);
                thursdayHours.setStartMinute(newStartMinute);
                thursdayHours.setStartAmOrPm(newStartAmOrPm);
                thursdayHours.setEndHour(newEndHour);
                thursdayHours.setEndMinute(newEndMinute);
                thursdayHours.setEndAmOrPm(newEndAmOrPm);
                break;
            case 5:
                //Emulate Intent SEND. Fuck Java.
                //intent.setAction(Intent.ACTION_SEND);
                //intent.putStringArrayListExtra(getString(R.string.com_example_cd_shiftreminder_FRIDAY), updateCurrrentHour);
                fridayHours.setStartHour(newStartHour);
                fridayHours.setStartMinute(newStartMinute);
                fridayHours.setStartAmOrPm(newStartAmOrPm);
                fridayHours.setEndHour(newEndHour);
                fridayHours.setEndMinute(newEndMinute);
                fridayHours.setEndAmOrPm(newEndAmOrPm);
                break;
            case 6:
                saturdayHours.setStartHour(newStartHour);
                saturdayHours.setStartMinute(newStartMinute);
                saturdayHours.setStartAmOrPm(newStartAmOrPm);
                saturdayHours.setEndHour(newEndHour);
                saturdayHours.setEndMinute(newEndMinute);
                saturdayHours.setEndAmOrPm(newEndAmOrPm);
                break;
        }

        //adapter.remove("foo");
        //adapter.insert(updatedHour.toString() , currentPosition);
        adapter.notifyDataSetChanged();

    }

    //Added on 2 - 27 - 2019
    //Because I don't have to pass data from the spinner to the listview
    @TargetApi(24)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Calendar cal = Calendar.getInstance();

        String newAlarmMinute = "";
        //String newDownloadDate = "";
        //newAlarmMinute = data.getIntExtra(workPreferenceIntent.getStringExtra("GET_NEW_ALARM_MINUTE"), 0);
        //if (resultCode == 1) {
        //newAlarmMinute = data.getStringExtra("GET_NEW_ALARM_MINUTE"); //Convert to Int??
        //newDownloadDate = data.getStringExtra("GET_NEW_DOWNLOAD_DATE");
        //Log.e(PRODUCTION_TAG, "THE REVISED DOWNLOAD DATE IS: " + newDownloadDate);
        //}

        //else {

        int weekPosition = data.getIntExtra(getString(R.string.DAY_OF_WEEK), 0);
        //int weekPosition = data.getIntExtra("NEW_DOWNLOAD_DATE", 0);

        newStartDay = week.get(weekPosition).get(0);
        newStartHour = data.getStringExtra(getString(R.string.START_HOUR));
        newStartMinute = data.getStringExtra(getString(R.string.START_MINUTE));
        newStartAmOrPm = data.getStringExtra(getString(R.string.START_AM_OR_PM));
        newEndHour = data.getStringExtra(getString(R.string.END_HOUR));
        newEndMinute = data.getStringExtra(getString(R.string.END_MINUTE));
        newEndAmOrPm = data.getStringExtra(getString(R.string.END_AM_OR_PM));

        //get day when I already have it??
        newWorkHours = new CurrentWorkWeek(pref, this, newStartDay,
                newStartHour, newStartMinute, newStartAmOrPm,
                newEndHour, newEndMinute, newEndAmOrPm);

        newWorkHours.convertCivilanTimeToMilitaryTime(newStartHour, newStartMinute, newStartAmOrPm);
        AlarmTimer alarmTimer = new AlarmTimer();
        alarmTimer.setAlarmTime(this, newWorkHours.getStartMilitaryHour(),
                newWorkHours.getStartMilitaryMinute(),
                Integer.parseInt(pref.getString("ALARM_MINUTES", WorkReaderContract.WorkEntry.ALARM_DEFAULT)));
        //Log.e(PRODUCTION_TAG, "THE UPDATED ALARM HOUR IS: " + newWorkHours.getStartMilitaryHour());
        //Log.e(PRODUCTION_TAG, "THE UPDATED ALARM MINUTE IS: " + newWorkHours.getStartMilitaryMinute());

        newWorkHours.setCurrentPosition(currentPosition);
        //Added getStartOfday)() to newWorkHours object??
        //if (!newStartDay.equals("OFF"))
        //mondayHours.setDayofWeek("OFF");
        //week.get(1).remove(0);
        //week.get(1).add(0, mondayHours.getDayOfWeek());

        //change currentPosition in switch from 6 back to zero??
        if (cal.get(Calendar.DAY_OF_WEEK) == getCurrentDay(weekPosition)) {
            intent.putExtra(getString(R.string.I_WORK_TODAY), newWorkHours.toString());
            //WorkNotification.notify(this,
            //        "XXX",
            //        dayList.get(currentPosition).get(currentPosition)+ " " +
            //        newWorkHours.toString(),
            //        0);
        }

        if (Calendar.DAY_OF_WEEK >= getCurrentDay(weekPosition)) {
            //get rid of nenWorkHours.getCurrentWorkHours() method?
            updateHours(newWorkHours.getDayOfWeek(), newWorkHours.getStartHour(), newWorkHours.getStartMinute(),
                    newWorkHours.getStartAmOrPm(), newWorkHours.getEndHour(), newWorkHours.getEndMinute(),
                    newWorkHours.getEndAmOrPm());


        }//end if
        //}

    }

    //Added on 4 - 5 - 2019
    private int getCurrentDay(int position) {
        int  currentDay = 0;
        switch (position) {
            case 0:
                currentDay = Calendar.SUNDAY;
                break;
            case 1:
                currentDay = Calendar.MONDAY;
                break;
            case 2:
                currentDay = Calendar.TUESDAY;
                break;
            case 3:
                currentDay = Calendar.WEDNESDAY;
                break;
            case 4:
                currentDay = Calendar.THURSDAY;
                break;
            case 5:
                currentDay = Calendar.FRIDAY;
                break;
            case 6:
                currentDay = Calendar.SATURDAY;
                break;
        }
        return currentDay;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(workAlarmReceiver);
        super.onDestroy();
    }
}//End class
